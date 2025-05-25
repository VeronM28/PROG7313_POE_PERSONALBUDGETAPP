package com.st10083866.prog7313_poe_personalbudgetapp.ui.budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.R

class CategoryAdapter(private val items: List<CategoryItem>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    data class CategoryItem(val name: String, val amount: Float, val color: Int)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dot = view.findViewById<View>(R.id.categoryColorDot)
        val name = view.findViewById<TextView>(R.id.categoryName)
        val amount = view.findViewById<TextView>(R.id.categoryAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_total, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.amount.text = "R${item.amount}"
        DrawableCompat.setTint(holder.dot.background, item.color)
    }
}