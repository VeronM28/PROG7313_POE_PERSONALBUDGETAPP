package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

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

class CreateSavingsContributionFragment : Fragment() {

    private var _binding: FragmentCreateSavingsContributionBinding? = null
    private val binding get() = _binding!!

    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private val savingsContributionViewModel: SavingsContributionViewModel by viewModels()

    private var userId: String = ""
    private var goalsList = listOf<SavingsGoal>()
    private var selectedGoalId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSavingsContributionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getString("USER_ID", "")
        }

        if (userId.isNotEmpty()) {
            loadGoalsForUser(userId)
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
        val date = binding.edtDate.text.toString()

        if (selectedGoalId.isBlank() || amount == null || date.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contribution = SavingsContribution(
            goalId = selectedGoalId,
            amount = amount,
            contributionDate = date
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
