package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import com.google.firebase.Timestamp


data class SavingsGoal(
    var id: String = "",
    var userOwnerId: String = "",
    var goalName: String = "",
    var targetAmount: Double = 0.0,
    var deadline: Timestamp = Timestamp.now(),
    var createdAt: Timestamp = Timestamp.now()
)