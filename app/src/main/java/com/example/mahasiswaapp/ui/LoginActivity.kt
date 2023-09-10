package com.example.mahasiswaapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mahasiswaapp.MainActivity
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.database.DatabaseHelper
import com.example.mahasiswaapp.manager.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextNIM: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextNIM = findViewById(R.id.editTextNIM)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        databaseHelper = DatabaseHelper(this)

        val sessionManager = SessionManager(this)

        buttonLogin.setOnClickListener(View.OnClickListener {
            val nim = editTextNIM.text.toString()
            val password = editTextPassword.text.toString()

            if (nim.isNotEmpty() && password.isNotEmpty()) {
                val mahasiswa = databaseHelper.readMahasiswa(nim, password)

                if (mahasiswa != null) {
                    // Login berhasil
                    sessionManager.login(nim, password)
                    sessionManager.setNim(mahasiswa.nim)
                    sessionManager.setNama(mahasiswa.nama)
                    sessionManager.setJurusan(mahasiswa.jurusan)

                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login gagal. Cek NIM dan Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Harap isi NIM dan Password", Toast.LENGTH_SHORT).show()
            }
        })

        buttonRegister.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegisActivity::class.java)
            startActivity(intent)
        })
    }
}