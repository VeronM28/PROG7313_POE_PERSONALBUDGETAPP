package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val categoryDao = db.categoryDao()

    fun addCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }


}