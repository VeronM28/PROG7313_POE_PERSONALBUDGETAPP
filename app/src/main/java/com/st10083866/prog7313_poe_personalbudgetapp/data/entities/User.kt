package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int=0,
    val username: String,
    val fname: String,
    val sname: String,
    val email: String,
    val passwordHash: String,
    val profilePicturePath: String? = null
)