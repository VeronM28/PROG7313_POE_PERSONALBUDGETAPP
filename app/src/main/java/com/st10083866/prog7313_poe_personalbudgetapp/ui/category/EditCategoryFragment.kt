package com.st10083866.prog7313_poe_personalbudgetapp.ui.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentEditCategoryBinding

class EditCategoryFragment : Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var categoryList = listOf<Category>()
    private var selectedCategory: Category? = null
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments?.getInt("USER_ID", -1) ?: -1
        if (userId == -1) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        // Observe categories from Firestore
        categoryViewModel.getCategoriesForUser(userId).observe(viewLifecycleOwner) { categories ->
            categoryList = categories
            val names = categories.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategories.adapter = adapter
        }

        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categoryList[position]
                selectedCategory?.let { cat ->
                    binding.etEditCategoryName.setText(cat.name)
                    binding.etEditDescription.setText(cat.description)
                    binding.etEditLimit.setText(cat.limit.toString())
                    if (cat.spendType == "Income") {
                        binding.typeToggleGroup.check(R.id.incomeButton)
                    } else {
                        binding.typeToggleGroup.check(R.id.expenseButton)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnEditCategory.setOnClickListener {
            selectedCategory?.let { cat ->
                val updatedCategory = cat.copy(
                    name = binding.etEditCategoryName.text.toString().ifBlank { cat.name },
                    description = binding.etEditDescription.text.toString().ifBlank { cat.description },
                    limit = binding.etEditLimit.text.toString().toDoubleOrNull() ?: cat.limit,
                    spendType = if (binding.typeToggleGroup.checkedButtonId == R.id.incomeButton) "Income" else "Expense"
                )
                categoryViewModel.saveCategory(updatedCategory)
                Toast.makeText(requireContext(), "Category updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            selectedCategory?.id?.let { id ->
                categoryViewModel.deleteCategoryById(id)
                Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

