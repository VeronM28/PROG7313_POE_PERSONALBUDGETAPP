package com.st10083866.prog7313_poe_personalbudgetapp.ui.category

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var etCategoryName : EditText
    private lateinit var etDesc : EditText
    private lateinit var etLimit : EditText
    private lateinit var typeToggleGroup: MaterialButtonToggleGroup
    private lateinit var btnAddCategory: MaterialButton

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        etCategoryName = findViewById(R.id.etCategoryName)
        etDesc = findViewById(R.id.etDesc)
        etLimit = findViewById(R.id.etLimit)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        typeToggleGroup = findViewById(R.id.typeToggleGroup)

        typeToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.incomeButton -> {
                        // Set category type to INCOME
                    }
                    R.id.expenseButton -> {
                        // Set category type to EXPENSE
                    }
                }
            }
        }
        btnAddCategory.setOnClickListener {
            val name = etCategoryName.text.toString().trim()
            val description = etDesc.text.toString().trim()
            val limitText = etLimit.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || limitText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val limit = limitText.toDoubleOrNull()
            if (limit == null) {
                Toast.makeText(this, "Invalid limit amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isIncome = typeToggleGroup.checkedButtonId == R.id.incomeButton
            val categoryType = if (isIncome) "Income" else "Expense"

            // Replace with actual user ID from session/auth
            val userId = 1

            val newCategory = Category(
                userOwnerId = userId,
                name = name,
                description = description,
                limit = limit,
                isIncome = isIncome

            )

            categoryViewModel.addCategory(newCategory)

            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}