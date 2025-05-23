package com.st10083866.prog7313_poe_personalbudgetapp.data.entities



data class SavingsGoal(
    var id: String = "",
    var userOwnerId: String = "",
    var goalName: String = "",
    var targetAmount: Double = 0.0,
    var deadline: String = "",
    var createdAt: String = ""
)