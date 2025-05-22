package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.google.firebase.Timestamp
import java.util.Date

data class Transaction(
    var id: String = "",
    var userOwnerId: String = "",
    var amount: Double = 0.0,
    var type: String = "",
    var categoryId: String? = null,
    var date: Timestamp = Timestamp.now(),
    var paymentMethod: String = "",
    var uploadedAt: Long = System.currentTimeMillis(),
    var uploadedPicturePath: String? = null
)