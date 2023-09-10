package com.example.mahasiswaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mahasiswaapp.FakultasActivity
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.database.Fakultas
import com.example.mahasiswaapp.ui.HomeFragment

class FakultasAdapter(private val context: Context, private val fakultasList: List<Fakultas>) : BaseAdapter() {
    override fun getCount(): Int {
        return fakultasList.size
    }

    override fun getItem(position: Int): Any {
        return fakultasList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_view, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val fakultas = getItem(position) as Fakultas
        viewHolder.tvFakultas.text = fakultas.namaFakultas

        return view
    }

    private class ViewHolder(view: View) {
        val tvFakultas: TextView = view.findViewById(R.id.tvFakultas)
    }
}