package com.example.caloriecalculator.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import java.util.*

@Dao
interface RationDao {

    @Transaction
    @Query("SELECT ration.id as ration_id, ration.created_at, ration.weight, ration.product_id," +
            "product.name, product.id, " +
            "(ration.weight / 100 * product.proteins) as ration_proteins, " +
            "(ration.weight / 100 * product.fats) as ration_fats, " +
            "(ration.weight / 100 * product.carbohydrates) as ration_carbohydrates, " +
            "(ration.weight / 100 * product.calories) as ration_calories  " +
            "FROM ration INNER JOIN product WHERE product.id = ration.product_id ORDER BY created_at DESC")
    fun getAll(): LiveData<List<RationWithProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRation(ration: Ration)

    @Update
    suspend fun updateRation(ration: Ration)

    @Query("DELETE FROM ration WHERE id = :ration_id")
    suspend fun deleteRationById(ration_id: Int)

    @Transaction
    @Query("SELECT ration.id as ration_id, ration.created_at, ration.weight, ration.product_id," +
            " product.name, product.id, " +
            "(ration.weight / 100 * product.proteins) as ration_proteins, " +
            "(ration.weight / 100 * product.fats) as ration_fats, " +
            "(ration.weight / 100 * product.carbohydrates) as ration_carbohydrates, " +
            "(ration.weight / 100 * product.calories) as ration_calories  " +
            "FROM ration INNER JOIN product WHERE product.id = ration.product_id AND created_at BETWEEN :startDate AND :endDate ")
    fun getTodayRation(startDate: Date, endDate: Date): LiveData<List<RationWithProduct>>

}