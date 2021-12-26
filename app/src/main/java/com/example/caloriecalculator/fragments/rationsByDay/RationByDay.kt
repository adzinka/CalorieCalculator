package com.example.caloriecalculator.fragments.rationsByDay

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class RationByDay(
    val date: Date,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbohydrates: Int
): Parcelable
