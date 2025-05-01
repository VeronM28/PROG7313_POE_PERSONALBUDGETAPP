package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val categoryDao = db.categoryDao()
    private val repository: CategoryRepository
    val allCategories: LiveData<List<Category>>

    init {
        val categoryDao = AppDatabase.getDatabase(application).categoryDao()
        repository = CategoryRepository(categoryDao)
        allCategories = repository.allCategories
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }
    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }
    fun updateCategory(category: Category) = viewModelScope.launch {
        repository.updateCategory(category)
    }
    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }


}