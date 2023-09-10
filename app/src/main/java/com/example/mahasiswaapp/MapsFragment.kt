package com.example.mahasiswaapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONException

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionCode = 1
    private var locationPermissionGranted = false

    private lateinit var currentLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Cek izin lokasi
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
            enableMyLocation()
        } else {
            // Jika izin belum diberikan, minta izin
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
                enableMyLocation()
            }
        }
    }

    private fun enableMyLocation() {
        if (locationPermissionGranted) {
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            currentLocation = location
                            val currentLatLng = LatLng(location.latitude, location.longitude)
                            val markerOptions = MarkerOptions()
                                .position(currentLatLng)
                                .title("Lokasi Anda")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            mMap.addMarker(markerOptions)
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                            direction()
                        }
                    }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun direction() {
        val requestQueue = Volley.newRequestQueue(requireContext())
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "destination=-0.8362337354785704, 119.89376880355402" +
                "&origin=${currentLocation.latitude},${currentLocation.longitude}" +
                "&mode=driving" +
                "&key=AIzaSyCZbOluL8fKo92Hir3DEH0ux2uwlatGSS4"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val status = response.getString("status")
                    if (status == "OK") {
                        val routes = response.getJSONArray("routes")

                        var points: ArrayList<LatLng>
                        var polylineOptions: PolylineOptions? = null

                        for (i in 0 until routes.length()) {
                            points = ArrayList()
                            polylineOptions = PolylineOptions()
                            val legs = routes.getJSONObject(i).getJSONArray("legs")

                            for (j in 0 until legs.length()) {
                                val steps = legs.getJSONObject(j).getJSONArray("steps")

                                for (k in 0 until steps.length()) {
                                    val polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points")
                                    val list = decodePoly(polyline)

                                    for (l in list.indices) {
                                        val position = LatLng(list[l].latitude, list[l].longitude)
                                        points.add(position)
                                    }
                                }
                            }
                            polylineOptions.addAll(points)
                            polylineOptions.width(10f)
                            polylineOptions.color(ContextCompat.getColor(requireContext(), R.color.colorLine))
                            polylineOptions.geodesic(true)
                        }
                        if (polylineOptions != null) {
                            mMap.addPolyline(polylineOptions)
                        }
                        mMap.addMarker(MarkerOptions().position(LatLng(currentLocation.latitude, currentLocation.longitude)).title("Lokasi Saya"))
                        mMap.addMarker(MarkerOptions().position(LatLng(-0.8362337354785704, 119.89376880355402)).title("Universitas Tadulako"))

                        val bounds = LatLngBounds.Builder()
                            .include(LatLng(currentLocation.latitude, currentLocation.longitude))
                            .include(LatLng(-0.8362337354785704, 119.89376880355402)).build()
                        val point = Point()
                        requireActivity().windowManager.defaultDisplay.getSize(point)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 30))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> error.printStackTrace() })

        val retryPolicy = DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsonObjectRequest.retryPolicy = retryPolicy
        requestQueue.add(jsonObjectRequest)
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        val len = encoded.length
        var index = 0
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0

            do {
                b = encoded[index].toInt() - 63
                result = result or ((b and 0x1F) shl shift)
                shift += 5
                index++
            } while (b >= 0x20)

            lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1
            shift = 0
            result = 0

            do {
                b = encoded[index].toInt() - 63
                result = result or ((b and 0x1F) shl shift)
                shift += 5
                index++
            } while (b >= 0x20)

            lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

            val p = LatLng(
                (lat.toDouble() / 1E5),
                (lng.toDouble() / 1E5)
            )
            poly.add(p)
        }
        return poly
    }
}





