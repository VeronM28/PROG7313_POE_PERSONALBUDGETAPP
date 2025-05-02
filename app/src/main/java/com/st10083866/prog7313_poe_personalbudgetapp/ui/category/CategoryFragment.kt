package com.st10083866.prog7313_poe_personalbudgetapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCategoryBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userId = arguments?.getInt("USER_ID", -1) ?: -1
        if (userId == -1){
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnAddCategory.setOnClickListener {
            val name = binding.etCategoryName.text.toString()
            val description = binding.etDesc.text.toString()
            val limit = binding.etLimit.text.toString().toDoubleOrNull()
            val type =
                if (binding.typeToggleGroup.checkedButtonId == R.id.incomeButton) "Income" else "Expense"

            if (name.isBlank() || description.isBlank() || limit == null) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT)
                    .show()
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

            Toast.makeText(requireContext(), "Category added successfully!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}