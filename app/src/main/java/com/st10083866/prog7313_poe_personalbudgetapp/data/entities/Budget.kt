package com.st10083866.prog7313_poe_personalbudgetapp.data.entities



data class Budget(
    var id: String = "",
    var userOwnerId: String = "",
    var totalAmount: Double = 0.0,
    var spendingLimit: Double = 0.0,
    var categoryId: String? = ""
)
