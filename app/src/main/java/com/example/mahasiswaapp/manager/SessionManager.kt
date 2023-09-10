package com.example.mahasiswaapp.manager

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("login_session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun login(nim: String, password: String) {
        editor.putString("nim", nim)
        editor.putString("password", password)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        val nim = sharedPref.getString("nim", null)
        val password = sharedPref.getString("password", null)
        return !nim.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    fun setNim(nim: String) {
        editor.putString("nim", nim)
        editor.apply()
    }

    fun setNama(nama: String) {
        editor.putString("nama", nama)
        editor.apply()
    }

    fun setJurusan(jurusan: String) {
        editor.putString("jurusan", jurusan)
        editor.apply()
    }

    fun getNim(): String? {
        return sharedPref.getString("nim", null)
    }

    fun getNama(): String? {
        return sharedPref.getString("nama", null)
    }

    fun getJurusan(): String? {
        return sharedPref.getString("jurusan", null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
