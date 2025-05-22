package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

data class Category(
    var id: String = "",              // Firestore document ID (can be set or used from snapshot)
    var userOwnerId: String = "",     // Reference to the owning user
    var name: String = "",
    var description: String = "",
    var limit: Double = 0.0,
    var spendType: String = ""
)