package com.example.caloriecalculator.fragments.addRation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.FragmentAddRationBinding
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.viewmodel.RationViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddRationFragment : Fragment() {

    private var _binding: FragmentAddRationBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddRationFragmentArgs>()
    private val viewModel: RationViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRationBinding.inflate(inflater, container, false)

        val productCalories = args.product.calories
        val productProteins = args.product.proteins
        val productFats = args.product.fats
        val productCarbohydrates = args.product.carbohydrates

        var currentCalories: Int
        var currentProteins: Int
        var currentFats: Int
        var currentCarbohydrates: Int

        binding.productName.text = args.product.name
        binding.proteins.text = "белки $productProteins"
        binding.fats.text = "жиры $productFats"
        binding.carbohydrates.text = "углев $productCarbohydrates"
        binding.caloriesWeight.text = "$productCalories ккал/100гр"

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
                binding.productName.text = args.product.name
                binding.proteins.text = "белки $productProteins"
                binding.fats.text = "жиры $productFats"
                binding.carbohydrates.text = "углев $productCarbohydrates"
                binding.caloriesWeight.text = "$productCalories ккал/100гр"
            }

        }

        binding.addRation.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertDataToDatabase() {
        val productId = args.product.id
        val weight = binding.weight.text
        val createdAt = Date()

        if (inputCheck(weight)) {
            val ration = Ration(0, productId, weight.toString().toFloat(), createdAt)

            viewModel.addRation(ration)
            Toast.makeText(requireContext(), "Успешно добавлен!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addRationFragment_to_homeFragment)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста заполните все поля.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(weight: Editable) = weight.isNotEmpty()

    private fun calculate(arg: Int, weight: Float) = weight * arg / 100

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}