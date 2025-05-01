// EditBudgetViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.launch

class EditBudgetViewModel(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private lateinit var currentBudget: Budget

    fun getBudget(budgetId: Int): LiveData<Budget> {
        // Note: You'll need to add a new DAO method to get budget by ID
        return budgetDao.getBudgetById(budgetId)
    }

    fun getCategories(userId: Int) = categoryDao.getCategory(userId)

    fun updateBudget(
        budgetId: Int,
        totalBudget: Double,
        spendingLimit: Double,
        categoryId: Int?
    ) {
        viewModelScope.launch {
            val updatedBudget = currentBudget.copy(
                limitAmount = totalBudget,
                spentAmount = spendingLimit,
                categoryId = categoryId
            )
            budgetDao.updateBudget(updatedBudget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.delete(budget)
        }
    }
}