// BudgetOverviewActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10083866.prog7313_poe_personalbudgetapp.adapters.CategoryAdapter
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityBudgetOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.BudgetOverviewViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory

class BudgetOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetOverviewBinding
    private val viewModel: BudgetOverviewViewModel by viewModels { ViewModelFactory(this) }
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID - in a real app, this would come from your auth system
        val userId = 1 // Replace with actual user ID

        setupRecyclerView()
        observeViewModel(userId)
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter()
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BudgetOverviewActivity)
            adapter = categoryAdapter
        }
    }

    private fun observeViewModel(userId: Int) {
        viewModel.getBudgets(userId).observe(this, Observer { budgets ->
            // Update UI with budget data
            budgets.firstOrNull()?.let {
                binding.budgetPeriod.text = "${it.month} ${it.year}"
            }
        })

        viewModel.getCategories(userId).observe(this, Observer { categories ->
            categoryAdapter.submitList(categories)
        })

        // Get current month start and end timestamps
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
        val fromDate = calendar.timeInMillis
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
        val toDate = calendar.timeInMillis

        viewModel.calculateTotalSpent(userId, fromDate, toDate).observe(this, Observer { total ->
            binding.balanceAmount.text = "R %.2f".format(total)
        })
    }
}