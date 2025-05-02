package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityLaunchpageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.LoginPageFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.RegPageFragment

class LaunchPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLaunchpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)

        if (session.isLoggedIn()) {
            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra("USER_ID", session.getUserId())
            startActivity(intent)
            finish()
            return
        }

        binding.btnLaunchLogin.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginPageFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLaunchReg.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegPageFragment())
                .addToBackStack(null)
                .commit()
        }
    }

}