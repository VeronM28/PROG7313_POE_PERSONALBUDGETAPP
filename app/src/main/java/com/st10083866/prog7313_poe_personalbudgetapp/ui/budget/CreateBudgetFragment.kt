package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import kotlin.getValue

class CreateBudgetFragment : Fragment() {

    private lateinit var viewModel: BudgetViewModel
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var categorySpinner: Spinner
    private var selectedCategoryId: Int? = null
    private lateinit var categoryMap: Map<String, Int>
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val etTotalBudget = view.findViewById<EditText>(R.id.etTotalBudget)
        val etSpendLimit = view.findViewById<EditText>(R.id.etSpendLimit)
        val btnaddBudgetButton = view.findViewById<Button>(R.id.addBudgetButton)
        userId = arguments?.getInt("USER_ID", -1) ?: -1
        categorySpinner = view.findViewById(R.id.categorySpinner)

        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        categoryViewModel.allCategories(userId).observe(viewLifecycleOwner) { categories ->
            val categoryNames = categories.map { it.name }
            categoryMap = categories.associateBy({ it.name }, { it.id })
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedName = parent.getItemAtPosition(position) as String
                selectedCategoryId = categoryMap[selectedName]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCategoryId = null
            }
        }

        btnaddBudgetButton.setOnClickListener {
            val total = etTotalBudget.text.toString().toDoubleOrNull()
            val limit = etSpendLimit.text.toString().toDoubleOrNull()
            val categoryId = selectedCategoryId

            if (total != null && limit != null && categoryId != null) {
                val newBudget = Budget(totalAmount = total, spendingLimit = limit, userOwnerId = id, categoryId = categoryId)
                viewModel.insertBudget(newBudget)
                Toast.makeText(requireContext(), "Budget Added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}