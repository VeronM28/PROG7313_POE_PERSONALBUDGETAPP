package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel

class EditBudgetFragment : Fragment() {

    private lateinit var viewModel: BudgetViewModel

    private lateinit var spinnerEditBudget: Spinner
    private lateinit var spinnerEditBudgetCat: Spinner
    private lateinit var etEditTotalBudget: EditText
    private lateinit var etEditSpendLimit: EditText
    private lateinit var btnEditBudget: MaterialButton
    private lateinit var btnDelete: MaterialButton

    private var selectedBudget: Budget? = null
    private var categoryMap: Map<String, String> = mapOf() // categoryName -> categoryId
    private var budgetMap: Map<String, Budget> = mapOf()   // display string -> Budget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinnerEditBudget = view.findViewById(R.id.spinnerEditBudget)
        spinnerEditBudgetCat = view.findViewById(R.id.spinnerEditBudgetCat)
        etEditTotalBudget = view.findViewById(R.id.etEditTotalBudget)
        etEditSpendLimit = view.findViewById(R.id.etEditSpendLimit)
        btnEditBudget = view.findViewById(R.id.btnEditBudget)
        btnDelete = view.findViewById(R.id.btnDelete)

        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]

        setupCategorySpinner()
        setupBudgetSpinner()

        btnEditBudget.setOnClickListener { updateBudget() }
        btnDelete.setOnClickListener { deleteBudget() }
    }

    private fun setupCategorySpinner() {
        viewModel.allCategories.observe(viewLifecycleOwner) { categories ->
            val categoryNames = categories.map { it.name }
            categoryMap = categories.associateBy({ it.name }, { it.id }) // id is String

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEditBudgetCat.adapter = adapter
        }

    }

    private fun setupBudgetSpinner() {
        viewModel.allBudgets.observe(viewLifecycleOwner) { budgets ->
            val budgetNames = budgets.map { "Budget ${it.id} (R${it.totalAmount})" }
            budgetMap = budgets.associateBy({ "Budget ${it.id} (R${it.totalAmount})" })

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, budgetNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerEditBudget.adapter = adapter

            spinnerEditBudget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedName = parent.getItemAtPosition(position) as String
                    selectedBudget = budgetMap[selectedName]

                    selectedBudget?.let {
                        etEditTotalBudget.setText(it.totalAmount.toString())
                        etEditSpendLimit.setText(it.spendingLimit.toString())
                        it.categoryId?.let { categoryId -> setCategorySpinnerSelection(categoryId) }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedBudget = null
                }
            }
        }
    }

    private fun setCategorySpinnerSelection(categoryId: String) {
        val index = categoryMap.entries.indexOfFirst { it.value == categoryId }
        if (index >= 0) {
            spinnerEditBudgetCat.setSelection(index)
        }
    }

    private fun updateBudget() {
        val budget = selectedBudget ?: return

        val newTotal = etEditTotalBudget.text.toString().toDoubleOrNull()
        val newLimit = etEditSpendLimit.text.toString().toDoubleOrNull()
        val newCategoryName = spinnerEditBudgetCat.selectedItem as? String
        val newCategoryId = categoryMap[newCategoryName]

        if (newTotal == null || newLimit == null || newCategoryId == null) {
            Toast.makeText(requireContext(), "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedBudget = budget.copy(
            totalAmount = newTotal,
            spendingLimit = newLimit,
            categoryId = newCategoryId
        )

        viewModel.insertOrUpdate(updatedBudget)
        Toast.makeText(requireContext(), "Budget updated!", Toast.LENGTH_SHORT).show()
    }

    private fun deleteBudget() {
        val budget = selectedBudget ?: return
        viewModel.delete(budget)
        Toast.makeText(requireContext(), "Budget deleted.", Toast.LENGTH_SHORT).show()
    }
}