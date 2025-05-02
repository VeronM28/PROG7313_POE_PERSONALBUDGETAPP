package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityLaunchpageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.LoginPageFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.RegPageFragment

class LaunchPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("session", MODE_PRIVATE)
        prefs.edit().clear().apply()
        val session = SessionManager(this)

        // If the user is already logged in, they will go to MainPageActivity
        if (session.isLoggedIn()) {
            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra("USER_ID", session.getUserId())
            startActivity(intent)
            finish()
            return
        }

        // Login Button
        binding.btnLaunchLogin.setOnClickListener {
            showFragment(LoginPageFragment())
        }

        // Register Button
        binding.btnLaunchReg.setOnClickListener {
            showFragment(RegPageFragment())
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        // Hides the initial launch layout
        binding.launchContent.visibility = View.GONE

        // Load sthe fragment into the full-screen FrameLayout
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}