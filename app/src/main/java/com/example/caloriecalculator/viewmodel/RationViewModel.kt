package com.example.caloriecalculator.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.caloriecalculator.data.AppDatabase
import com.example.caloriecalculator.fragments.rationsByDay.RationByDay
import com.example.caloriecalculator.fragments.rationsByDay.RationsByDayInterface
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import com.example.caloriecalculator.repository.RationRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RationViewModel(application: Application): AndroidViewModel(application), RationsByDayInterface {

    val getAll: LiveData<List<RationWithProduct>>
    lateinit var getTodayRations: LiveData<List<RationWithProduct>>
    private val repository: RationRepository

    init {
        val rationDao = AppDatabase.getDatabase(application).rationDao()
        repository = RationRepository(rationDao)
        getAll = repository.getAll()
    }

    fun getTodayRation(startDate: Date, endDate: Date): LiveData<List<RationWithProduct>> {
        viewModelScope.launch {
            getTodayRations = repository.getTodayRation(startDate, endDate)
        }
        return getTodayRations
    }

    fun addRation(ration: Ration) {
        viewModelScope.launch(IO) {
            repository.addRation(ration)
        }
    }

    fun updateRation(ration: Ration) {
        viewModelScope.launch(IO) {
            repository.updateRation(ration)
        }
    }

    fun deleteRationById(ration_id: Int) {
        viewModelScope.launch(IO) {
            repository.deleteRationById(ration_id)
        }
        Log.d("delete", "$ration_id")
    }

    override fun getStartOfDay(date: Date): Date {
        val dayDate = Calendar.getInstance()
        dayDate.time = date
        val year = dayDate.get(Calendar.YEAR)
        val month = dayDate.get(Calendar.MONTH)
        val day = dayDate.get(Calendar.DATE)
        dayDate.set(year, month, day, 0, 0, 0)
        return dayDate.time
    }

    override fun getEndOfDay(date: Date): Date {
        val dayDate = Calendar.getInstance()
        dayDate.time = date
        val year = dayDate.get(Calendar.YEAR)
        val month = dayDate.get(Calendar.MONTH)
        val day = dayDate.get(Calendar.DATE)
        dayDate.set(year, month, day, 23, 59, 59)
        return dayDate.time
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getRationsByDay(rationsWithProduct: List<RationWithProduct>): List<RationByDay> {
        val rationsByDay = mutableListOf<RationByDay>()
        var currentCalories = 0
        var currentProteins = 0
        var currentFats = 0
        var currentCarbohydrates = 0

        var date = rationsWithProduct[0].ration.createdAt
        val formatter = SimpleDateFormat("dd..MM..yyyy")

        for (rationProduct in rationsWithProduct) {
            val rationDate = formatter.format(rationProduct.ration.createdAt)
            val currentDay = formatter.format(date)

            if (rationDate == currentDay) {
                currentCalories += rationProduct.ration_calories
                currentProteins += rationProduct.ration_proteins
                currentFats += rationProduct.ration_fats
                currentCarbohydrates += rationProduct.ration_carbohydrates

            } else {
                var rationByDay = RationByDay(date, currentCalories, currentProteins, currentFats, currentCarbohydrates)
                rationsByDay.add(rationByDay)
                date = rationProduct.ration.createdAt
                currentCalories = rationProduct.ration_calories
                currentProteins = rationProduct.ration_proteins
                currentFats = rationProduct.ration_fats
                currentCarbohydrates = rationProduct.ration_carbohydrates

                if (rationProduct == rationsWithProduct.last()) {
                    rationByDay = RationByDay(date, currentCalories, currentProteins, currentFats, currentCarbohydrates)
                    rationsByDay.add(rationByDay)
                }
            }

        }
        return rationsByDay
    }


}

