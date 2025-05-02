package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category

class BudgetRepository(private val budgetDao: BudgetDao, private val categoryDao: CategoryDao) {

    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()
}