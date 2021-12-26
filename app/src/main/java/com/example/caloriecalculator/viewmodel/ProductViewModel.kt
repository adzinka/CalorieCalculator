package com.example.caloriecalculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.caloriecalculator.data.AppDatabase
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.repository.ProductRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProductViewModel(application: Application): AndroidViewModel(application) {

    val getAll: LiveData<List<Product>>
    private  val repository: ProductRepository

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        getAll = repository.getAll
    }

    fun addProduct(product: Product) {
        viewModelScope.launch(IO) {
            repository.addProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(IO) {
            repository.updateProduct(product)
        }
    }

}