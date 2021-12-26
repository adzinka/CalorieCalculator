package com.example.caloriecalculator.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.model.Ration
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RationWithProduct(
    @Embedded
    val ration: Ration,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: Product,
    val ration_id: Int,
    val ration_proteins: Int,
    val ration_fats: Int,
    val ration_carbohydrates: Int,
    val ration_calories: Int
): Parcelable