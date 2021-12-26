package com.example.caloriecalculator.fragments.rationList

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecalculator.databinding.FragmentRationListBinding
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import com.example.caloriecalculator.viewmodel.RationViewModel

class RationListFragment : Fragment() {

    private var _binding: FragmentRationListBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RationListFragmentArgs>()
    private val viewModel: RationViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRationListBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView
        val adapter = container?.let { RationListAdapter(it.context, viewModel) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dayDate = args.currentRations.date
        val startOfDay = viewModel.getStartOfDay(dayDate)
        val endOfDay = viewModel.getEndOfDay(dayDate)

        viewModel.getTodayRation(startOfDay, endOfDay).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) adapter?.setListData(it.reversed() as MutableList<RationWithProduct>)
            else binding.tv.text = "empty"
        } )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}