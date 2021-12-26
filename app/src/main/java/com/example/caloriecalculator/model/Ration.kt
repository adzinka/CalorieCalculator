package com.example.caloriecalculator.model

import android.os.Parcelable
import androidx.room.*
import com.example.caloriecalculator.converters.Converters
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
@Entity
data class Ration(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    val weight: Float,
    @ColumnInfo(name = "created_at") val createdAt: Date
): Parcelable
