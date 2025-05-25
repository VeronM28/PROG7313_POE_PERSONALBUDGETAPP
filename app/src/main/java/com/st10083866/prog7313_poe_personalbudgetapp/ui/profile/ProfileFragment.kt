package com.st10083866.prog7313_poe_personalbudgetapp.ui.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentProfilePageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null

    private val binding get() = _binding!!

    private val profileViewModel: LoginViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userId = it.getInt("USER_ID", -1)
        }

        if (userId != -1) {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
