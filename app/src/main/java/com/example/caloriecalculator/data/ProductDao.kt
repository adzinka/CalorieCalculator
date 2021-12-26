package com.example.caloriecalculator.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.caloriecalculator.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM product ORDER BY name")
    fun getAll(): LiveData<List<Product>>

}