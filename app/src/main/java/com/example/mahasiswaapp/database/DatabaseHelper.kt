package com.example.mahasiswaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        // Database
        private const val DATABASE_NAME = "MahasiswaDatabase.db"
        private const val DATABASE_VERSION = 1

        // Tabel Mahasiswa
        private const val TABLE_NAME = "tb_mahasiswa"
        private const val KEY_NIM = "nim"
        private const val KEY_NAMA = "nama"
        private const val KEY_JURUSAN = "jurusan"
        private const val KEY_PASSWORD = "password"

        // Tabel Fakultas
        private const val TABLE_FAKULTAS = "tb_fakultas"
        private const val KEY_ID = "fakultas_id"
        private const val KEY_FAKULTAS = "fakultas"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery1 = """
            CREATE TABLE $TABLE_NAME (
                $KEY_NIM TEXT PRIMARY KEY,
                $KEY_NAMA TEXT,
                $KEY_JURUSAN TEXT,
                $KEY_PASSWORD TEXT
            )
        """.trimIndent()

        val createTableQuery2 = """
        CREATE TABLE $TABLE_FAKULTAS (
            $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $KEY_FAKULTAS TEXT
        )
    """.trimIndent()

        db?.execSQL(createTableQuery1)
        db?.execSQL(createTableQuery2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAKULTAS")
        onCreate(db)
    }

    fun insertMahasiswa(mhs: Mahasiswa) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NIM, mhs.nim)
        values.put(KEY_NAMA, mhs.nama)
        values.put(KEY_JURUSAN, mhs.jurusan)
        values.put(KEY_PASSWORD, mhs.password)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readMahasiswa(nim: String, password: String): Mahasiswa? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $KEY_NIM = ? AND $KEY_PASSWORD = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(nim, password))

        var mahasiswa: Mahasiswa? = null

        if (cursor.moveToFirst()) {
            val nimIndex = cursor.getColumnIndex(KEY_NIM)
            val namaIndex = cursor.getColumnIndex(KEY_NAMA)
            val jurusanIndex = cursor.getColumnIndex(KEY_JURUSAN)
            val passwordIndex = cursor.getColumnIndex(KEY_PASSWORD)

            val nimResult = cursor.getString(nimIndex)
            val namaResult = cursor.getString(namaIndex)
            val jurusanResult = cursor.getString(jurusanIndex)
            val passwordResult = cursor.getString(passwordIndex)

            mahasiswa = Mahasiswa(nimResult, namaResult, jurusanResult, passwordResult)
        }

        cursor.close()
        db.close()

        return mahasiswa
    }

    fun insertFakultas(fakultas: Fakultas) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_FAKULTAS, fakultas.namaFakultas)

        db.insert(TABLE_FAKULTAS, null, values)
        db.close()
    }

    fun readFakultas(): List<Fakultas> {
        val fakultasList = ArrayList<Fakultas>()
        val selectQuery = "SELECT * FROM $TABLE_FAKULTAS"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val fakultasIndex = cursor.getColumnIndex(KEY_FAKULTAS)

                val id = cursor.getInt(idIndex)
                val namaFakultas = cursor.getString(fakultasIndex)

                val fakultas = Fakultas(id, namaFakultas)
                fakultasList.add(fakultas)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return fakultasList
    }

    fun updateFakultas(fakultas: Fakultas) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_FAKULTAS, fakultas.namaFakultas)

        // Misalnya, kita ingin melakukan perbarui berdasarkan ID
        db.update(TABLE_FAKULTAS, values, "$KEY_ID = ?", arrayOf(fakultas.id.toString()))

        db.close()
    }

    fun deleteFakultas(fakultasId: Int) {
        val db = this.writableDatabase

        // Misalnya, kita ingin melakukan hapus berdasarkan ID
        db.delete(TABLE_FAKULTAS, "$KEY_ID = ?", arrayOf(fakultasId.toString()))

        db.close()
    }
}
