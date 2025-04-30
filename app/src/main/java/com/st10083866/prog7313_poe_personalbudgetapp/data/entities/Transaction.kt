package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns =["userId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val userOwnerId: Int,
    val amount: Double,
    val type: String,
    val categoryId: Int?,
    val date: Long,
    val paymentMethod: String,
    val uploadedAt: Long = System.currentTimeMillis(),
    val uploadedPicturePath: String? = null
)