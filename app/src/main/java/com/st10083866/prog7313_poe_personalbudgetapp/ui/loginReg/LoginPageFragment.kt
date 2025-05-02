package com.st10083866.prog7313_poe_personalbudgetapp.ui.loginReg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.st10083866.prog7313_poe_personalbudgetapp.SessionManager
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentLoginPageBinding
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPageFragment : Fragment() {

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    private val userDao by lazy {
        Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "budget_app_dp"
        ).build().userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUser(username, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()

                        val session = SessionManager(requireContext())
                        session.saveUserId(user.userId.toInt())

                        val intent = Intent(requireContext(), MainPageActivity::class.java)
                        intent.putExtra("USER_ID", user.userId)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
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