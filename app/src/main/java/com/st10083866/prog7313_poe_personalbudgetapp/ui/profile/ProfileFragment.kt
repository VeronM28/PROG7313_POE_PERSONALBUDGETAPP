package com.st10083866.prog7313_poe_personalbudgetapp.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.st10083866.prog7313_poe_personalbudgetapp.LaunchPageActivity

import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentProfilePageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null

    private val binding get() = _binding!!

    private val profileViewModel: LoginViewModel by viewModels()
    private var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("USER_ID") ?: ""
        if (userId.isBlank()) {
            Toast.makeText(requireContext(), "Invalid user session", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId.isNotBlank()) {
            profileViewModel.loadUserById(userId.toString())
        }

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
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

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(), LaunchPageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
