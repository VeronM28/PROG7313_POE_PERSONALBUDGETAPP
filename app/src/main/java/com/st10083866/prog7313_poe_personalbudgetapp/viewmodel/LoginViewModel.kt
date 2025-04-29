package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.st10083866.prog7313_poe_personalbudgetapp.database.AppDatabase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application){
    private val userDao = AppDatabase.getDatabase(application).userDao()

    //this function checks if the user exists based on email and password
    fun loginUser(email: String, password: String, onResult: (User?) -> Unit){
        viewModelScope.launch {
            val user = userDao.login(email, password)
            onResult(user)
        }
    }

    //this function inserts a new user into the database
    fun registerUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            userDao.insertUser(user)
            onResult(true)
        }
    }
}