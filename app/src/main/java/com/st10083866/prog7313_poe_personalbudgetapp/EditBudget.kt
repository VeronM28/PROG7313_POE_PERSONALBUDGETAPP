// EditBudgetActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditBudgetBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.EditBudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory

class EditBudgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBudgetBinding
    private val viewModel: EditBudgetViewModel by viewModels { ViewModelFactory(this) }

    private var currentBudgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get budget ID from intent
        currentBudgetId = intent.getIntExtra("BUDGET_ID", -1)
        if (currentBudgetId == -1) {
            finish()
            return
        }

        // Get current user ID - in a real app, this would come from your auth system
        val userId = 1 // Replace with actual user ID

        setupViews(userId)
        setupClickListeners()
    }

    private fun setupViews(userId: Int) {
        // Observe budget data
        viewModel.getBudget(currentBudgetId).observe(this, Observer { budget ->
            budget?.let {
                binding.totalBudgetInput.setText(it.limitAmount.toString())
                binding.spendingLimitInput.setText(it.spentAmount.toString())
            }
        })

        // Setup category spinner
        viewModel.getCategories(userId).observe(this, Observer { categories ->
            val categoryNames = categories.map { it.name }
            val categoryAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
            )
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = categoryAdapter

            // Select current category if exists
            viewModel.getBudget(currentBudgetId).value?.let { budget ->
                budget.categoryId?.let { categoryId ->
                    val position = categories.indexOfFirst { it.id == categoryId }
                    if (position >= 0) {
                        binding.categorySpinner.setSelection(position)
                    }
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            updateBudget()
        }

        binding.deleteButton.setOnClickListener {
            deleteBudget()
        }
    }

    private fun updateBudget() {
        val totalBudgetText = binding.totalBudgetInput.text.toString()
        val spendingLimitText = binding.spendingLimitInput.text.toString()

        if (totalBudgetText.isEmpty()) {
            binding.totalBudgetInput.error = "Please enter total budget"
            return
        }

        if (spendingLimitText.isEmpty()) {
            binding.spendingLimitInput.error = "Please enter spending limit"
            return
        }

        val totalBudget = totalBudgetText.toDouble()
        val spendingLimit = spendingLimitText.toDouble()
        val categoryPosition = binding.categorySpinner.selectedItemPosition

        // Get category ID if a category is selected
        var categoryId: Int? = null
        viewModel.getCategories(1).value?.let { categories ->
            if (categoryPosition >= 0 && categoryPosition < categories.size) {
                categoryId = categories[categoryPosition].id
            }
        }

        viewModel.updateBudget(currentBudgetId, totalBudget, spendingLimit, categoryId)
        finish()
    }

    private fun deleteBudget() {
        viewModel.getBudget(currentBudgetId).value?.let {
            viewModel.deleteBudget(it)
        }
        finish()
    }
}