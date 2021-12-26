package com.example.caloriecalculator.fragments.rationsByDay

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecalculator.databinding.FragmentRationsByDayBinding
import com.example.caloriecalculator.relations.RationWithProduct
import com.example.caloriecalculator.viewmodel.RationViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RationsByDayFragment : Fragment() {

    private var _binding: FragmentRationsByDayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRationsByDayBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerview
        val adapter = RationsByDayAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var rationsByDay: MutableList<RationByDay>

        viewModel.getAll.observe(viewLifecycleOwner, {

            GlobalScope.launch(Dispatchers.Main) {
                rationsByDay = viewModel.getRationsByDay(it) as MutableList<RationByDay>
                adapter.setListData(rationsByDay)
            }

        })

        return binding.root
    }

}