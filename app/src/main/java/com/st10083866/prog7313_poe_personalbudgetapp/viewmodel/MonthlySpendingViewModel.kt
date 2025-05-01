// MonthlySpendingViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.TransactionDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import kotlinx.coroutines.launch
import java.util.*

class MonthlySpendingViewModel(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : ViewModel() {

    fun getMonthlyBudgets(userId: Int): LiveData<List<Budget>> {
        return budgetDao.getBudget(userId)
    }

    fun getMonthlySpending(userId: Int, month: Int, year: Int): LiveData<Double> {
        // Get start and end of month timestamps
        val calendar = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0)
        }
        val fromDate = calendar.timeInMillis
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val toDate = calendar.timeInMillis

        return transactionDao.getTotalExpensesBetweenDates(userId, fromDate, toDate)
    }

    fun getCurrentMonthBudget(userId: Int): LiveData<Budget> {
        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        val year = calendar.get(Calendar.YEAR).toString()

        return budgetDao.getBudgetForMonth(userId, month, year)
    }
}