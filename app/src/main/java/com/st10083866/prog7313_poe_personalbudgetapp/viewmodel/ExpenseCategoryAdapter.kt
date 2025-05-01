// ExpenseCategoryAdapter.kt
package com.st10083866.prog7313_poe_personalbudgetapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ItemExpenseCategoryBinding

class ExpenseCategoryAdapter : ListAdapter<Category, ExpenseCategoryAdapter.ExpenseCategoryViewHolder>(
    ExpenseCategoryDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCategoryViewHolder {
        val binding = ItemExpenseCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseCategoryViewHolder(private val binding: ItemExpenseCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.categoryName.text = category.name
            // These values would come from your ViewModel calculations
            binding.spentAmount.text = "Spent: R500.00" // Example
            binding.leftAmount.text = "Left: R%.2f".format(category.limit - 500.00) // Example
        }
    }
}

class ExpenseCategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}