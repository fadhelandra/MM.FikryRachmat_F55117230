package com.example.mahasiswaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.database.DatabaseHelper
import com.example.mahasiswaapp.database.Mahasiswa

class RegisActivity : AppCompatActivity() {
    private lateinit var editTextNIM: EditText
    private lateinit var editTextNama: EditText
    private lateinit var editTextJurusan: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button

    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regis)

        editTextNIM = findViewById(R.id.editTextNIM)
        editTextNama = findViewById(R.id.editTextNama)
        editTextJurusan = findViewById(R.id.editTextJurusan)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        databaseHelper = DatabaseHelper(this)

        buttonRegister.setOnClickListener(View.OnClickListener {
            val nim = editTextNIM.text.toString()
            val nama = editTextNama.text.toString()
            val jurusan = editTextJurusan.text.toString()
            val password = editTextPassword.text.toString()

            if (nim.isNotEmpty() && nama.isNotEmpty() && jurusan.isNotEmpty() && password.isNotEmpty()) {
                val mahasiswa = Mahasiswa(nim, nama, jurusan, password)

                databaseHelper.insertMahasiswa(mahasiswa)

                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            }
        })
    }
}