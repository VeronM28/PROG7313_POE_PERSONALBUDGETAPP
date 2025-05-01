package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateBudgetBinding
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import java.util.*

class CreateBudgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBudgetBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = 1 // Replace with actual user ID
        setupViews(userId)
        setupClickListeners(userId)
    }

    private fun setupViews(userId: Int) {
        categoryViewModel.getCategories(userId).observe(this, Observer { categories ->
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
        })
    }

    private fun setupClickListeners(userId: Int) {
        binding.addBudgetButton.setOnClickListener {
            val totalBudget = binding.totalBudgetInput.text.toString().toDoubleOrNull() ?: 0.0
            val spendingLimit = binding.spendingLimitInput.text.toString().toDoubleOrNull() ?: 0.0
            val categoryPosition = binding.categorySpinner.selectedItemPosition

            var categoryId: Int? = null
            categoryViewModel.getCategories(userId).value?.let { categories ->
                if (categoryPosition >= 0 && categoryPosition < categories.size) {
                    categoryId = categories[categoryPosition].id
                }
            }

            val calendar = Calendar.getInstance()
            val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
            val year = calendar.get(Calendar.YEAR).toString()

            val budget = Budget(
                userOwnerId = userId,
                month = month,
                year = year,
                categoryId = categoryId,
                limitAmount = totalBudget,
                spentAmount = 0.0
            )

            budgetViewModel.addBudget(budget)
            finish()
        }
    }
}