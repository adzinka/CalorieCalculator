package com.example.caloriecalculator.fragments.updateProduct

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
import androidx.navigation.fragment.navArgs
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.FragmentUpdateProductBinding
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_update_product.*
import java.util.*

class UpdateProductFragment : Fragment() {

    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateProductFragmentArgs>()
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)

        binding.updateName.setText(args.currentProduct.name)
        binding.updateCalories.setText(args.currentProduct.calories.toString())
        binding.updateProteins.setText(args.currentProduct.proteins.toString())
        binding.updateFats.setText(args.currentProduct.fats.toString())
        binding.updateCarbohydrates.setText(args.currentProduct.carbohydrates.toString())

        binding.addProductBtn.setOnClickListener {
            updateProduct()
        }

        return binding.root
    }

    private fun updateProduct() {
        val name = update_name.text.toString()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val calories = update_calories.text
        val proteins = update_proteins.text
        val fats = update_fats.text
        val carbohydrates = update_carbohydrates.text

        if (inputCheck(name, calories, proteins, fats, carbohydrates)) {

            val updatedProduct = Product(args.currentProduct.id, name,
                                        calories.toString().toInt(),
                                        proteins.toString().toInt(),
                                        fats.toString().toInt(),
                                        carbohydrates.toString().toInt(),
                                        true)

            viewModel.updateProduct(updatedProduct)
            Toast.makeText(requireContext(), "Успешно обновлено!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateProductFragment_to_productListFragment)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста заполните все поля.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(name: String,
                           calories: Editable,
                           proteins: Editable,
                           fats: Editable,
                           carbohydrates: Editable
    ): Boolean {
        return !(TextUtils.isEmpty(name) ||
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