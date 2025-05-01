package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE userOwnerId = :userId ORDER BY date DESC")
    fun getAllTransactions(userId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE userOwnerId = :userId AND date BETWEEN :fromDate AND :toDate ORDER BY date DESC")
    fun getTransactionsForUserBetweenDates(userId: Int, fromDate: Long, toDate: Long): LiveData<List<Transaction>>

    // Add to TransactionDao.kt
    @Query("SELECT SUM(amount) FROM transactions WHERE userOwnerId = :userId AND type = 'Expense' AND date BETWEEN :fromDate AND :toDate")
    fun getTotalExpensesBetweenDates(userId: Int, fromDate: Long, toDate: Long): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE userOwnerId = :userId AND categoryId = :categoryId ORDER BY date DESC")
    fun getTransactionsByCategory(userId: Int, categoryId: Int): LiveData<List<Transaction>>

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}