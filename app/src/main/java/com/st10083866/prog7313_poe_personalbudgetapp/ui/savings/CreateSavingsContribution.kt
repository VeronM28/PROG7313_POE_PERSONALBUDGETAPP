package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateSavingsContributionBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel

class CreateSavingsContribution : AppCompatActivity() {

    private lateinit var binding: ActivityCreateSavingsContributionBinding
    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private var userId: Int = -1
    private var goalsList = listOf<SavingsGoal>()
    private var selectedGoalId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_savings_contribution)

        if (userId != -1){
            loadGoalsForUser(userId)
        }

        binding.btnAddContribution.setOnClickListener {
            saveContribution()
        }
    }

    private fun loadGoalsForUser(userId: Int) {
        savingsGoalViewModel.getSavingsGoals(userId).observe(this) { goals ->
            if (goals.isNullOrEmpty()) {
                Toast.makeText(this, "No goals found. Create a goal first!", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return@observe
            }

            goalsList = goals
            val goalNames = goals.map { it.goalName }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, goalNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goalTypeSpinner.adapter = adapter

            binding.goalTypeSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedGoalId = goalsList[position].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
        }
    }

    private fun saveContribution() {
        val amount = binding.edtAmount.text.toString().toDoubleOrNull()
        val date = binding.edtDate.text.toString()

        if (selectedGoalId == -1 || amount == null || date.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val contribution = SavingsContribution(
            goalId = selectedGoalId,
            amount = amount,
            contributionDate = date
        )

        savingsGoalViewModel.addContribution(contribution)

        finish()
    }
}