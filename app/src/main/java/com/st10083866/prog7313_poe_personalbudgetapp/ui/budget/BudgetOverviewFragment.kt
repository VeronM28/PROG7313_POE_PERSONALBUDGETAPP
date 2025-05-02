package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentBudgetOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.R
import androidx.fragment.app.viewModels

class BudgetOverviewFragment : Fragment() {

    private lateinit var binding: FragmentBudgetOverviewBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBudgetOverviewBinding.inflate(inflater, container, false)
        userId = arguments?.getInt("USER_ID") ?: -1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (userId != -1) {
            displayBudgetSummary(userId)
        }
    }

    private fun displayBudgetSummary(userId: Int) {
        categoryViewModel.getCategoriesForUser(userId).observe(viewLifecycleOwner) { categories ->
            transactionViewModel.getAllTransactions(userId).observe(viewLifecycleOwner) { transactions ->
                val totalBalance = transactions.sumOf {
                    if (it.type == "income") it.amount else -it.amount
                }
                binding.txtBalance.text = "R %.2f".format(totalBalance)

                binding.categoryContainer.removeAllViews()

                val categorySums = mutableMapOf<Int, Double>()

                // Calculates spending per category
                transactions.filter { it.type == "expense" && it.categoryId != null }
                    .forEach {
                        categorySums[it.categoryId!!] = categorySums.getOrDefault(it.categoryId!!, 0.0) + it.amount
                    }

                val totalExpenses = categorySums.values.sum()

                // Displays category
                categories.forEach { category ->
                    val amount = categorySums[category.id] ?: 0.0
                    val percent = if (totalExpenses > 0) (amount / totalExpenses) * 100 else 0.0

                    val view = layoutInflater.inflate(R.layout.item_category_summary, binding.categoryContainer, false)
                    view.findViewById<TextView>(R.id.txtCategoryName).text = category.name
                    view.findViewById<TextView>(R.id.txtAmount).text = "R %.2f".format(amount)
                    view.findViewById<TextView>(R.id.txtPercent).text = "%.0f%%".format(percent)
                    binding.categoryContainer.addView(view)
                }
            }
        }
    }
}
