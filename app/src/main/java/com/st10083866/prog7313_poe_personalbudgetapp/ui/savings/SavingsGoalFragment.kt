package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivitySavingsGoalPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel

class SavingsGoalFragment : Fragment() {

    private var _binding: ActivitySavingsGoalPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavingsGoalViewModel by viewModels()
    private lateinit var adapter: ContributionsAdapter
    private var goalList: List<SavingsGoal> = listOf()
    private var selectedGoalId: String = ""
    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivitySavingsGoalPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Receive userId from arguments (Firestore expects String)
        arguments?.let {
            userId = it.getString("USER_ID", "")
        }

        adapter = ContributionsAdapter(emptyList())
        binding.contributionsRecyclerView.adapter = adapter
        binding.contributionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe savings goals for this user
        viewModel.getSavingsGoals(userId).observe(viewLifecycleOwner) { goals ->
            goalList = goals
            val names = goals.map { it.goalName }

            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goalDropdown.findViewById<Spinner>(R.id.goalTypeSpinner).adapter = spinnerAdapter

            binding.goalDropdown.findViewById<Spinner>(R.id.goalTypeSpinner).onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>, view: View?, position: Int, id: Long
                    ) {
                        val goal = goalList[position]
                        selectedGoalId = goal.id
                        updateProgress(goal)
                        loadContributions(goal.id)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
        }
    }

    private fun updateProgress(goal: SavingsGoal) {
        viewModel.getContributions(goal.id).observe(viewLifecycleOwner) { contributions ->
            val totalSaved = contributions.sumOf { it.amount }
            val progress = if (goal.targetAmount > 0) {
                ((totalSaved / goal.targetAmount) * 100).toInt()
            } else {
                0
            }

            binding.txtAmount.text = "R${totalSaved.toInt()}"
            binding.txtProgress.text = "You've saved $progress% of your goal!"
            binding.progressCircle.setProgressCompat(progress, true)
        }
    }

    private fun loadContributions(goalId: String) {
        viewModel.getContributions(goalId).observe(viewLifecycleOwner) { contributions ->
            adapter.updateList(contributions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
