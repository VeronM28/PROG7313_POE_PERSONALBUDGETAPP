// EditSpendingActivity.kt
package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditSpendingBinding
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.EditSpendingViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodels.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class EditSpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSpendingBinding
    private val viewModel: EditSpendingViewModel by viewModels { ViewModelFactory(this) }

    private lateinit var currentTransaction: Transaction
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get transaction from intent
        currentTransaction = intent.getSerializableExtra("TRANSACTION") as Transaction

        // Get current user ID - in a real app, this would come from your auth system
        val userId = 1 // Replace with actual user ID

        setupViews(userId)
        setupClickListeners()
    }

    private fun setupViews(userId: Int) {
        // Set current transaction data
        binding.amountEditText.setText(currentTransaction.amount.toString())
        binding.radioIncome.isChecked = currentTransaction.type == "Income"
        binding.radioExpense.isChecked = currentTransaction.type != "Income"

        // Set date
        calendar.timeInMillis = currentTransaction.date
        binding.dateDisplayText.text = dateFormat.format(calendar.time)

        // Setup payment method spinner
        val paymentMethods = arrayOf("Cash", "Credit Card", "Debit Card", "Bank Transfer")
        val paymentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentMethodSpinner.adapter = paymentAdapter

        // Select current payment method
        val paymentPosition = paymentMethods.indexOf(currentTransaction.paymentMethod)
        if (paymentPosition >= 0) {
            binding.paymentMethodSpinner.setSelection(paymentPosition)
        }

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
            currentTransaction.categoryId?.let { categoryId ->
                val position = categories.indexOfFirst { it.id == categoryId }
                if (position >= 0) {
                    binding.categorySpinner.setSelection(position)
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.calendarIcon.setOnClickListener {
            showDatePicker()
        }

        binding.editEntryButton.setOnClickListener {
            updateTransaction()
        }

        binding.deleteButton.setOnClickListener {
            deleteTransaction()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                binding.dateDisplayText.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateTransaction() {
        val amountText = binding.amountEditText.text.toString()
        if (amountText.isEmpty()) {
            binding.amountEditText.error = "Please enter an amount"
            return
        }

        val amount = amountText.toDouble()
        val category = binding.categorySpinner.selectedItem.toString()
        val paymentMethod = binding.paymentMethodSpinner.selectedItem.toString()
        val isIncome = binding.radioIncome.isChecked
        val date = calendar.timeInMillis

        // Get category ID
        var categoryId: Int? = null
        viewModel.getCategories(1).value?.let { categories ->
            val position = binding.categorySpinner.selectedItemPosition
            if (position >= 0 && position < categories.size) {
                categoryId = categories[position].id
            }
        }

        val updatedTransaction = currentTransaction.copy(
            amount = amount,
            type = if (isIncome) "Income" else "Expense",
            categoryId = categoryId,
            date = date,
            paymentMethod = paymentMethod
        )

        viewModel.updateTransaction(updatedTransaction)
        finish()
    }

    private fun deleteTransaction() {
        viewModel.deleteTransaction(currentTransaction)
        finish()
    }
}