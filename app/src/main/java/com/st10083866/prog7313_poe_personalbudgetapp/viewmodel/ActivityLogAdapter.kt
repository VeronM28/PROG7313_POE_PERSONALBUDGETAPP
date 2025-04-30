package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityLogAdapter : RecyclerView.Adapter<ActivityLogAdapter.ViewHolder>(){

    private var items = listOf<Transaction>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.transactionIcon)
        val date: TextView = view.findViewById(R.id.transactionDate)
        val method: TextView = view.findViewById(R.id.transactionType)
        val amount: TextView = view.findViewById(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx = items[position]

        holder.date.text = formatDate(tx.date)
        holder.amount.text = "R${String.format("%,.2f", tx.amount)}"
        holder.method.text = tx.paymentMethod

        val icon = if (tx.type.lowercase() == "income") R.drawable.baseline_arrow_circle_up_24 else R.drawable.ic_dropdown
        val color = if (tx.type.lowercase() == "income") "#1ad417" else "#ed0c35"
        holder.icon.setImageResource(icon)

        holder.icon.setColorFilter(Color.parseColor(color))
    }

    fun submitList(list: List<Transaction>){
        items = list
        notifyDataSetChanged()
    }

    private fun formatDate(timestamp: Long): String {
        val format = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return format.format(Date(timestamp))
    }
}