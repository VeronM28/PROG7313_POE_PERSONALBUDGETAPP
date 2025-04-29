package com.st10083866.prog7313_poe_personalbudgetapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityProfilePageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel
import java.io.File

class ProfilePage : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePageBinding
    private val profileViewModel: LoginViewModel by viewModels()

    //default user Id (if Id isn't passed)
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_page)

        //Get userId passed from previous fragment
        userId = intent.getIntExtra("USER_ID", -1)

        if (userId != -1) {
            profileViewModel.loadUserById(userId)
        }

        profileViewModel.user.observe(this) { user ->
            if (user != null) {
                val userFullName = "@${user.fname + user.sname}"
                binding.txtUsername.text = userFullName
                binding.txtUsername.text = "@${user.username}"
                binding.txtEmail.text = user.email
                if (!user.profilePicturePath.isNullOrEmpty()) {
                    val file = File(user.profilePicturePath)
                    if (file.exists()) {
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding.imgProfile.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}