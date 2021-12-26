package com.example.caloriecalculator.fragments.updateRationFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.caloriecalculator.databinding.FragmentUpdateRationBinding
import com.example.caloriecalculator.fragments.rationsByDay.RationByDay
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.viewmodel.RationViewModel
import kotlinx.android.synthetic.main.fragment_update_product.*
import java.util.*

class UpdateRationFragment : Fragment() {

    private var _binding: FragmentUpdateRationBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateRationFragmentArgs>()
    private val viewModel: RationViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateRationBinding.inflate(inflater, container, false)

        val productCalories = args.currentRation.product.calories
        val productProteins = args.currentRation.product.proteins
        val productFats = args.currentRation.product.fats
        val productCarbohydrates = args.currentRation.product.carbohydrates

        Log.d("RATION1", "${args.currentRation}")
        var currentCalories: Int
        var currentProteins: Int
        var currentFats: Int
        var currentCarbohydrates: Int

        binding.productName.text = args.currentRation.product.name
        binding.proteins.text = "белки $productProteins"
        binding.fats.text = "жиры $productFats"
        binding.carbohydrates.text = "углев $productCarbohydrates"
        binding.caloriesWeight.text = "$productCalories ккал"
        binding.weight.setText("${args.currentRation.ration.weight.toInt()}")

        binding.weight.doAfterTextChanged {
            val weightEditable = binding.weight.text
            if (weightEditable.isNotEmpty()) {
                val weight = weightEditable.toString().toFloat()

                currentCalories = calculate(productCalories, weight).toInt()
                currentProteins = calculate(productProteins, weight).toInt()
                currentFats = calculate(productFats, weight).toInt()
                currentCarbohydrates = calculate(productCarbohydrates, weight).toInt()

                binding.proteins.text = "белки $currentProteins"
                binding.fats.text = "жиры $currentFats"
                binding.carbohydrates.text = "углев $currentCarbohydrates"
                binding.caloriesWeight.text = "$currentCalories ккал"
            } else {
                binding.productName.text = args.currentRation.product.name
                binding.proteins.text = "белки $productProteins"
                binding.fats.text = "жиры $productFats"
                binding.carbohydrates.text = "углев $productCarbohydrates"
                binding.caloriesWeight.text = "${args.currentRation.product.calories} ккал/100гр"
            }
        }

        binding.addRation.setOnClickListener {
            updateRation()
        }

        return binding.root
    }

    private fun updateRation() {
        val rationId = args.currentRation.ration_id
        val productId = args.currentRation.ration.productId
        val weight = binding.weight.text
        val createdAt = args.currentRation.ration.createdAt

        if (inputCheck(weight)) {

            val ration = Ration(
                    rationId,
                    productId,
                    weight.toString().toFloat(),
                    createdAt)

            val rationByDay = RationByDay(createdAt, args.currentRation.ration_calories, args.currentRation.ration_proteins, args.currentRation.ration_fats, args.currentRation.ration_carbohydrates)
            viewModel.updateRation(ration)
            Log.d("RATION2", "$rationId")
            Log.d("RAT", "$ration")
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            val action = UpdateRationFragmentDirections.actionUpdateRationFragmentToRationListFragment(rationByDay)
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(weight: Editable) = weight.isNotEmpty()

    private fun calculate(arg: Int, weight: Float) = weight * arg / 100

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}