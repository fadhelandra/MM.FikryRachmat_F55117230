package com.example.mahasiswaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mahasiswaapp.adapter.FakultasAdapter
import com.example.mahasiswaapp.database.DatabaseHelper
import com.example.mahasiswaapp.database.Fakultas

class FakultasActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var fakultasList: ArrayList<Fakultas>
    private lateinit var listView: ListView
    private lateinit var emptyView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fakultas)

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listView)
        emptyView = findViewById(R.id.emptyView)

        loadFakultasData()
        setupListView()

        findViewById<View>(R.id.fabAboveBottomNav).setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add, null)
        val editTextFakultas = dialogView.findViewById<EditText>(R.id.editTextFakultas)
        val errorMessageTextView = dialogView.findViewById<TextView>(R.id.errorMessage)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Tambah Fakultas")
            .setView(dialogView)
            .setPositiveButton("Tambah", null)
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.setOnShowListener {
            // Tambahkan tindakan ke tombol "Tambah" setelah dialog ditampilkan
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val namaFakultas = editTextFakultas.text.toString().trim()
                if (namaFakultas.isNotEmpty()) {
                    val newFakultas = Fakultas(0, namaFakultas)
                    databaseHelper.insertFakultas(newFakultas)
                    updateFakultasList()
                    alertDialog.dismiss()
                } else {
                    // Tampilkan pesan kesalahan dan ubah visibilitas TextView menjadi terlihat
                    errorMessageTextView.visibility = View.VISIBLE
                }
            }
        }

        alertDialog.show()
    }

    // Fungsi ini akan dipanggil ketika tombol "Edit" diklik
    fun onEditButtonClick(view: View) {
        // Dapatkan posisi item yang akan diedit dari tampilan
        val position = listView.getPositionForView(view)

        // Pastikan posisi valid
        if (position != ListView.INVALID_POSITION) {
            // Dapatkan objek Fakultas yang akan diedit dari daftar
            val fakultas = fakultasList[position]

            // Tampilkan dialog modal pengeditan
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)
            val editTextFakultas = dialogView.findViewById<EditText>(R.id.editTextFakultas)
            val errorMessageTextView = dialogView.findViewById<TextView>(R.id.errorMessage)

            // Set teks di EditText sesuai dengan data yang ada
            editTextFakultas.setText(fakultas.namaFakultas)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Edit Fakultas")
                .setView(dialogView)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.setOnShowListener {
                // Tambahkan tindakan ke tombol "Simpan" setelah dialog ditampilkan
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    val namaFakultasBaru = editTextFakultas.text.toString().trim()

                    if (namaFakultasBaru.isNotEmpty()) {
                        // Perbarui data di database
                        fakultas.namaFakultas = namaFakultasBaru
                        databaseHelper.updateFakultas(fakultas)

                        // Perbarui tampilan ListView
                        updateFakultasList()

                        alertDialog.dismiss()
                    } else {
                        // Tampilkan pesan kesalahan dan ubah visibilitas TextView menjadi terlihat
                        errorMessageTextView.visibility = View.VISIBLE
                    }
                }
            }

            alertDialog.show()
        }
    }

    // Fungsi ini akan dipanggil ketika tombol "Hapus" diklik
    fun onHapusButtonClick(view: View) {
        // Dapatkan posisi item yang akan dihapus dari tampilan
        val position = listView.getPositionForView(view)

        // Pastikan posisi valid
        if (position != ListView.INVALID_POSITION) {
            // Dapatkan objek Fakultas yang akan dihapus dari daftar
            val fakultas = fakultasList[position]

            // Tampilkan konfirmasi dialog sebelum menghapus dengan menampilkan nama fakultas yang akan dihapus
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin Menghapus Data '${fakultas.namaFakultas}'?")
                .setPositiveButton("Ya") { _, _ ->
                    // Hapus fakultas dari database
                    databaseHelper.deleteFakultas(fakultas.id)

                    // Hapus fakultas dari daftar
                    fakultasList.removeAt(position)

                    // Perbarui tampilan ListView
                    (listView.adapter as FakultasAdapter).notifyDataSetChanged()

                    // Perbarui visibilitas emptyView
                    updateEmptyViewVisibility()
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun loadFakultasData() {
        fakultasList = ArrayList(databaseHelper.readFakultas())
        updateEmptyViewVisibility()
    }

    private fun setupListView() {
        val adapter = FakultasAdapter(this, fakultasList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFakultas = fakultasList[position]
            // Lakukan sesuatu dengan item yang dipilih (jika diperlukan)
        }
    }

    private fun updateEmptyViewVisibility() {
        if (fakultasList.isEmpty()) {
            emptyView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
        }
    }

    private fun updateFakultasList() {
        loadFakultasData()
        setupListView()
    }
}