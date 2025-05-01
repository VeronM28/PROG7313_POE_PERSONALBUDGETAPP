// CreateBudgetActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateBudgetBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.CreateBudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory

class CreateBudgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBudgetBinding
    private val viewModel: CreateBudgetViewModel by viewModels { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get current user ID - in a real app, this would come from your auth system
        val userId = 1 // Replace with actual user ID

        setupViews(userId)
        setupClickListeners(userId)
    }

    private fun setupViews(userId: Int) {
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
        })
    }

    private fun setupClickListeners(userId: Int) {
        binding.addBudgetButton.setOnClickListener {
            createBudget(userId)
        }
    }

    private fun createBudget(userId: Int) {
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
        viewModel.getCategories(userId).value?.let { categories ->
            if (categoryPosition >= 0 && categoryPosition < categories.size) {
                categoryId = categories[categoryPosition].id
            }
        }

        viewModel.createBudget(userId, totalBudget, spendingLimit, categoryId)
        finish()
    }
}