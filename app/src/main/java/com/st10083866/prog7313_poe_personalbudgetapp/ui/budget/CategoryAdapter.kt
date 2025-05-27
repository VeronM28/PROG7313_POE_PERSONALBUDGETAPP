package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.R

class CategoryAdapter(private val items: List<CategoryItem>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    data class CategoryItem(
        val name: String,
        val amountSpent: Float,
        val spendingLimit: Float,
        val color: Int
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dot = view.findViewById<View>(R.id.categoryColorDot)
        val name = view.findViewById<TextView>(R.id.categoryName)
        val spentLimit = view.findViewById<TextView>(R.id.categorySpentLimit)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_total, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.spentLimit.text = "R${item.amountSpent.toInt()} / R${item.spendingLimit.toInt()}"

        // Apply's color to the indicator dot
        DrawableCompat.setTint(holder.dot.background, item.color)

        // Calculates percentage and set progress
        val percent = if (item.spendingLimit > 0f)
            ((item.amountSpent / item.spendingLimit) * 100).toInt().coerceAtMost(100)
        else 0

        holder.progressBar.progress = percent


        val progressColor = if (item.amountSpent > item.spendingLimit) {
            Color.RED
        } else {
            item.color
        }
        DrawableCompat.setTint(holder.progressBar.progressDrawable, progressColor)
    }
}