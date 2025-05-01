package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    // Existing functions
    fun getCategories(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategory(userId)
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }

    // New functions
    fun getExpenseCategories(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategory(userId).map { categories ->
            categories.filter { category -> !category.isIncome }
        }
    }

    fun getIncomeCategories(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategory(userId).map { categories ->
            categories.filter { category -> category.isIncome }
        }
    }
}