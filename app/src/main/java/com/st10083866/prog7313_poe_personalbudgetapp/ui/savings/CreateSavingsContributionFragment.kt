package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal

import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCreateSavingsContributionBinding
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentCreateSavingsGoalBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsContributionViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

class CreateSavingsContributionFragment : Fragment() {

    private var _binding: FragmentCreateSavingsContributionBinding? = null
    private val binding get() = _binding!!

    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private val savingsContributionViewModel: SavingsContributionViewModel by viewModels()

    private var userId: String = ""
    private var goalsList = listOf<SavingsGoal>()
    private var selectedGoalId: String = ""
    private var selectedDate: Timestamp = Timestamp.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSavingsContributionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId.isNotEmpty()) {
            loadGoalsForUser(userId)
        }

        binding.edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selYear, selMonth, selDay ->
                calendar.set(selYear, selMonth, selDay)
                selectedDate = Timestamp(Date(calendar.timeInMillis))

                // Shows selected date in EditText
                binding.edtDate.setText("$selDay/${selMonth + 1}/$selYear")
            }, year, month, day)

            datePicker.show()
        }

        binding.btnAddContribution.setOnClickListener {
            saveContribution()
        }
    }

    private fun loadGoalsForUser(userId: String) {
        savingsGoalViewModel.getSavingsGoals(userId).observe(viewLifecycleOwner) { goals ->
            if (goals.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No goals found. Create a goal first!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
                return@observe
            }

            goalsList = goals
            val goalNames = goals.map { it.goalName }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, goalNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goalTypeSpinner.adapter = adapter

            binding.goalTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedGoalId = goalsList[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    private fun saveContribution() {
        val amount = binding.edtAmount.text.toString().toDoubleOrNull()

        if (selectedGoalId.isBlank() || amount == null) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contribution = SavingsContribution(
            goalId = selectedGoalId,
            amount = amount,
            contributionDate = selectedDate
        )

        savingsContributionViewModel.addContribution(contribution) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Contribution added!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Failed to add contribution", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
