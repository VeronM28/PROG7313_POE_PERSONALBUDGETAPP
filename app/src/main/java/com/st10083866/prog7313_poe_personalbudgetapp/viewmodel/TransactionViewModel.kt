package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.st10083866.prog7313_poe_personalbudgetapp.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val repository = TransactionRepository()

    // LiveData holding the current list of transactions for a user
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    // LiveData holding a single transaction (by id)
    private val _transaction = MutableLiveData<Transaction?>()
    val transaction: LiveData<Transaction?> get() = _transaction

    // Fetch all transactions for a user and update LiveData
    fun getAllTransactions(userId: String): LiveData<List<Transaction>> {
        return repository.getAllTransactions(userId)
    }


    // Fetch transactions for a user between dates and update LiveData
    fun fetchTransactionsBetweenDates(userId: String, fromDate: Long, toDate: Long) {
        repository.getTransactionsForUserBetweenDates(userId, fromDate, toDate).observeForever {
            _transactions.postValue(it)
        }
    }

    // Fetch a single transaction by ID and update LiveData
    fun fetchTransactionById(id: String) {
        repository.getTransactionById(id).observeForever {
            _transaction.postValue(it)
        }
    }

    // Insert a new transaction
    fun insertTransaction(transaction: Transaction, callback: (Boolean) -> Unit) {
        repository.insert(transaction, callback)
    }

    // Update an existing transaction
    fun updateTransaction(transaction: Transaction, callback: (Boolean) -> Unit) {
        repository.updateTransaction(transaction, callback)
    }

    // Delete a transaction by ID
    fun deleteTransactionById(id: String, callback: (Boolean) -> Unit) {
        repository.deleteTransactionById(id, callback)
    }
}