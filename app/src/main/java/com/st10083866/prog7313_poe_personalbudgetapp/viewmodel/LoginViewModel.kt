package com.st10083866.prog7313_poe_personalbudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import com.st10083866.prog7313_poe_personalbudgetapp.repository.UserRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    // Register user
    fun registerUser(user: User, password: String, onResult: (Boolean) -> Unit) {
        userRepository.registerUser(user, password, onResult)
    }


    // Login using email and passwordHash
    fun loginUser(email: String, password: String) {
        userRepository.login(email, password) { loggedInUser ->
            _user.postValue(loggedInUser)
        }
    }

    // Load user by Firestore ID (String)
    fun loadUserById(userId: String) {
        userRepository.getUserById(userId) { foundUser ->
            _user.postValue(foundUser)
        }
    }

    // Update Firestore user profile
    fun updateUser(user: User, onResult: (Boolean) -> Unit) {
        userRepository.updateUserProfile(user, onResult)
    }
}

