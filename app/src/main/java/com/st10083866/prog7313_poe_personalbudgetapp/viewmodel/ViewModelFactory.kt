// ViewModelFactory.kt
package com.st10083866.prog7313_poe_personalbudgetapp.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)

        return when {
            modelClass.isAssignableFrom(BudgetOverviewViewModel::class.java) -> {
                BudgetOverviewViewModel(
                    database.budgetDao(),
                    database.categoryDao(),
                    database.transactionDao()
                ) as T
            }
            modelClass.isAssignableFrom(ExpensesViewModel::class.java) -> {
                ExpensesViewModel(
                    database.budgetDao(),
                    database.categoryDao()
                ) as T
            }
            modelClass.isAssignableFrom(AddExpenseViewModel::class.java) -> {
                AddExpenseViewModel(
                    database.transactionDao(),
                    database.categoryDao()
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}