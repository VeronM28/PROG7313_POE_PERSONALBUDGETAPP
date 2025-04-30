package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution

class ContributionsAdapter(private var items: List<SavingsContribution>) : RecyclerView.Adapter<ContributionsAdapter.ContributionViewHolder>() {

    inner class ContributionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountText: TextView = itemView.findViewById(R.id.contributionAmount)
        val dateText: TextView = itemView.findViewById(R.id.contributionDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contribution, parent, false)
        return ContributionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContributionViewHolder, position: Int) {
        val contribution = items[position]
        holder.amountText.text = "R${contribution.amount}"
        holder.dateText.text = contribution.contributionDate
    }

    override fun getItemCount() = items.size

    fun updateList(newList: List<SavingsContribution>) {
        items = newList
        notifyDataSetChanged()
    }
}