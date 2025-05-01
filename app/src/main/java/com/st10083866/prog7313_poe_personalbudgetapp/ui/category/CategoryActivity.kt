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
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCategoryBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var userId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_category)

        userId = intent.getIntExtra("USER_ID", -1)

        binding.btnAddCategory.setOnClickListener {
            val name = binding.etCategoryName.text.toString()
            val description = binding.etDesc.text.toString()
            val limit = binding.etLimit.text.toString().toDoubleOrNull()
            val type = if (binding.typeToggleGroup.checkedButtonId == R.id.incomeButton) "Income" else "Expense"

            if (name.isBlank() || description.isBlank() || limit == null) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newCategory = Category(
                userOwnerId = userId,
                name = name,
                description = description,
                limit = limit,
                spendType = type
            )

            categoryViewModel.addCategory(newCategory)

            Toast.makeText(this, "Category added successfully!", Toast.LENGTH_SHORT).show()
            finish() // Go back or move to the next screen
        }


    }
}

