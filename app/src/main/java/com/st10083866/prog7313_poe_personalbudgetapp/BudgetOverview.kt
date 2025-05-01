package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityBudgetOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.util.*

class BudgetOverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetOverviewBinding
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = 1 // Replace with actual user ID
        observeViewModels(userId)
    }

    private fun observeViewModels(userId: Int) {
        budgetViewModel.getBudget(userId).observe(this, Observer { budgets ->
            budgets.firstOrNull()?.let {
                binding.budgetPeriod.text = "${it.month} ${it.year}"
            }
        })

        categoryViewModel.getCategories(userId).observe(this, Observer { categories ->
            // Directly populate categories layout
            binding.categoriesContainer.removeAllViews()
            categories.forEach { category ->
                // Inflate and add category views manually
                // (You would create this layout in your XML)
                // val categoryView = layoutInflater.inflate(R.layout.item_category, binding.categoriesContainer, false)
                // categoryView.categoryName.text = category.name
                // binding.categoriesContainer.addView(categoryView)
            }
        })

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val fromDate = calendar.timeInMillis
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val toDate = calendar.timeInMillis

        transactionViewModel.getTotalExpensesBetweenDates(userId, fromDate, toDate)
            .observe(this, Observer { total ->
                binding.balanceAmount.text = "R %.2f".format(total)
            })
    }
}