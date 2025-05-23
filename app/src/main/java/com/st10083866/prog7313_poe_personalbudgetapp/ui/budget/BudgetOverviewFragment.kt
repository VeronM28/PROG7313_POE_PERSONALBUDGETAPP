package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBudgetOverviewBinding.inflate(inflater, container, false)
        userId = arguments?.getString("USER_ID") ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (userId.isNotEmpty()) {
            displayBudgetSummary(userId)
        }
    }

    private fun displayBudgetSummary(userId: String) {
        categoryViewModel.getCategoriesForUser(userId).observe(viewLifecycleOwner) { categories ->
            transactionViewModel.getAllTransactions(userId).observe(viewLifecycleOwner) { transactions ->

                // Calculate total balance (income - expenses)
                val totalBalance = transactions.sumOf {
                    if (it.type == "income") it.amount else -it.amount
                }
                binding.txtTotal.text = "R %.2f".format(totalBalance)

                // Clear any existing views
                binding.contributionsRecyclerView.removeAllViews()

                // Map to track total expenses per category
                val categorySums = mutableMapOf<String, Double>()

                transactions.filter { it.type == "expense" && it.categoryId != null }
                    .forEach { txn ->
                        val catId = txn.categoryId!!
                        categorySums[catId] = categorySums.getOrDefault(catId, 0.0) + txn.amount
                    }

                val totalExpenses = categorySums.values.sum()

                categories.forEach { category ->
                    val amount = categorySums[category.id] ?: 0.0
                    val percent = if (totalExpenses > 0) (amount / totalExpenses) * 100 else 0.0

                    val itemView = layoutInflater.inflate(R.layout.item_category_summary, binding.contributionsRecyclerView, false)
                    itemView.findViewById<TextView>(R.id.txtCategoryName).text = category.name
                    itemView.findViewById<TextView>(R.id.txtAmount).text = "R %.2f".format(amount)
                    itemView.findViewById<TextView>(R.id.txtPercent).text = "%.0f%%".format(percent)

                    binding.contributionsRecyclerView.addView(itemView)
                }
            }
        }
    }
}
