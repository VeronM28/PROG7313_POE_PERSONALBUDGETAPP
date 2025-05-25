package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import com.st10083866.prog7313_poe_personalbudgetapp.repository.SavingsContributionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavingsContributionViewModel : ViewModel() {

    private val repository = SavingsContributionRepository()


    fun addContribution(contribution: SavingsContribution, onResult: (Boolean) -> Unit) {
        repository.insert(contribution, onResult)
    }


    fun getContributionsForGoal(goalId: String): LiveData<List<SavingsContribution>> {
        return repository.getContributions(goalId)
    }
}