package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :id")
    suspend fun getUserbyId(id: Int): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?
}