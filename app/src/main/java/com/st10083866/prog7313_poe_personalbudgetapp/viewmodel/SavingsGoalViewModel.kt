package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.repository.SavingsContributionRepository

import com.st10083866.prog7313_poe_personalbudgetapp.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavingsGoalViewModel : ViewModel() {

    private val repository = SavingsGoalRepository()
    private val contributionRepository = SavingsContributionRepository()

    // Insert a savings goal
    fun insertSavingsGoal(goal: SavingsGoal, onResult: (Boolean) -> Unit) {
        repository.insert(goal, onResult)
    }

    // Get savings goals by user ID
    fun getSavingsGoals(userId: String): LiveData<List<SavingsGoal>> {
        return repository.getGoals(userId)
    }
    fun getContributions(goalId: String): LiveData<List<SavingsContribution>> {
        return contributionRepository.getContributions(goalId)
    }

    // Clean up Firestore listeners
    override fun onCleared() {
        super.onCleared()
        repository.removeGoalsListener()
    }
}