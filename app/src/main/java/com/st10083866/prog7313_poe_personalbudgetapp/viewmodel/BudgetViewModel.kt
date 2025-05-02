package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch
import java.util.*

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val budgetDao = AppDatabase.getDatabase(application).budgetDao()
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    // Budget list LiveData
    private val _budgets = MutableLiveData<List<Budget>>()
    val budgets: LiveData<List<Budget>> get() = _budgets

    // Current budget LiveData
    private val _currentBudget = MutableLiveData<Budget?>()
    val currentBudget: LiveData<Budget?> get() = _currentBudget

    // Categories LiveData
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    // Budget overview data
    private val _availableBalance = MutableLiveData<Double>()
    val availableBalance: LiveData<Double> get() = _availableBalance

    private val _budgetPeriod = MutableLiveData<String>()
    val budgetPeriod: LiveData<String> get() = _budgetPeriod

    private val _availableBalance1 = MutableLiveData<String>()
    val availableBalance1: LiveData<String> get() = _availableBalance1


    // Existing functions
    fun getBudget(userId: Int): LiveData<List<Budget>> {
        return budgetDao.getBudget(userId)
    }

    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.insert(budget)
        }
    }

    fun getBudgetForMonth(userId: Int, month: String, year: String): LiveData<Budget> {
        return budgetDao.getBudgetForMonth(userId, month, year)
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.updateBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            budgetDao.delete(budget)
        }
    }

    fun loadBudgetOverviewData(userId: Int) {
        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        val year = calendar.get(Calendar.YEAR).toString()
        val period = "$month $year"
        _budgetPeriod.postValue(period)


        AppDatabase.getDatabase(getApplication()).categoryDao().getCategory(userId).observeForever {
            _categories.postValue(it)
        }

    fun getCurrentMonthBudget(userId: Int): LiveData<Budget> {
        val calendar = Calendar.getInstance()
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
        val year = calendar.get(Calendar.YEAR).toString()
        return getBudgetForMonth(userId, month, year)
    }
}}