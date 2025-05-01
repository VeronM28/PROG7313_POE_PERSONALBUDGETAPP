package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}