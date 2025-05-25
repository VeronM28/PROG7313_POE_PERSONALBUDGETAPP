package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val contributionsRef = db.collection("savings_contributions")

    // Save a new contribution
    fun insert(contribution: SavingsContribution, onResult: (Boolean) -> Unit) {
        val docRef = contributionsRef.document(contribution.id.ifEmpty { contributionsRef.document().id })
        contribution.id = docRef.id

        docRef.set(contribution)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Get all contributions for a goal ID (ordered by contributionDate descending)
    fun getContributions(goalId: String): LiveData<List<SavingsContribution>> {
        val liveData = MutableLiveData<List<SavingsContribution>>()

        contributionsRef
            .whereEqualTo("goalId", goalId)
            .orderBy("contributionDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.postValue(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val contributions = snapshot.documents.mapNotNull {
                        it.toObject(SavingsContribution::class.java)
                    }
                    liveData.postValue(contributions)
                } else {
                    liveData.postValue(emptyList())
                }
            }

        return liveData
    }
}