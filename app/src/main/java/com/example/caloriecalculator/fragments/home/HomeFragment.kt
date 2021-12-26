package com.example.caloriecalculator.fragments.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.FragmentHomeBinding
import com.example.caloriecalculator.fragments.rationList.RationListAdapter
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import com.example.caloriecalculator.viewmodel.RationViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RationViewModel by viewModels()

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.navigationBarColor = Color.BLACK
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView

        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val currentDateTime = Calendar.getInstance().time
        val startOfDay = viewModel.getStartOfDay(currentDateTime)
        val endOfDay = viewModel.getEndOfDay(currentDateTime)

        var calories = 0
        var proteins = 0
        var fats = 0
        var carbohydrates = 0

        binding.date.text = formatter.format(currentDateTime)

        val adapter = container?.let { RationListAdapter(it.context, viewModel) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productListFragment)
        }

        binding.showRationList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rationsByDayFragment)
        }

        viewModel.getTodayRation(startOfDay, endOfDay).observe(viewLifecycleOwner, {

            Log.d("RAT", "${it.take(4)}")
            if (it.isNotEmpty()) {
                adapter?.setListData(it.takeLast(4).reversed() as MutableList<RationWithProduct>)
                for (item in it) {

                    calories += item.ration_calories
                    proteins += item.ration_proteins
                    fats += item.ration_fats
                    carbohydrates += item.ration_carbohydrates
                }

                binding.calories.text = "$calories ккал"
                binding.proteins.text = "белки $proteins"
                binding.fats.text = "жиры $fats"
                binding.carbohydrates.text = "углев $carbohydrates"
            }
            else {
                binding.calories.text = "0 ккал"
                binding.proteins.text = "белки 0"
                binding.fats.text = "жиры 0"
                binding.carbohydrates.text = "углев 0"
            }

        } )

        return binding.root
    }
}


