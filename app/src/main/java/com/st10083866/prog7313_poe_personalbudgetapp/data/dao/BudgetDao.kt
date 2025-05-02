package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)
}