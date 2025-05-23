package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.repository.BudgetRepository
import kotlinx.coroutines.launch


class BudgetViewModel : ViewModel() {

    private val repository = BudgetRepository()

    // Expose budgets LiveData from repository
    val allBudgets: LiveData<List<Budget>> = repository.getAllBudgets()

    private val _operationStatus = MutableLiveData<Boolean>()
    val operationStatus: LiveData<Boolean> get() = _operationStatus

    fun insertOrUpdate(budget: Budget) {
        repository.insertOrUpdateBudget(budget) { success ->
            _operationStatus.postValue(success)
        }
    }

    fun delete(budget: Budget) {
        repository.deleteBudget(budget) { success ->
            _operationStatus.postValue(success)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearListener()  // avoid memory leaks
    }
}
