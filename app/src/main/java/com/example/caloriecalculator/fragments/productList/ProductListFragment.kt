package com.example.caloriecalculator.fragments.productList

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.FragmentProductListBinding
import com.example.caloriecalculator.fragments.productList.ProductListAdapter
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.viewmodel.ProductViewModel

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerview
        val adapter = container?.let { ProductListAdapter(it.context, ::deleteProduct) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAll.observe(viewLifecycleOwner, { product ->
            adapter?.setListData(product as MutableList<Product>)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
        }

        return binding.root
    }

    private fun deleteProduct(product: Product) {
        product.isVisible = false
        viewModel.updateProduct(product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}