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

    //this function returns LiveData of the Transaction (in real time for the UI)
    fun getAllTransactions(userId: Int): LiveData<List<Transaction>> {
        return transactionDao.getAllTransactions(userId)
    }

    //this function adds a new transaction to the database
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    //this function gets the transactions between specific dates specified by the user
    fun getTransactionsBetweenDates(userId: Int, from: Long, to: Long): LiveData<List<Transaction>> {
        return transactionDao.getTransactionsForUserBetweenDates(userId, from, to)
    }
}