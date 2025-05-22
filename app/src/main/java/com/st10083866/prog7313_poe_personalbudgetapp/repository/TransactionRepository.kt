package com.st10083866.prog7313_poe_personalbudgetapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TransactionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val transactionsCollection = db.collection("transactions")

    suspend fun insert(transaction: Transaction) {
        val docRef = if (transaction.id.isBlank()) {
            transactionsCollection.document()
        } else {
            transactionsCollection.document(transaction.id)
        }
        transaction.id = docRef.id
        docRef.set(transaction).await()
    }

    fun getAllTransactions(userId: String): Flow<List<Transaction>> = callbackFlow {
        val listener = transactionsCollection
            .whereEqualTo("userOwnerId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot.toObjects())
            }
        awaitClose { listener.remove() }
    }

    fun getTransactionsForUserBetweenDates(userId: String, fromDate: Long, toDate: Long): Flow<List<Transaction>> = callbackFlow {
        val listener = transactionsCollection
            .whereEqualTo("userOwnerId", userId)
            .whereGreaterThanOrEqualTo("date", fromDate)
            .whereLessThanOrEqualTo("date", toDate)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot.toObjects())
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateTransaction(transaction: Transaction) {
        if (transaction.id.isNotBlank()) {
            transactionsCollection.document(transaction.id).set(transaction).await()
        }
    }

    suspend fun deleteTransactionById(id: String) {
        transactionsCollection.document(id).delete().await()
    }

    fun getTransactionById(id: String): Flow<Transaction?> = callbackFlow {
        val listener = transactionsCollection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || !snapshot.exists()) {
                    trySend(null)
                    return@addSnapshotListener
                }
                trySend(snapshot.toObject<Transaction>())
            }
        awaitClose { listener.remove() }
    }
}