package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userOwnerId = :userId")
    suspend fun getBudget(userId: Int): List<Budget>
}