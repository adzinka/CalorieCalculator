package com.example.caloriecalculator.fragments.addProduct

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.FragmentAddProductBinding
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.viewmodel.ProductViewModel
import java.util.*

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)

        binding.addProductBtn.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val name = binding.name.text
        val calories = binding.calories.text
        val proteins = binding.proteins.text
        val fats = binding.fats.text
        val carbohydrates = binding.carbohydrates.text

        if (inputCheck(name, calories, proteins, fats, carbohydrates)) {
            val product = Product(0,
                name.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                Integer.parseInt(calories.toString()), Integer.parseInt(proteins.toString()),
                Integer.parseInt(fats.toString()), Integer.parseInt(carbohydrates.toString()),
                true)

            viewModel.addProduct(product)
            Toast.makeText(requireContext(), "Успешно добавлен!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addProductFragment_to_productListFragment)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста заполните все поля.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: Editable, calories: Editable, proteins: Editable, fats: Editable, carbohydrates: Editable): Boolean {
        return !(name.isEmpty() ||
                calories.isEmpty() ||
                proteins.isEmpty() ||
                fats.isEmpty() ||
                carbohydrates.isEmpty()
                )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}