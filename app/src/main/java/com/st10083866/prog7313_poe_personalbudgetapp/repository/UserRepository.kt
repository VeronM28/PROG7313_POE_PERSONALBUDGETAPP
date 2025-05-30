package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    // Register user with FirebaseAuth and then save profile in Firestore
    fun registerUser(user: User, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid == null) {
                    onResult(false)
                    return@addOnSuccessListener
                }
                user.userId = uid

                // Save user profile to Firestore WITHOUT password
                usersCollection.document(uid).set(user)
                    .addOnSuccessListener { onResult(true) }
                    .addOnFailureListener { onResult(false) }
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    // Login user with FirebaseAuth and fetch user profile from Firestore
    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid == null) {
                    onResult(null)
                    return@addOnSuccessListener
                }
                getUserById(uid, onResult)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // Fetch user profile from Firestore by userId (FirebaseAuth UID)
    fun getUserById(userId: String, onResult: (User?) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onResult(doc.toObject(User::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    // Update user profile info in Firestore (no password)
    fun updateUserProfile(user: User, onResult: (Boolean) -> Unit) {
        val userMap = mapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "username" to user.username,
            "email" to user.email,
            "profilePicturePath" to user.profilePicturePath
        )

        usersCollection.document(user.userId).update(userMap)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Optional: Logout user
    fun logout() {
        auth.signOut()
    }
}