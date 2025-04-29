package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivitySavingsGoalPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel

class SavingsGoalPage : AppCompatActivity() {

    private lateinit var adapter: ContributionsAdapter
    private lateinit var binding: ActivitySavingsGoalPageBinding
    private val viewModel: SavingsGoalViewModel by viewModels()
    private var goalList: List<SavingsGoal> = listOf()
    private var selectedGoalId = -1
    private var userId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_savings_goal_page)

        userId = intent.getIntExtra("USER_ID", -1)

        adapter = ContributionsAdapter(emptyList())
        binding.contributionsRecyclerView.adapter = adapter
        binding.contributionsRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getSavingsGoals(userId).observe(this) { goals ->
            goalList = goals
            val names = goals.map { it.goalName }

            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goalDropdown.findViewById<Spinner>(R.id.goalTypeSpinner).adapter =
                spinnerAdapter

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
        viewModel.getContributions(goal.id).observe(this) { contributions ->
            val totalSaved = contributions.sumOf { it.amount }
            val progress = ((totalSaved / goal.targetAmount) * 100).toInt()

            binding.txtAmount.text = "R${totalSaved.toInt()}"
            binding.txtProgress.text = "You've saved $progress% of your goal!"
            binding.progressCircle.setProgressCompat(progress, true)
        }
    }
    private fun loadContributions(goalId: Int) {
        viewModel.getContributions(goalId).observe(this) { contributions ->
            adapter.updateList(contributions)
        }
    }
}
