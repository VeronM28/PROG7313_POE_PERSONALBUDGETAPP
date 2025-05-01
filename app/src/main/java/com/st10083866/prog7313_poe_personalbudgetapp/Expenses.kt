package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityExpensesBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.*

class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = 1 // Replace with actual user ID
        setupCurrentMonth()
        observeViewModels(userId)
    }

    private fun setupCurrentMonth() {
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
        binding.totalSpent.text = currentMonth
    }

    private fun observeViewModels(userId: Int) {
        budgetViewModel.getCurrentMonthBudget(userId).observe(this, Observer { budget ->
            budget?.let {
                binding.monthlyBudget.text = "R %.2f".format(it.limitAmount)
                val leftToSpend = it.limitAmount - it.spentAmount
                binding.leftToSpend.text = "R %.2f".format(leftToSpend)
            }
        })

        categoryViewModel.getExpenseCategories(userId).observe(this, Observer { categories ->
            // Directly populate categories
            binding.categoriesContainer.removeAllViews()
            categories.forEach { category ->

            }
        })
    }
}