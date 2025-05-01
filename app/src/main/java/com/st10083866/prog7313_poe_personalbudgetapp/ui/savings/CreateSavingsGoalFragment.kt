package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateSavingsGoalBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateSavingsGoalFragment : Fragment() {

    private var _binding: ActivityCreateSavingsGoalBinding? = null
    private val binding get() = _binding!!

    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityCreateSavingsGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }

        binding.btnCreateGoal.setOnClickListener {
            val goalName = binding.edtName.text.toString()
            val targetAmount = binding.edtAmount.text.toString().toDoubleOrNull()
            val deadline = binding.edtDate.text.toString()

            if (goalName.isBlank() || targetAmount == null || deadline.isBlank()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newGoal = SavingsGoal(
                userOwnerId = userId,
                goalName = goalName,
                targetAmount = targetAmount,
                deadline = deadline,
                createdAt = getCurrentDate()
            )

            savingsGoalViewModel.addSavingsGoal(newGoal)

            Toast.makeText(requireContext(), "Goal created!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
