package com.st10083866.prog7313_poe_personalbudgetapp.data.entities



data class Category(
    var id: String = "",              // Firestore document ID (can be set or used from snapshot)
    var userOwnerId: String = "",     // Reference to the owning user
    var name: String = "",
    var description: String = "",
    var limit: Double = 0.0,
    var spendType: String = ""
)