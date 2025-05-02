package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userOwnerId = :userId")
    fun getBudget(userId: Int): LiveData<List<Budget>>

    // Add to BudgetDao.kt
    @Query("SELECT * FROM budgets WHERE userOwnerId = :userId AND month = :month AND year = :year")
    fun getBudgetForMonth(userId: Int, month: String, year: String): LiveData<Budget>

    @Update
    suspend fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budgets WHERE id = :budgetId")
    fun getBudgetById(budgetId: Int): LiveData<Budget>

    @Delete
    suspend fun delete(budget: Budget)

    @Query("""
        SELECT c.id as categoryId, b.spentAmount 
        FROM budgets b
        JOIN categories c ON b.categoryId = c.id
        WHERE b.userOwnerId = :userId 
        AND b.month = :month 
        AND b.year = :year
    """)
    suspend fun getCategorySpendingMap(userId: Int, month: String, year: String): Map<Int, Double>


}