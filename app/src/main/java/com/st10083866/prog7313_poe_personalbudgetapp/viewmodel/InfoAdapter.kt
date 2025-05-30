package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.R

class InfoAdapter(private var items: List<InfoItem>) :
    RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.infoTitle)
        val description: TextView = view.findViewById(R.id.infoDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.description.text = item.description
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<InfoItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}