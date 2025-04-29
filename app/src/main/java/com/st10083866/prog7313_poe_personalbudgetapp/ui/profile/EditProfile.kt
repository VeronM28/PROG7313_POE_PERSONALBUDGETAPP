package com.st10083866.prog7313_poe_personalbudgetapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
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
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditProfileBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel
import kotlin.getValue
import java.io.File

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: LoginViewModel by viewModels()
    //default user Id (if Id isn't passed)
    private var userId: Int = -1
    private var selectedProfilePicturePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        //Get userId passed from previous fragment
        userId = intent.getIntExtra("USER_ID",-1)

        if(userId != -1){
            profileViewModel.loadUserById(userId)
        }

        profileViewModel.user.observe(this) { user ->
            if (user != null) {
                binding.edtUsername.setText(user.username)
                binding.edtEmail.setText(user.email)
                binding.edtPassword.setText(user.passwordHash)

                if (!user.profilePicturePath.isNullOrEmpty()){
                    val file = File(user.profilePicturePath)

                    if(file.exists()){
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        binding.uploadStatusIcon.setImageBitmap(bitmap)
                    }
                }
            }
        }

        binding.uploadStatusCard.setOnClickListener {
            openGalleryToPickImage()
        }


        binding.btnConfirm.setOnClickListener {
            saveProfile()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null){
                val filePath = getPathFromUri(imageUri)
                if (filePath !=null) {
                    selectedProfilePicturePath = filePath

                    val bitmap = BitmapFactory.decodeFile(filePath)
                    binding.uploadStatusIcon.setImageBitmap(bitmap)
                }
            }
        }
    }

    //gets file path from URI
    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val path = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }


    private fun openGalleryToPickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun saveProfile(){

        val newUsername = binding.edtUsername.text.toString()
        val newEmail = binding.edtEmail.text.toString()
        val newPassword = binding.edtPassword.toString()

        if( newUsername.isBlank() || newEmail.isBlank() || newPassword.isBlank()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        profileViewModel.updateUser(userId,newUsername,newEmail,newPassword,
            selectedProfilePicturePath.toString()
        )

        Toast.makeText(this,"Profile Updated!", Toast.LENGTH_SHORT).show()
        finish()
    }


}


