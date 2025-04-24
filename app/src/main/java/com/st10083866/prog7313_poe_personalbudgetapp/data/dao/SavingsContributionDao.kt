package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution

@Dao
interface SavingsContributionDao {
    @Insert
    suspend fun insert(contribution: SavingsContribution)

    @Query("SELECT * FROM savings_contributions WHERE goalId = :goalId ORDER BY contributionDate DESC")
    fun getContributions(goalId: Int): LiveData<List<SavingsContribution>>
}