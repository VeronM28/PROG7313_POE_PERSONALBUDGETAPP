package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.UserDao
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import com.st10083866.prog7313_poe_personalbudgetapp.ui.home.MainPageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPage : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "budget_app_dp"
        ).build()
        userDao = db.userDao()

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUser(username, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(this@LoginPage, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to Home/Dashboard
                        val intent = Intent(this@LoginPage, MainPageActivity::class.java)
                        intent.putExtra("userId", user.userId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginPage, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    }