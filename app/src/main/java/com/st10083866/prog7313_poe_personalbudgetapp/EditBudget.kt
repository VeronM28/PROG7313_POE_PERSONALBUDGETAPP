package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditBudgetBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class EditBudgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBudgetBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var currentBudgetId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentBudgetId = intent.getIntExtra("BUDGET_ID", -1)
        if (currentBudgetId == -1) finish()

        val userId = 1 // Replace with actual user ID
        setupViews(userId)
        setupClickListeners()
    }

    private fun setupViews(userId: Int) {
        budgetViewModel.getBudgetById(currentBudgetId).observe(this, Observer { budget ->
            budget?.let {
                binding.totalBudgetInput.setText(it.limitAmount.toString())
                binding.spendingLimitInput.setText(it.spentAmount.toString())
            }
        })

        categoryViewModel.getCategories(userId).observe(this, Observer { categories ->
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
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
        val totalBudget = binding.totalBudgetInput.text.toString().toDoubleOrNull() ?: 0.0
        val spendingLimit = binding.spendingLimitInput.text.toString().toDoubleOrNull() ?: 0.0
        val categoryPosition = binding.categorySpinner.selectedItemPosition

        var categoryId: Int? = null
        categoryViewModel.getCategories(1).value?.let { categories ->
            if (categoryPosition in categories.indices) {
                categoryId = categories[categoryPosition].id
            }
        }

        budgetViewModel.updateBudget(currentBudgetId, totalBudget, spendingLimit, categoryId)
        finish()
    }

    private fun deleteBudget() {
        budgetViewModel.getBudgetById(currentBudgetId).value?.let {
            budgetViewModel.deleteBudget(it)
        }
        finish()
    }
}