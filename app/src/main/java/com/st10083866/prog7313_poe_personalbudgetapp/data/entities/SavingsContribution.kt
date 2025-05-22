package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

data class SavingsContribution(
    var id: String = "",
    var goalId: String = "",
    var amount: Double = 0.0,
    var contributionDate: String = ""
)