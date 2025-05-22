package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun insertUser(user: User): String {
        val documentRef = if (user.userId.isBlank()) {
            usersCollection.document() // Generate new ID
        } else {
            usersCollection.document(user.userId) // Use existing ID
        }
        user.userId = documentRef.id
        documentRef.set(user).await()
        return documentRef.id
    }

    suspend fun getUserById(userId: String): User? {
        val snapshot = usersCollection.document(userId).get().await()
        return if (snapshot.exists()) snapshot.toObject<User>() else null
    }

    suspend fun getUserByUsernameAndPassword(username: String, passwordHash: String): User? {
        val snapshot = usersCollection
            .whereEqualTo("username", username)
            .whereEqualTo("passwordHash", passwordHash)
            .get()
            .await()
        return snapshot.toObjects<User>().firstOrNull()
    }

    suspend fun login(email: String, password: String): User? {
        val snapshot = usersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("passwordHash", password)
            .get()
            .await()
        return snapshot.toObjects<User>().firstOrNull()
    }

    suspend fun updateUserProfile(userId: String, username: String, email: String, password: String, picturePath: String) {
        val updates = mapOf(
            "username" to username,
            "email" to email,
            "passwordHash" to password,
            "profilePicturePath" to picturePath
        )
        usersCollection.document(userId).update(updates).await()
    }
}