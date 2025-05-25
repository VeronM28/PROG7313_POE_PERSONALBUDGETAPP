package com.st10083866.prog7313_poe_personalbudgetapp.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.st10083866.prog7313_poe_personalbudgetapp.ActivityLogFragment
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.SessionManager
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityMainPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.budget.BudgetOverviewFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.budget.CreateBudgetFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.budget.EditBudgetFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.category.CategoryFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.category.EditCategoryFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.profile.EditProfileFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.profile.ProfileFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.CreateSavingsContributionFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.CreateSavingsGoalFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.savings.SavingsGoalFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.transactions.AddExpensesFragment

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //loads userID from saved session
        val session = SessionManager(this)
        userId = intent.getStringExtra("USER_ID") ?: session.getUserId() ?: ""

        //default fragment
        loadFragment(HomeFragment().withUser(userId))

        // Drawer item clicks
        binding.navigationDrawer.setNavigationItemSelectedListener  { menuitem ->
            when (menuitem.itemId) {
                R.id.navCreateSavingsContribution -> {
                    loadFragment(CreateSavingsContributionFragment().withUser(userId))
                    true
                }
                R.id.navSavingGoals -> {
                    loadFragment(SavingsGoalFragment().withUser(userId))
                    true
                }
                R.id.navAddSavingGoal -> {
                    loadFragment(CreateSavingsGoalFragment().withUser(userId))
                    true
                }
                R.id.navEditProfilePage -> {
                    loadFragment(EditProfileFragment().withUser(userId))
                    true
                }
                R.id.navAddCategory -> {
                    loadFragment(CategoryFragment().withUser(userId))
                    true
                }
                R.id.navEditCategory -> {

                    loadFragment(EditCategoryFragment().withUser(userId))
                    true
                }
                R.id.navCreateBudget -> {
                    loadFragment(CreateBudgetFragment().withUser(userId))
                    true
                }
                R.id.navEditBudget -> {
                    loadFragment(EditBudgetFragment().withUser(userId))
                    true
                }
                R.id.navLogPage -> {
                    loadFragment(ActivityLogFragment().withUser(userId))
                    true
                }
                else -> false
            }.also{
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        // Bottom Navigation item clicks
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment().withUser(userId))
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment().withUser(userId))
                    true
                }
                R.id.nav_addEntry -> {
                    loadFragment(AddExpensesFragment().withUser(userId))
                    true
                }
                R.id.nav_budget -> {
                    loadFragment(BudgetOverviewFragment().withUser(userId))

                    true
                }
                else -> false
            }.also {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        setSupportActionBar(binding.toolbar) // set toolbar for action bar

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
    fun Fragment.withUser(userId: String): Fragment {
        arguments = Bundle().apply { putString("USER_ID", userId) }
        return this
    }


}
