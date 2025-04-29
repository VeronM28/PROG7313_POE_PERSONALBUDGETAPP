package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "savings_contributions",
    foreignKeys = [ForeignKey(
        entity = SavingsGoal::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SavingsContribution(
    @PrimaryKey(autoGenerate = true) val id: Int= 0,
    val goalId: Int,
    val amount: Double,
    val contributionDate: String
)