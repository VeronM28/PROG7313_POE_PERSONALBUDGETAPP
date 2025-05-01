package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityLaunchpageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.LoginPageFragment
import com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg.RegPageFragment

class LaunchPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launchpage)
        binding = ActivityLaunchpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLaunchLogin.setOnClickListener {
            val intent = Intent(this, LoginPageFragment::class.java)
            startActivity(intent)
        }

        binding.btnLaunchReg.setOnClickListener {
            val intent = Intent(this, RegPageFragment::class.java)
            startActivity(intent)
        }



    }
}