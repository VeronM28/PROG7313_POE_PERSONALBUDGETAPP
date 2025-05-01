package com.st10083866.prog7313_poe_personalbudgetapp.ui.category

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditCategoryBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var spinnerCategories: Spinner
    private lateinit var etEditCategoryName: EditText
    private lateinit var etEditDescription: EditText
    private lateinit var etEditLimit: EditText
    private lateinit var typeToggleGroup: com.google.android.material.button.MaterialButtonToggleGroup
    private lateinit var btnEditCategory: Button
    private lateinit var btnDelete: Button

    private var selectedCategory: Category? = null
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_category)

        // Get category ID from intent
        currentCategoryId = intent.getIntExtra("CATEGORY_ID", -1)
        if (currentCategoryId == -1) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Observe the category to prefill fields
        categoryViewModel.getCategoryById(currentCategoryId).observe(this) { category ->
            if (category != null) {
                binding.etCategoryName.setText(category.name)
                binding.etDesc.setText(category.description)
                binding.etLimit.setText(category.limit.toString())

                // Set toggle state
                if (category.spendType) {
                    binding.typeToggleGroup.check(R.id.incomeButton)
                } else {
                    binding.typeToggleGroup.check(R.id.expenseButton)
                }

                // Save updated category
                binding.btnAddCategory.text = "Save Changes"
                binding.btnAddCategory.setOnClickListener {
                    val updatedName = binding.etCategoryName.text.toString()
                    val updatedDesc = binding.etDesc.text.toString()
                    val updatedLimit = binding.etLimit.text.toString().toDoubleOrNull()

                    val updatedSpendType = when (binding.typeToggleGroup.checkedButtonId) {
                        R.id.incomeButton -> true
                        R.id.expenseButton -> false
                        else -> category.spendType
                    }

                    if (updatedName.isBlank() || updatedDesc.isBlank() || updatedLimit == null) {
                        Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val updatedCategory = category.copy(
                        name = updatedName,
                        description = updatedDesc,
                        limit = updatedLimit,
                        spendType = updatedSpendType
                    )

                    categoryViewModel.updateCategory(updatedCategory)
                    Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show()
                    finish()
                }

                // Optional: Add a separate delete button
                binding.btnDeleteCategory?.setOnClickListener {
                    categoryViewModel.deleteCategory(category)
                    Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }
}