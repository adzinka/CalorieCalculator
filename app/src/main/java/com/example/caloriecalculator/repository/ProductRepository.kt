package com.example.caloriecalculator.repository

import androidx.lifecycle.LiveData
import com.example.caloriecalculator.data.ProductDao
import com.example.caloriecalculator.model.Product

class ProductRepository(private val productDao: ProductDao) {

    val getAll: LiveData<List<Product>> = productDao.getAll()

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

}