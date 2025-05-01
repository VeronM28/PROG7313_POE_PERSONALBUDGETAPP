// ExpensesViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.launch

class ExpensesViewModel(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    fun getCurrentBudget(userId: Int): LiveData<Budget> {
        // This would need a new query to get the current month's budget
        return budgetDao.getBudget(userId).map { budgets ->
            budgets.firstOrNull {
                it.month == getCurrentMonth() && it.year == getCurrentYear()
            } ?: Budget()
        }
    }

    fun getExpenseCategories(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategory(userId).map { categories ->
            categories.filter { !it.isIncome }
        }
    }

    private fun getCurrentMonth(): String {
        return java.text.SimpleDateFormat("MMMM", java.util.Locale.getDefault())
            .format(java.util.Date())
    }

    private fun getCurrentYear(): String {
        return java.text.SimpleDateFormat("yyyy", java.util.Locale.getDefault())
            .format(java.util.Date())
    }
}