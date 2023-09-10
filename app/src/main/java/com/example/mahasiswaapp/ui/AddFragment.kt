package com.example.mahasiswaapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.mahasiswaapp.R
import com.example.mahasiswaapp.adapter.CustomListViewAdapter
import com.example.mahasiswaapp.adapter.DataModel

class AddFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: CustomListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        listView = view.findViewById(R.id.listView)
        adapter = CustomListViewAdapter(requireContext(), getSampleData())
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        }

        return view
    }

    private fun getSampleData(): ArrayList<DataModel> {
        val data = ArrayList<DataModel>()
        data.add(DataModel(1, "Fakultas A"))
        data.add(DataModel(2, "Fakultas B"))
        data.add(DataModel(3, "Fakultas C"))
        // Tambahkan data lainnya sesuai kebutuhan
        return data
    }
}