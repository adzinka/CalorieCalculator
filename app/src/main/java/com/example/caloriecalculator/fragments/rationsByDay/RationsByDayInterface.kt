package com.example.caloriecalculator.fragments.rationsByDay

import com.example.caloriecalculator.relations.RationWithProduct
import java.util.*

interface RationsByDayInterface {
    fun getRationsByDay(rationsWithProduct: List<RationWithProduct>): List<RationByDay>

    fun getStartOfDay(dayDate: Date): Date
    fun getEndOfDay(dayDate: Date): Date
}