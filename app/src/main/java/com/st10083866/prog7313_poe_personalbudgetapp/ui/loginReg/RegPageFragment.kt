package com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.st10083866.prog7313_poe_personalbudgetapp.R
import com.st10083866.prog7313_poe_personalbudgetapp.SessionManager
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentRegPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import kotlinx.coroutines.launch

class RegPageFragment : Fragment() {

    private var _binding: FragmentRegPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getDatabase(requireContext())

        binding.btnReg.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User(
            firstName = firstName,
            lastName = lastName,
            username = username,
            email = email,
            passwordHash = password
        )

        lifecycleScope.launch {
            db.userDao().insertUser(user)

            val session = SessionManager(requireContext())
            session.saveUserId(user.userId) // Save the actual userId

            val intent = Intent(requireContext(), MainPageActivity::class.java)
            intent.putExtra("USER_ID", user.userId)
            startActivity(intent)
            requireActivity().finish()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}