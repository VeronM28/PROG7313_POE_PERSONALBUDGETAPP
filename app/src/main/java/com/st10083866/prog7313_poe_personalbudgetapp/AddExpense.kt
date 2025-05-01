package com.st10083866.prog7313_poe_personalbudgetapp.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityAddExpenseBinding
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = 1 // Replace with actual user ID
        setupViews(userId)
        setupClickListeners()
    }

    private fun setupViews(userId: Int) {
        // Setup payment method spinner
        val paymentMethods = arrayOf("Cash", "Credit Card", "Debit Card", "Bank Transfer")
        val paymentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentMethodSpinner.adapter = paymentAdapter

        // Setup category spinner
        categoryViewModel.getCategories(userId).observe(this, Observer { categories ->
            val categoryNames = categories.map { it.name }
            val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = categoryAdapter
        })

        // Set current date
        binding.dateDisplayText.text = dateFormat.format(calendar.time)
    }

    private fun setupClickListeners() {
        binding.calendarIcon.setOnClickListener {
            showDatePicker()
        }

        binding.addEntryButton.setOnClickListener {
            saveTransaction()
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

    private fun saveTransaction() {
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

        val userId = 1 // Replace with actual user ID

        val transaction = Transaction(
            userOwnerId = userId,
            amount = amount,
            type = if (isIncome) "Income" else "Expense",
            categoryId = null, // You would need to get the actual category ID
            date = date,
            paymentMethod = paymentMethod
        )

        transactionViewModel.addTransaction(transaction)
        finish()
    }
}