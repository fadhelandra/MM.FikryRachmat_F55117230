package com.example.mahasiswaapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.mahasiswaapp.FakultasActivity
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.adapter.FakultasAdapter
import com.example.mahasiswaapp.adapter.HomeAdapter
import com.example.mahasiswaapp.database.DatabaseHelper
import com.example.mahasiswaapp.database.Fakultas
import com.example.mahasiswaapp.manager.SessionManager

class HomeFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var buttonLogout: Button
    private lateinit var tvLihatSemua: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var fakultasList: ArrayList<Fakultas>
    private lateinit var listView: ListView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        listView = view.findViewById(R.id.listViewFakultas)

        loadFakultasData()
        setupListView()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        }

        sessionManager = SessionManager(requireContext())
        tvLihatSemua = view.findViewById(R.id.tvLihatSemua)
        buttonLogout = view.findViewById(R.id.buttonLogout)

        // Cek apakah pengguna sudah masuk
        if (sessionManager.isLoggedIn()) {
            val nim = sessionManager.getNim()
            val nama = sessionManager.getNama()
            val jurusan = sessionManager.getJurusan()

            val textViewNama = view.findViewById<TextView>(R.id.tvNama)
            val textViewNIM = view.findViewById<TextView>(R.id.tvNIM)
            val textViewJurusan = view.findViewById<TextView>(R.id.tvJurusan)

            textViewNama.text = "$nama"
            textViewNIM.text = "$nim"
            textViewJurusan.text = "$jurusan"

        } else {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        tvLihatSemua.setOnClickListener {
            val intent = Intent(requireContext(), FakultasActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        buttonLogout.setOnClickListener {
            sessionManager.logout()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    private fun loadFakultasData() {
        fakultasList = ArrayList(databaseHelper.readFakultas())
    }

    private fun setupListView() {
        val adapter = HomeAdapter(requireContext(), fakultasList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFakultas = fakultasList[position]
            // Lakukan sesuatu dengan item yang dipilih (jika diperlukan)
        }
    }
}
