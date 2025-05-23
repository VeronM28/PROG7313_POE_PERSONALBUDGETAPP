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
import java.util.UUID
import kotlin.getValue

class CreateBudgetFragment : Fragment() {

    private val viewModel: BudgetViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private lateinit var categorySpinner: Spinner
    private var selectedCategoryId: String? = null
    private lateinit var categoryMap: Map<String, String>
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
        val btnAddBudget = view.findViewById<Button>(R.id.addBudgetButton)
        categorySpinner = view.findViewById(R.id.categorySpinner)

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        // Observe categories and populate spinner
        categoryViewModel.getCategoriesByUser(userId.toString()).observe(viewLifecycleOwner) { categories ->
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

        // Observe operation status to give feedback to user
        viewModel.operationStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Budget Added!", Toast.LENGTH_SHORT).show()
                // Optionally clear inputs here
                etTotalBudget.text.clear()
                etSpendLimit.text.clear()
                categorySpinner.setSelection(0)
            } else {
                Toast.makeText(requireContext(), "Failed to add budget. Try again.", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddBudget.setOnClickListener {
            val total = etTotalBudget.text.toString().toDoubleOrNull()
            val limit = etSpendLimit.text.toString().toDoubleOrNull()
            val categoryId = selectedCategoryId

            if (total != null && limit != null && categoryId != null && userId != -1) {
                // Generate an ID in Budget if you want; Firestore repo will handle if empty
                val newBudget = Budget(
                    id = "", // Let repository generate ID if empty
                    totalAmount = total,
                    spendingLimit = limit,
                    userOwnerId = userId.toString(),
                    categoryId = categoryId
                )
                viewModel.insertOrUpdate(newBudget)
            } else {
                Toast.makeText(requireContext(), "Fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}