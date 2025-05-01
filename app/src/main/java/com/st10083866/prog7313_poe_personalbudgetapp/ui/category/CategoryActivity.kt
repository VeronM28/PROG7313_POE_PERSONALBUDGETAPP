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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "Invalid user. Cannot save category.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.btnAddCategory.setOnClickListener {

            val name = binding.etCategoryName.text.toString()
            val description = binding.etDesc.text.toString()
            val limitText = binding.etLimit.text.toString()
            val selectedSpendType = binding.typeToggleGroup.checkedButtonId

            val spendType = when (selectedSpendType) {
                R.id.incomeButton -> true  // income
                R.id.expenseButton -> false // expense
                else -> {
                    Toast.makeText(this, "Please select income or expense.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if (name.isBlank() || description.isBlank() || limitText.isBlank() || spendType) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val catLimit = limitText.toDoubleOrNull()
            if (catLimit == null) {
                Toast.makeText(this, "Limit must be a number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = Category(
                userOwnerId = userId,
                name = name,
                description = description,
                limit = catLimit,
                spendType = spendType
            )

            categoryViewModel.insertCategory(category)

            Toast.makeText(this, "Category saved!", Toast.LENGTH_SHORT).show()
            finish() // go back to previous screen
        }
    }
}

