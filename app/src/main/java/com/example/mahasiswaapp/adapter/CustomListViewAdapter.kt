package com.example.mahasiswaapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mahasiswaapp.R

class CustomListViewAdapter(private val context: Context, private val data: ArrayList<DataModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
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

        val dataModel = getItem(position) as DataModel
        viewHolder.tvFakultas.text = dataModel.fakultas

        return view
    }

    private class ViewHolder(view: View) {
        val tvFakultas: TextView = view.findViewById(R.id.tvFakultas)
    }
}