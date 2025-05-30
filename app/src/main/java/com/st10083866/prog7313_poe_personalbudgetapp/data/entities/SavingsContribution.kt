package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import com.google.firebase.Timestamp


data class SavingsContribution(
    var id: String = "",
    var goalId: String = "",
    var amount: Double = 0.0,
    var contributionDate: Timestamp = Timestamp.now()
)