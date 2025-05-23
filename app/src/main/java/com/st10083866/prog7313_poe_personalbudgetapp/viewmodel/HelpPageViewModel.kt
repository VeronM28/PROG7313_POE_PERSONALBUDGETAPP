package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.HelpPageContent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.st10083866.prog7313_poe_personalbudgetapp.repository.HelpPageContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HelpPageViewModel(private val repository: HelpPageContentRepository) : ViewModel() {

    private val _helpContents = MutableStateFlow<List<HelpPageContent>>(emptyList())
    val helpContents: StateFlow<List<HelpPageContent>> = _helpContents.asStateFlow()

    fun insertHelpContent(content: HelpPageContent) = viewModelScope.launch {
        repository.insert(content)
    }

    fun loadHelpContents() = viewModelScope.launch {
        repository.getHelpContent().collect {
            _helpContents.value = it
        }
    }
}