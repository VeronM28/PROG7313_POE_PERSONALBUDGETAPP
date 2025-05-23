package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import com.st10083866.prog7313_poe_personalbudgetapp.repository.SavingsContributionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavingsContributionViewModel(private val repository: SavingsContributionRepository) : ViewModel() {

    private val _contributions = MutableStateFlow<List<SavingsContribution>>(emptyList())
    val contributions: StateFlow<List<SavingsContribution>> = _contributions.asStateFlow()

    fun insertContribution(contribution: SavingsContribution) = viewModelScope.launch {
        repository.insert(contribution)
    }

    fun loadContributions(goalId: String) = viewModelScope.launch {
        repository.getContributions(goalId).collect {
            _contributions.value = it
        }
    }
}