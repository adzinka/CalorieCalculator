package com.example.caloriecalculator.repository

import androidx.lifecycle.LiveData
import com.example.caloriecalculator.data.RationDao
import com.example.caloriecalculator.fragments.rationsByDay.RationByDay
import com.example.caloriecalculator.fragments.rationsByDay.RationsByDayInterface
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import java.time.LocalDateTime
import java.util.*

class RationRepository(private val rationDao: RationDao) {

    fun getAll(): LiveData<List<RationWithProduct>> {
        return rationDao.getAll()
    }

    fun getTodayRation(startDate: Date, endDate: Date): LiveData<List<RationWithProduct>> {
        return rationDao.getTodayRation(startDate, endDate)
    }

    suspend fun addRation(ration: Ration) {
        rationDao.addRation(ration)
    }

    suspend fun updateRation(ration: Ration) {
        rationDao.updateRation(ration)
    }

    suspend fun deleteRationById(ration_id: Int) {
        rationDao.deleteRationById(ration_id)
    }

}