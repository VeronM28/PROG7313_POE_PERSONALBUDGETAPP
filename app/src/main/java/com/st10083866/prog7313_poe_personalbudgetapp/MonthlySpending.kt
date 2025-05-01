package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityMonthlySpendingBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.util.*

class MonthlySpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthlySpendingBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlySpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user ID from shared preferences or authentication
        val userId = getAuthenticatedUserId()
        setupMonthlySpending(userId)
    }

    private fun getAuthenticatedUserId(): Int {
        // Implement actual user authentication logic
        // This is just a placeholder
        return 1
    }

    private fun setupMonthlySpending(userId: Int) {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Get start and end of month timestamps
        val (fromDate, toDate) = getMonthStartEndTimestamps(currentMonth, currentYear)

        // Observe total spending
        transactionViewModel.getTotalExpensesBetweenDates(userId, fromDate, toDate)
            .observe(this) { total ->
                binding.textTotalSpending.text = "R %.2f".format(total)
            }

        // Observe budget data
        budgetViewModel.getCurrentMonthBudget(userId).observe(this) { budget ->
            budget?.let {
                binding.textMonthlyBudget.text = "R %.2f".format(it.limitAmount)
                val leftToSpend = it.limitAmount - it.spentAmount
                binding.textLeftToSpend.text = "R %.2f".format(leftToSpend)
            }
        }
    }

    private fun getMonthStartEndTimestamps(month: Int, year: Int): Pair<Long, Long> {
        val startCalendar = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0)
        }
        val start = startCalendar.timeInMillis

        val endCalendar = Calendar.getInstance().apply {
            set(year, month, 1, 23, 59, 59)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        val end = endCalendar.timeInMillis

        return Pair(start, end)
    }
}