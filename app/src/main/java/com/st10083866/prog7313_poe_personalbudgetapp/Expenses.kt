// ExpensesActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10083866.prog7313_poe_personalbudgetapp.adapters.ExpenseCategoryAdapter
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityExpensesBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ExpensesViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class ExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesBinding
    private val viewModel: ExpensesViewModel by viewModels { ViewModelFactory(this) }
    private lateinit var adapter: ExpenseCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID - in a real app, this would come from your auth system
        val userId = 1 // Replace with actual user ID

        setupRecyclerView()
        observeViewModel(userId)
    }

    private fun setupRecyclerView() {
        adapter = ExpenseCategoryAdapter()
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ExpensesActivity)
            adapter = this@ExpensesActivity.adapter
        }
    }

    private fun observeViewModel(userId: Int) {
        // Set current month
        val currentMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
        binding.totalSpent.text = currentMonth

        viewModel.getCurrentBudget(userId).observe(this, Observer { budget ->
            binding.monthlyBudget.text = "R %.2f".format(budget.limitAmount)
            val leftToSpend = budget.limitAmount - budget.spentAmount
            binding.leftToSpend.text = "R %.2f".format(leftToSpend)
        })

        viewModel.getExpenseCategories(userId).observe(this, Observer { categories ->
            adapter.submitList(categories)
        })
    }
}