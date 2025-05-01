package com.st10083866.prog7313_poe_personalbudgetapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityLogAdapter(private val onEditClick: (Transaction) -> Unit, private val onDownloadClick: (Transaction) -> Unit) : RecyclerView.Adapter<ActivityLogAdapter.ViewHolder>(){

    private var items = listOf<Transaction>()

    //a holder for each item
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.transactionIcon)
        val date: TextView = view.findViewById(R.id.transactionDate)
        val method: TextView = view.findViewById(R.id.transactionType)
        val amount: TextView = view.findViewById(R.id.transactionAmount)
        val editButton: TextView = view.findViewById(R.id.btnEditEntry)
        val downloadButton: TextView = view.findViewById(R.id.btnViewImage)
    }

    //this function creates the view for a single item in the logs list and inflates the item_activity_log layout.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity_log, parent, false)
        return ViewHolder(view)
    }

    //tells the recycler view how many items to display
    override fun getItemCount() = items.size

    //this function is called for each transaction and fills in the appropriate data while also connecting button clicks for Edit and Download
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx = items[position]

        holder.date.text = formatDate(tx.date)
        holder.amount.text = "R${String.format("%,.2f", tx.amount)}"
        holder.method.text = tx.paymentMethod

        val icon = if (tx.type.lowercase() == "income") R.drawable.baseline_arrow_circle_up_24 else R.drawable.ic_dropdown
        val color = if (tx.type.lowercase() == "income") "#1ad417" else "#ed0c35"
        holder.icon.setImageResource(icon)

        holder.icon.setColorFilter(Color.parseColor(color))

        holder.editButton.setOnClickListener {
            onEditClick(tx)
        }

        holder.downloadButton.setOnClickListener {
            onDownloadClick(tx)
        }
    }
     //this function fetches all the transactions between two dates and replaces the current list of transactions as well as notifies the adapter to refresh the screen
    fun submitList(list: List<Transaction>){
        items = list
        notifyDataSetChanged()
    }

    //this function converts a timestamp into a readable date
    private fun formatDate(timestamp: Long): String {
        val format = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        return format.format(Date(timestamp))
    }
}