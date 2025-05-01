package com.st10083866.prog7313_poe_personalbudgetapp.ui.category

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var spinnerCategories: Spinner
    private lateinit var etEditCategoryName: EditText
    private lateinit var etEditDescription: EditText
    private lateinit var etLimit: EditText
    private lateinit var incomeButton: View
    private lateinit var expenseButton: View
    private lateinit var typeToggleGroup: MaterialButtonToggleGroup

    private val categoryViewModel: CategoryViewModel by viewModels()
    private var categoryList = listOf<Category>()
    private var selectedCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_category)

        spinnerCategories = findViewById(R.id.spinnerCategories)
        etEditCategoryName = findViewById(R.id.etEditCategoryName)
        etEditDescription = findViewById(R.id.etEditDescription)
        etLimit = findViewById(R.id.etEditLimit)
        typeToggleGroup = findViewById(R.id.typeToggleGroup)
        incomeButton = findViewById(R.id.incomeButton)
        expenseButton = findViewById(R.id.expenseButton)

        val btnEdit = findViewById<Button>(R.id.btnEditCategory)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        categoryViewModel.allCategories.observe(this) { categories ->
            categoryList = categories
            val names = categories.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategories.adapter = adapter
        }
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categoryList[position]
                selectedCategory?.let { cat ->
                    etEditCategoryName.setText(cat.name)
                    etEditDescription.setText(cat.description)
                    etLimit.setText(cat.limit.toString())
                    if (cat.spendType == "Income") {
                        typeToggleGroup.check(R.id.incomeButton)
                    } else {
                        typeToggleGroup.check(R.id.expenseButton)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        btnEdit.setOnClickListener {
            selectedCategory?.let { cat ->
                val updatedCategory = cat.copy(
                    name = etEditCategoryName.text.toString().ifBlank { cat.name },
                    description = etEditDescription.text.toString().ifBlank { cat.description },
                    limit = etLimit.text.toString().toDoubleOrNull() ?: cat.limit,
                    spendType = if (typeToggleGroup.checkedButtonId == R.id.incomeButton) "Income" else "Expense"
                )
                categoryViewModel.updateCategory(updatedCategory)
                Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        btnDelete.setOnClickListener {
            selectedCategory?.let {
                categoryViewModel.deleteCategory(it)
                Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}