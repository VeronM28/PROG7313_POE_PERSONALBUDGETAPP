package com.st10083866.prog7313_poe_personalbudgetapp.data.entities



data class User(
    var userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    val email: String = "",
    val passwordHash: String = "",
    val profilePicturePath: String? = ""
)