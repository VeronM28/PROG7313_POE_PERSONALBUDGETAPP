package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SavingsGoalRepository {

    private val db = FirebaseFirestore.getInstance()
    private val goalsCollection = db.collection("savings_goals")

    suspend fun insert(goal: SavingsGoal) {
        val docRef = if (goal.id.isBlank()) {
            goalsCollection.document() // Create new doc
        } else {
            goalsCollection.document(goal.id)
        }
        goal.id = docRef.id
        docRef.set(goal).await()
    }

    fun getGoals(userId: String): Flow<List<SavingsGoal>> = callbackFlow {
        val listener = goalsCollection
            .whereEqualTo("userOwnerId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot.toObjects())
            }

        awaitClose { listener.remove() }
    }
}