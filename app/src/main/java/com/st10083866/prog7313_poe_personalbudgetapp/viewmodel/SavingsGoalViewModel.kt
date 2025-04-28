package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class SavingsGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val savingsGoalDao = AppDatabase.getDatabase(application).savingsGoalDao()
    private val savingsContributionDao = AppDatabase.getDatabase(application).savingsContributionDao()

    //this function retrives all the savingsGoals
    fun getSavingsGoals(userId: Int): LiveData<List<SavingsGoal>> {
        return savingsGoalDao.getGoals(userId)
    }

    //this function adds a saving Goal
    fun addSavingsGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            savingsGoalDao.insert(goal)
        }
    }


    //this function adds a contribution
    fun addContribution(contribution: SavingsContribution) {
        viewModelScope.launch {
            savingsContributionDao.insert(contribution)
        }
    }

    //this function gets all the contributions from a specific saving goal
    fun getContributions(goalId: Int): LiveData<List<SavingsContribution>> {
        return savingsContributionDao.getContributions(goalId)
    }



}