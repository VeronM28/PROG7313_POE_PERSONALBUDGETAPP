package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.st10083866.prog7313_poe_personalbudgetapp.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavingsGoalViewModel(private val repository: SavingsGoalRepository) : ViewModel() {

    private val _goals = MutableStateFlow<List<SavingsGoal>>(emptyList())
    val goals: StateFlow<List<SavingsGoal>> = _goals.asStateFlow()

    fun insertGoal(goal: SavingsGoal) = viewModelScope.launch {
        repository.insert(goal)
    }

    fun loadGoals(userId: String) = viewModelScope.launch {
        repository.getGoals(userId).collect {
            _goals.value = it
        }
    }
}