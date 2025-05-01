package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch
import java.util.*

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    // Existing functions
    fun getBudget(userId: Int): LiveData<List<Budget>> {
        return budgetDao.getBudget(userId)
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
        }
    }

    // New functions
    fun getBudgetForMonth(userId: Int, month: String, year: String): LiveData<Budget> {
        return budgetDao.getBudgetForMonth(userId, month, year)
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.updateBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.delete(budget)
        }
    }

    fun getCurrentMonthBudget(userId: Int): LiveData<Budget> {
        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        val year = calendar.get(Calendar.YEAR).toString()
        return getBudgetForMonth(userId, month, year)
    }
}