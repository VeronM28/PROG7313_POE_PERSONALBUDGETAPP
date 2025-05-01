package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDao = AppDatabase.getDatabase(application).transactionDao()

    // Existing functions
    fun getAllTransactions(userId: Int): LiveData<List<Transaction>> {
        return transactionDao.getAllTransactions(userId)
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    // New functions
    fun getTransactionsForUserBetweenDates(userId: Int, fromDate: Long, toDate: Long): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsForUserBetweenDates(userId, fromDate, toDate)
    }

    fun getTotalExpensesBetweenDates(userId: Int, fromDate: Long, toDate: Long): LiveData<Double> {
        return transactionDao.getTotalExpensesBetweenDates(userId, fromDate, toDate)
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.update(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }
}