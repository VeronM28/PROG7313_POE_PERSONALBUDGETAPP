// MonthlySpendingActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityMonthlySpendingBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.MonthlySpendingViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory
import java.util.*

class MonthlySpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlySpendingBinding
    private val viewModel: MonthlySpendingViewModel by viewModels { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlySpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID from your authentication system
        val userId = getCurrentUserId()
        observeViewModel(userId)
    }

    private fun observeViewModel(userId: Int) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        viewModel.getMonthlySpending(userId, currentMonth, currentYear).observe(this) { total ->
            binding.totalSpendingTextView.text = "R %.2f".format(total)
        }

        viewModel.getCurrentMonthBudget(userId).observe(this) { budget ->
            budget?.let {
                binding.monthlyBudgetTextView.text = "R %.2f".format(it.limitAmount)
                val leftToSpend = it.limitAmount - it.spentAmount
                binding.leftToSpendTextView.text = "R %.2f".format(leftToSpend)
            }
        }
    }

    private fun getCurrentUserId(): Int {
        // Implement your actual user ID retrieval logic
        return 1 // Replace with real implementation
    }
}