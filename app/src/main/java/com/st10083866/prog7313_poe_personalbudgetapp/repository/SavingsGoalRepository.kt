package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsGoal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SavingsGoalRepository {
    private val db = FirebaseFirestore.getInstance()
    private val goalsRef = db.collection("savings_goals")

    private var goalsListener: ListenerRegistration? = null

    // Insert a new savings goal
    fun insert(goal: SavingsGoal, onResult: (Boolean) -> Unit) {
        if (goal.id.isBlank()) {
            goal.id = UUID.randomUUID().toString()
        }

        goalsRef.document(goal.id).set(goal)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Get LiveData of all savings goals for a specific user
    fun getGoals(userId: String): LiveData<List<SavingsGoal>> {
        val liveData = MutableLiveData<List<SavingsGoal>>()

        // Remove previous listener if any
        goalsListener?.remove()

        goalsListener = goalsRef
            .whereEqualTo("userOwnerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    liveData.postValue(emptyList())
                    return@addSnapshotListener
                }

                val goals = snapshot.documents.mapNotNull { it.toObject(SavingsGoal::class.java) }
                liveData.postValue(goals)
            }

        return liveData
    }

    // Clean up listener when not needed
    fun removeGoalsListener() {
        goalsListener?.remove()
        goalsListener = null
    }
}