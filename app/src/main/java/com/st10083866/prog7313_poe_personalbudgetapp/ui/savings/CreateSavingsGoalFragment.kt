package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import android.app.DatePickerDialog
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCreateSavingsGoalBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel
import java.util.Date
import java.util.Locale

class CreateSavingsGoalFragment : Fragment() {

    private var _binding: FragmentCreateSavingsGoalBinding? = null
    private val binding get() = _binding!!

    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private var userId: String = ""
    private var selectedDeadline: Timestamp = Timestamp.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSavingsGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        binding.edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selYear, selMonth, selDay ->
                calendar.set(selYear, selMonth, selDay)
                selectedDeadline = Timestamp(Date(calendar.timeInMillis))
                binding.edtDate.setText("$selDay/${selMonth + 1}/$selYear")
            }, year, month, day)

            datePicker.show()
        }

        binding.btnCreateGoal.setOnClickListener {
            val goalName = binding.edtName.text.toString()
            val targetAmount = binding.edtAmount.text.toString().toDoubleOrNull()
            val deadlineText = binding.edtDate.text.toString()

            if (goalName.isBlank() || targetAmount == null || deadlineText.isBlank()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newGoal = SavingsGoal(
                userOwnerId = userId,
                goalName = goalName,
                targetAmount = targetAmount,
                deadline = selectedDeadline,
                createdAt = Timestamp.now()
            )

            savingsGoalViewModel.insertSavingsGoal(newGoal) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Goal created!", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to create goal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
