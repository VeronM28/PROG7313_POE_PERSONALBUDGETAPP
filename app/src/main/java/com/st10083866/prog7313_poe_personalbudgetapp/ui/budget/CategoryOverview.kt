package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCategoryOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.BudgetViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.util.Calendar

class CategoryOverviewFragment : Fragment() {

    private lateinit var binding: FragmentCategoryOverviewBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    private var fromDate: Calendar = Calendar.getInstance()
    private var toDate: Calendar = Calendar.getInstance()

    private val colorPalette = listOf(
        Color.parseColor("#F44336"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#8BC34A")
    )

    private var userId: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        setupDatePickers()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchFilteredData() {
        val fromTimestamp = fromDate.timeInMillis
        val toTimestamp = toDate.timeInMillis

        // Use ViewModel's existing method to fetch filtered data
        transactionViewModel.fetchTransactionsBetweenDates(userId, fromTimestamp, toTimestamp)
        Log.d("FilterParams", "userId=$userId, from=$fromTimestamp, to=$toTimestamp")
        transactionViewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            budgetViewModel.allBudgets.observe(viewLifecycleOwner) { budgets ->
                budgetViewModel.allCategories.observe(viewLifecycleOwner) { categories ->
                    Log.d("CategoryOverview", "Transactions: ${transactions.size}")
                    Log.d("CategoryOverview", "Budgets: ${budgets.size}")
                    Log.d("CategoryOverview", "Categories: ${categories.size}")
                    val categorySpendMap: Map<String, Float> = transactions
                        .filter { it.type.equals("expense", ignoreCase = true) }
                        .groupBy { it.categoryId ?: "Uncategorized" }
                        .mapValues { entry -> entry.value.map { txn -> txn.amount.toFloat() }.sum() }

                    val pieSlices = mutableListOf<PieChartView.PieSlice>()
                    val categoryItems = mutableListOf<CategoryAdapter.CategoryItem>()

                    val userBudgets = budgets.filter { it.userOwnerId == userId.toString() }

                    userBudgets.forEachIndexed { index, budget ->
                        val categoryId = budget.categoryId ?: "Uncategorized"
                        val spent = categorySpendMap[categoryId] ?: 0f
                        val limit = budget.spendingLimit.toFloat()
                        val categoryName = categories.find { it.id == categoryId }?.name ?: "Unknown"
                        val color = colorPalette[index % colorPalette.size]

                        pieSlices.add(PieChartView.PieSlice(spent, color))
                        categoryItems.add(
                            CategoryAdapter.CategoryItem(
                                name = categoryName,
                                amountSpent = spent,
                                spendingLimit = limit,
                                color = color
                            )
                        )
                    }

                    binding.pieChart.data = pieSlices
                    binding.recyclerView.adapter = CategoryAdapter(categoryItems)
                }
            }
        }
    }

    private fun setupDatePickers() {
        val fromButton = binding.fromDateButton
        val toButton = binding.toDateButton

        fromButton.setOnClickListener {
            val date = fromDate
            DatePickerDialog(requireContext(), { _, year, month, day ->
                date.set(year, month, day)
                fromButton.text = "From: ${day}/${month + 1}/${year}"
                fetchFilteredData()
            }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show()
        }

        toButton.setOnClickListener {
            val date = toDate
            DatePickerDialog(requireContext(), { _, year, month, day ->
                date.set(year, month, day)
                toButton.text = "To: ${day}/${month + 1}/${year}"
                fetchFilteredData()
            }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Default range -> current month
        fromDate.set(Calendar.DAY_OF_MONTH, 1)
        toDate = Calendar.getInstance()

        fromButton.text = "From: ${fromDate.get(Calendar.DAY_OF_MONTH)}/${fromDate.get(Calendar.MONTH) + 1}/${fromDate.get(Calendar.YEAR)}"
        toButton.text = "To: ${toDate.get(Calendar.DAY_OF_MONTH)}/${toDate.get(Calendar.MONTH) + 1}/${toDate.get(Calendar.YEAR)}"

        fetchFilteredData()
    }
}