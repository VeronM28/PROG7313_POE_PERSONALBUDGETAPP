package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "savings_goals",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SavingsGoal(
    @PrimaryKey(autoGenerate = true) val id: Int= 0,
    val userOwnerId: Int,
    val goalName: String,
    val targetAmount: Double,
    val deadline: String,
    val createdAt: String,
)