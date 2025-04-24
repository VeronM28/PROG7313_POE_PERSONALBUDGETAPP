package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal

@Dao
interface SavingsGoalDao {
    @Insert
    suspend fun insert(goal: SavingsGoal)

    @Query("SELECT * FROM savings_goals WHERE userOwnerId = :userid")
    fun getGoals(userId: Int): LiveData<List<SavingsGoal>>
}