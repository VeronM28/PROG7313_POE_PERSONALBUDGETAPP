// EditSpendingViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.TransactionDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import kotlinx.coroutines.launch

class EditSpendingViewModel(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    fun getCategories(userId: Int) = categoryDao.getCategory(userId)

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