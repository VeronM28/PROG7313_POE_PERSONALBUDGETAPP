package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.SavingsContribution
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SavingsContributionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val contributionsCollection = db.collection("savings_contributions")

    suspend fun insert(contribution: SavingsContribution) {
        val docRef = if (contribution.id.isBlank()) {
            contributionsCollection.document() // New doc with generated ID
        } else {
            contributionsCollection.document(contribution.id)
        }
        contribution.id = docRef.id
        docRef.set(contribution).await()
    }

    fun getContributions(goalId: String): Flow<List<SavingsContribution>> = callbackFlow {
        val listener = contributionsCollection
            .whereEqualTo("goalId", goalId)
            .orderBy("contributionDate", Query.Direction.DESCENDING)
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