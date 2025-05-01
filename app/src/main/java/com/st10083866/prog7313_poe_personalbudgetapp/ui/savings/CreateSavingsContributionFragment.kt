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
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateSavingsContributionBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel

class CreateSavingsContributionFragment : Fragment() {

    private var _binding: ActivityCreateSavingsContributionBinding? = null
    private val binding get() = _binding!!

    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private var userId: Int = -1
    private var goalsList = listOf<SavingsGoal>()
    private var selectedGoalId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityCreateSavingsContributionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }

        if (userId != -1) {
            loadGoalsForUser(userId)
        }

        binding.btnAddContribution.setOnClickListener {
            saveContribution()
        }
    }

    private fun loadGoalsForUser(userId: Int) {
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

            binding.goalTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
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

        if (selectedGoalId == -1 || amount == null || date.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contribution = SavingsContribution(
            goalId = selectedGoalId,
            amount = amount,
            contributionDate = date
        )

        savingsGoalViewModel.addContribution(contribution)

        Toast.makeText(requireContext(), "Contribution added!", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
