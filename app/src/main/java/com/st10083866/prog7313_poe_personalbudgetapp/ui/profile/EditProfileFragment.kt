package com.st10083866.prog7313_poe_personalbudgetapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.ActivityEditProfileBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel
import java.io.File

class EditProfileFragment : Fragment() {

    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: LoginViewModel by viewModels()
    private var userId: String = ""
    private var selectedProfilePicturePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("USER_ID") ?: "-1"

        if (userId != "-1") {
            profileViewModel.loadUserById(userId)
        }

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.edtUsername.setText(user.username)
                binding.edtEmail.setText(user.email)
                binding.edtPassword.setText(user.passwordHash)

                if (!user.profilePicturePath.isNullOrEmpty()) {
                    val file = File(user.profilePicturePath)
                    if (file.exists()) {
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

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                val filePath = getPathFromUri(imageUri)
                if (filePath != null) {
                    selectedProfilePicturePath = filePath
                    val bitmap = BitmapFactory.decodeFile(filePath)
                    binding.uploadStatusIcon.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
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

    private fun saveProfile() {
        val newUsername = binding.edtUsername.text.toString()
        val newEmail = binding.edtEmail.text.toString()
        val newPassword = binding.edtPassword.text.toString()

        if (newUsername.isBlank() || newEmail.isBlank() || newPassword.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = User(
            userId =  userId,
            username = newUsername,
            email = newEmail,
            passwordHash = newPassword,
            profilePicturePath =  selectedProfilePicturePath ?: ""
        )

        profileViewModel.updateUser(updatedUser) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }

        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
