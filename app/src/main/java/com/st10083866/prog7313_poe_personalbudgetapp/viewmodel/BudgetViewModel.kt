package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()

    //this function fetches all the budgets
    fun getBudget(userId: Int): LiveData<List<Budget>> {
            return budgetDao.getBudget(userId)
    }

    //this function adds a new budget
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
        }
    }
}