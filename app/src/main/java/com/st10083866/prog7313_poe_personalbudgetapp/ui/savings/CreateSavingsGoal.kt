package com.st10083866.prog7313_poe_personalbudgetapp.ui.savings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityCreateSavingsGoalBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.SavingsGoalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateSavingsGoal : AppCompatActivity() {

    private lateinit var binding: ActivityCreateSavingsGoalBinding
    private val savingsGoalViewModel: SavingsGoalViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_savings_goal)

        //Get userId passed from previous fragment
        userId = intent.getIntExtra("USER_ID", -1)

        binding.btnCreateGoal.setOnClickListener {
            val goalName = binding.edtName.text.toString()
            val targetAmount = binding.edtAmount.toString().toDoubleOrNull()
            val deadline = binding.edtDate.toString()

            if (goalName.isBlank() || targetAmount == null || deadline.isBlank()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
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

            finish()
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}