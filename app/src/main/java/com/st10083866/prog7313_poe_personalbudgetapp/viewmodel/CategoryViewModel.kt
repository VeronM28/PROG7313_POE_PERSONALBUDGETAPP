package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.repository.CategoryRepository
import kotlinx.coroutines.launch

// ViewModel for handling UI-related data for categories in a lifecycle-conscious way
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    // Reference to the database and DAO
    private val db = AppDatabase.getDatabase(application)
    private val categoryDao = db.categoryDao()
    // Repository to abstract data operations
    private val repository: CategoryRepository
    // LiveData to observe list of all categories
    val allCategories: LiveData<List<Category>>
    // Initialization block
    init {
        // Initialize the repository with the DAO
        val categoryDao = AppDatabase.getDatabase(application).categoryDao()
        // Retrieve all categories through the repository
        repository = CategoryRepository(categoryDao)
        allCategories = repository.allCategories
    }
    // Adds a category directly through the DAO (alternative to using repository)
    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }
    // Returns a list of categories for a specific user as LiveData
    fun getCategoriesForUser(userId: Int): LiveData<List<Category>> {
         return  categoryDao.getCategoriesForUser(userId)
    }
    // Inserts a new category using the repository
    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }
    // Updates an existing category using the repository
    fun updateCategory(category: Category) = viewModelScope.launch {
        repository.updateCategory(category)
    }
    // Deletes a category using the repository
    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }




}