package com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.st10083866.prog7313_poe_personalbudgetapp.SessionManager
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentLoginPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.LoginViewModel


class LoginPageFragment : Fragment() {

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe user login result
        loginViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

                val session = SessionManager(requireContext())
                session.saveUserId(user.userId) // userId is now a String

                val intent = Intent(requireContext(), MainPageActivity::class.java)
                intent.putExtra("USER_ID", user.userId)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use Firestore login through ViewModel
            loginViewModel.loginUser(email, password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}