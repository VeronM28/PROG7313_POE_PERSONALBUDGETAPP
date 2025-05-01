package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditSpendingBinding
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditSpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSpendingBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var currentTransaction: Transaction
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentTransaction = intent.getSerializableExtra("TRANSACTION") as Transaction
        val userId = 1 // Replace with actual user ID
        setupViews(userId)
        setupClickListeners()
    }

    private fun setupViews(userId: Int) {
        binding.amountEditText.setText(currentTransaction.amount.toString())
        binding.radioIncome.isChecked = currentTransaction.type == "Income"
        binding.radioExpense.isChecked = currentTransaction.type != "Income"
        calendar.timeInMillis = currentTransaction.date
        binding.dateDisplayText.text = dateFormat.format(calendar.time)

        // Payment method spinner
        val paymentMethods = arrayOf("Cash", "Credit Card", "Debit Card", "Bank Transfer")
        val paymentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentMethodSpinner.adapter = paymentAdapter
        binding.paymentMethodSpinner.setSelection(paymentMethods.indexOf(currentTransaction.paymentMethod))

        // Category spinner
        categoryViewModel.getCategories(userId).observe(this, Observer { categories ->
            val categoryNames = categories.map { it.name }
            val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = categoryAdapter
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
        val amount = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val isIncome = binding.radioIncome.isChecked
        val paymentMethod = binding.paymentMethodSpinner.selectedItem.toString()
        val date = calendar.timeInMillis

        val updatedTransaction = currentTransaction.copy(
            amount = amount,
            type = if (isIncome) "Income" else "Expense",
            paymentMethod = paymentMethod,
            date = date
        )

        transactionViewModel.updateTransaction(updatedTransaction)
        finish()
    }

    private fun deleteTransaction() {
        transactionViewModel.deleteTransaction(currentTransaction)
        finish()
    }
}