package com.st10083866.prog7313_poe_personalbudgetapp

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.ActivityLogAdapter
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentLogPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.transactions.EditSpendingFragment
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import java.text.SimpleDateFormat
import java.util.*

class ActivityLogFragment : Fragment() {

    private var _binding: FragmentLogPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var adapter: ActivityLogAdapter

    private var fromDate: String = ""
    private var toDate: String = ""
    private var userId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogPageBinding.inflate(inflater, container, false)

        return binding.root
    }
    //this function initializes the screen layout and logic for how the screen elements behave
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments?.getString("USER_ID") ?: ""

        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }
        //displays all transaction logs
        adapter = ActivityLogAdapter(
            categoryViewModel,
            onEditClick = { transaction ->
                val fragment = EditSpendingFragment().apply {
                    arguments = Bundle().apply {
                        putString("TRANSACTION_ID", transaction.id)
                        putString("USER_ID", userId)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDownloadClick = { transaction ->
                if (!transaction.uploadedPicturePath.isNullOrEmpty()) {
                    downloadImageFromUrl(transaction.uploadedPicturePath!!)
                } else {
                    Toast.makeText(requireContext(), "No image uploaded for this transaction", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.activityLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.activityLogRecyclerView.adapter = adapter
        //opens date pickers
        binding.edtStartDate.setOnClickListener {
            showDatePicker { date ->
                fromDate = date
                binding.edtStartDate.setText(formatDisplayDate(date))
            }
        }

        binding.edtEndDate.setOnClickListener {
            showDatePicker { date ->
                toDate = date
                binding.edtEndDate.setText(formatDisplayDate(date))
            }
        }
        //updates logs when button is pressed
        binding.btnUpdateLogs.setOnClickListener {
            if (fromDate.isNotEmpty() && toDate.isNotEmpty()) {
                val fromMillis = parseDateToMillis(fromDate)
                val toMillis = parseDateToMillis(toDate)

                viewModel.fetchTransactionsBetweenDates(userId, fromMillis, toMillis)
                viewModel.transactions.observe(viewLifecycleOwner) { logs ->
                    adapter.submitList(logs)
                }
            } else {
                Toast.makeText(requireContext(), "Please select both dates!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    //this function display the date picker for the user screen
    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val dpd = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                val formatted = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(formatted)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }
    //this function takes the formatted date and converts it to a Unix timestamp
    private fun parseDateToMillis(dateStr: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(dateStr)?.time ?: 0L
    }
    //this function takes the formatted date and turns it into a more readable date (eg: 2025-05-04 -> 4 May 2025)
    private fun formatDisplayDate(isoDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(isoDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            isoDate
        }
    }
    //this function will use the user's system's download manager to save the image
    private fun downloadImageFromUrl(imageUrl: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle("Transaction Image")
            .setDescription("Downloading attached image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "transaction_image_${System.currentTimeMillis()}.jpg")

        val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
