package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.HelpPageContent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class HelpPageViewModel(application: Application) : AndroidViewModel(application){
    private val helpPageContentDao = AppDatabase.getDatabase(application).helpPageContentDao()

    fun getHelpContent(): LiveData<List<HelpPageContent>> {
    return helpPageContentDao.getHelpContent()
    }
}