package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    fun getAllCategories(): List<Category> {
        return categoryDao.getAllCategories()
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category)
    }
    fun getCategoriesForUser(userId: Int): LiveData<List<Category>> {
        return categoryDao.getCategory(userId)
    }
}