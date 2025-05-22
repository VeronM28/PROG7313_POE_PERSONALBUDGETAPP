package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.HelpPageContent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HelpPageContentRepository {

    private val db = FirebaseFirestore.getInstance()
    private val helpContentCollection = db.collection("help_page_content")

    suspend fun insert(content: HelpPageContent) {
        val docRef = if (content.id.isBlank()) {
            helpContentCollection.document() // Auto-generated ID
        } else {
            helpContentCollection.document(content.id)
        }
        content.id = docRef.id
        docRef.set(content).await()
    }

    fun getHelpContent(): Flow<List<HelpPageContent>> = callbackFlow {
        val listener = helpContentCollection
            .orderBy("displayOrder", Query.Direction.ASCENDING)
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