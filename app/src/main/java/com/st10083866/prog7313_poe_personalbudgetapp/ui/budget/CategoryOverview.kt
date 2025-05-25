package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCategoryOverviewBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel

class CategoryOverview : Fragment() {
    private lateinit var binding: FragmentCategoryOverviewBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private var userId: Int = -1

    private val colorPalette = listOf(
        Color.parseColor("#F44336"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#8BC34A")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("USER_ID", -1) ?: -1
        if (userId == -1) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeTransactions(userId.toString()) // Converts Int to String
    }

    private fun observeTransactions(userId: String) {
        transactionViewModel.getAllTransactions(userId).observe(viewLifecycleOwner) { transactions ->
            val grouped = transactions
                .filter { it.type.equals("expense", ignoreCase = true) }
                .groupBy { it.categoryId ?: "Uncategorized" }

            val categoryItems = mutableListOf<CategoryAdapter.CategoryItem>()
            val pieSlices = mutableListOf<PieChartView.PieSlice>()

            if (grouped.isEmpty()) {
                binding.recyclerView.adapter = CategoryAdapter(emptyList())
                binding.pieChart.data = emptyList()
                return@observe
            }

            var processedCount = 0
            grouped.entries.forEachIndexed { index, entry ->
                val total = entry.value.map { it.amount.toFloat() }.sum()
                val color = colorPalette[index % colorPalette.size]
                val categoryId = entry.key

                fetchCategoryName(categoryId) { name ->
                    val label = name ?: "Unknown"
                    categoryItems.add(CategoryAdapter.CategoryItem(label, total, color))
                    pieSlices.add(PieChartView.PieSlice(total, color))

                    processedCount++
                    if (processedCount == grouped.size) {
                        binding.recyclerView.adapter = CategoryAdapter(categoryItems)
                        binding.pieChart.data = pieSlices
                    }
                }
            }
        }
    }

    private fun fetchCategoryName(categoryId: String, callback: (String?) -> Unit) {
        db.collection("categories").document(categoryId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    callback(snapshot.getString("name"))
                } else {
                    callback("Unknown")
                }
            }
            .addOnFailureListener {
                callback("Unknown")
            }
    }
}