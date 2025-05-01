// BudgetOverviewViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.TransactionDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import java.util.*

class BudgetOverviewViewModel(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : ViewModel() {

    fun getBudgets(userId: Int): LiveData<List<Budget>> = budgetDao.getBudget(userId)

    fun getCategories(userId: Int): LiveData<List<Category>> = categoryDao.getCategory(userId)

    fun calculateTotalSpent(userId: Int, fromDate: Long, toDate: Long): LiveData<Double> {
        return transactionDao.getTransactionsForUserBetweenDates(userId, fromDate, toDate)
            .map { transactions ->
                transactions.sumOf { it.amount }
            }
    }
}