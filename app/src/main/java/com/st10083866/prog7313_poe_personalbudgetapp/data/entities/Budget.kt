package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "budgets",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
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
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userOwnerId: Int,
    val month: String,
    val year: String,
    val categoryId: Int?,
    val limitAmount: Double,
    val spentAmount: Double
)
