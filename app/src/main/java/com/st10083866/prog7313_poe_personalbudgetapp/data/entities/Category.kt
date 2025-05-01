package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userOwnerId: Int,
    val name: String,
    val description: String,
    val limit: Double,
    val spendType: String
)