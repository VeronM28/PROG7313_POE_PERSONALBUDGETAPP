// CreateBudgetViewModel.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.launch
import java.util.*

class CreateBudgetViewModel(
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    fun getCategories(userId: Int) = categoryDao.getCategory(userId)

    fun createBudget(
        userId: Int,
        totalBudget: Double,
        spendingLimit: Double,
        categoryId: Int?
    ) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
            val year = calendar.get(Calendar.YEAR).toString()

            val budget = Budget(
                userOwnerId = userId,
                month = month,
                year = year,
                categoryId = categoryId,
                limitAmount = totalBudget,
                spentAmount = 0.0 // Starting with 0 spent
            )

            budgetDao.insert(budget)
        }
    }
}