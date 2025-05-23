package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.st10083866.prog7313_poe_personalbudgetapp.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val repository = CategoryRepository()

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> get() = _operationSuccess

    private val _categoryName = MutableLiveData<String?>()
    val categoryName: LiveData<String?> get() = _categoryName

    //  Get categories for a specific user
    fun getCategoriesForUser(userId: String): LiveData<List<Category>> {
        return repository.getCategoriesForUser(userId)
    }

    //  Get all categories
    fun getAllCategories(userId: String): LiveData<List<Category>> {
        return repository.getAllCategories(userId)
    }

    //  Insert or update a category
    fun saveCategory(category: Category) {
        repository.upsertCategory(category) { success ->
            _operationSuccess.postValue(success)
        }
    }

    //  Delete category by ID
    fun deleteCategoryById(categoryId: String) {
        repository.deleteCategory(categoryId) { success ->
            _operationSuccess.postValue(success)
        }
    }

    // 🔎 Get category name for a given ID
    fun loadCategoryName(categoryId: String) {
        repository.getCategoryNameById(categoryId) { name ->
            _categoryName.postValue(name)
        }
    }
}