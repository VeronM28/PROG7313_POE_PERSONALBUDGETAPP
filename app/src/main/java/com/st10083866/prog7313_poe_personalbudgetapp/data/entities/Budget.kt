package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

data class Budget(
    var id: String = "",
    var userOwnerId: String = "",
    var totalAmount: Double = 0.0,
    var spendingLimit: Double = 0.0,
    var categoryId: String? = null
)
