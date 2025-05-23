package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun insertUser(user: User, onResult: (Boolean) -> Unit) {
        if (user.userId.isBlank()) {
            user.userId = usersCollection.document().id // Generate new ID if needed
        }
        usersCollection.document(user.userId).set(user)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserById(userId: String, onResult: (User?) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onResult(doc.toObject(User::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { onResult(null) }
    }

    fun getUserByUsernameAndPassword(username: String, passwordHash: String, onResult: (User?) -> Unit) {
        usersCollection
            .whereEqualTo("username", username)
            .whereEqualTo("passwordHash", passwordHash)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener { onResult(null) }
    }

    fun login(email: String, passwordHash: String, onResult: (User?) -> Unit) {
        usersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("passwordHash", passwordHash)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener { onResult(null) }
    }

    fun updateUserProfile(user: User, onResult: (Boolean) -> Unit) {
        usersCollection.document(user.userId).update(
            mapOf(
                "username" to user.username,
                "email" to user.email,
                "passwordHash" to user.passwordHash,
                "profilePicturePath" to user.profilePicturePath
            )
        ).addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}