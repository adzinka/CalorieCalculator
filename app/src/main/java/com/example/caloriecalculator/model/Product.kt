package com.example.caloriecalculator.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbohydrates: Int,
    var isVisible: Boolean = true
): Parcelable
