package com.st10083866.prog7313_poe_personalbudgetapp.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityMainPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.profile.EditProfileFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.CreateSavingsContributionFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.CreateSavingsGoalFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.SavingsGoalFragment

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding
    private var userId: Int = -1
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        userId = intent.getIntExtra("USER_ID", -1)

        //default fragment
        loadFragment(HomeFragment().apply {
            arguments = Bundle().apply { putInt("USER_ID", userId) }
        })

        // Bottom nav item clicks
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navCreateSavingsContribution -> {
                    loadFragment(CreateSavingsContributionFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navSavingGoals -> {
                    loadFragment(SavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navAddSavingGoal -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navEditProfilePage -> {
                    loadFragment(EditProfileFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navAddCategory -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navEditCategory -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navCreateBudget -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navEditBudget -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navExpenses -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navMonthlySpending -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                R.id.navLogPage -> {
                    loadFragment(CreateSavingsGoalFragment().apply {
                        arguments = Bundle().apply { putInt("USER_ID", userId) }
                    })
                    true
                }
                else -> false
            }
        }

        // Drawer item clicks
        binding.navigationDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    finish()
                    true
                }
                R.id.nav_home -> {

                    true
                }
                R.id.nav_budget -> {

                    true
                }
                R.id.nav_addEntry -> {

                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }


    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
