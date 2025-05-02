package com.st10083866.prog7313_poe_personalbudgetapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityBudgetOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel

class BudgetOverviewActivity : AppCompatActivity() {

    private val viewModel: BudgetViewModel by viewModels()
    private lateinit var binding: ActivityBudgetOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budget_overview)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeViewModel()

        val userId = 1 // Replace with actual user ID
        viewModel.loadBudgetOverviewData(userId)
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { categories ->
            if (categories.isNotEmpty()) {
                binding.category = categories[0]
                binding.executePendingBindings()
            }
        }

        viewModel.availableBalance.observe(this) { balance ->
            binding.balanceAmount.text = getString(R.string.currency_format, balance)
        }

        viewModel.budgetPeriod.observe(this) { period ->
            binding.budgetPeriod.text = period
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}