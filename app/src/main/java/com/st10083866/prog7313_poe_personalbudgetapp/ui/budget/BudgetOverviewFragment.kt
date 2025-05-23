package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentBudgetOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

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
        categoryViewModel.getCategoriesForUser(userId).observe(viewLifecycleOwner, Observer { categories ->
            transactionViewModel.getAllTransactions(userId).observe(viewLifecycleOwner, Observer { transactions ->

                val totalBalance = transactions.sumOf {
                    if (it.type == "income") it.amount else -it.amount
                }
                binding.txtTotal.text = "R %.2f".format(totalBalance)

                binding.contributionsRecyclerView.removeAllViews()

                val categorySums = mutableMapOf<String, Double>() // categoryId -> total spent

                // Calculate total spending per category
                transactions.filter { it.type == "expense" && it.categoryId != null }
                    .forEach {
                        val catId = it.categoryId!!
                        categorySums[catId] = categorySums.getOrDefault(catId, 0.0) + it.amount
                    }

                val totalExpenses = categorySums.values.sum()

                // Display each category's contribution
                categories.forEach { category ->
                    val amount = categorySums[category.id] ?: 0.0
                    val percent = if (totalExpenses > 0) (amount / totalExpenses) * 100 else 0.0

                    val catView = layoutInflater.inflate(R.layout.item_category_summary, binding.contributionsRecyclerView, false)
                    catView.findViewById<TextView>(R.id.txtCategoryName).text = category.name
                    catView.findViewById<TextView>(R.id.txtAmount).text = "R %.2f".format(amount)
                    catView.findViewById<TextView>(R.id.txtPercent).text = "%.0f%%".format(percent)
                    binding.contributionsRecyclerView.addView(catView)
                }
            })
        })
    }
}

