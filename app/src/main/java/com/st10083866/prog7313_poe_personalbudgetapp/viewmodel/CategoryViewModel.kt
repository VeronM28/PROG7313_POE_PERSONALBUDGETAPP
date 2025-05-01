package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }
    suspend fun getCategoryById(categoryId: Int): LiveData<Category> {
        return categoryDao.getCategoryById(categoryId).asLiveData()
    }

    suspend fun getAllCategoriesForUser(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategoriesForUser(userId)
    }


}