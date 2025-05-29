package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val transactionsRef = db.collection("transactions")

    // Insert a transaction
    fun insert(transaction: Transaction, onComplete: (Boolean) -> Unit) {
        // Use Firestore document ID = transaction.id or auto-generated if null
        val docId = if (transaction.id.isBlank()) transactionsRef.document().id else transaction.id
        transaction.id = docId
        transactionsRef.document(docId).set(transaction)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Update a transaction (same as insert with existing doc ID)
    fun updateTransaction(transaction: Transaction, onComplete: (Boolean) -> Unit) {
        transaction.id?.let { id ->
            transactionsRef.document(id.toString()).set(transaction)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } ?: onComplete(false)
    }

    // Delete transaction by ID
    fun deleteTransactionById(id: String, onComplete: (Boolean) -> Unit) {
        transactionsRef.document(id.toString()).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Get transaction by ID
    fun getTransactionById(id: String): LiveData<Transaction?> {
        val liveData = MutableLiveData<Transaction?>()
        transactionsRef.document(id.toString())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = null
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val transaction = snapshot.toObject(Transaction::class.java)
                    liveData.value = transaction
                } else {
                    liveData.value = null
                }
            }
        return liveData
    }

    // Get all transactions for user ordered by date descending
    fun getAllTransactions(userId: String): LiveData<List<Transaction>> {
        val liveData = MutableLiveData<List<Transaction>>()
        transactionsRef
            .whereEqualTo("userOwnerId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }
                val transactions = snapshots.documents.mapNotNull { it.toObject(Transaction::class.java) }
                liveData.value = transactions
            }
        return liveData
    }

    // Get transactions for user between two dates ordered by date descending
    fun getTransactionsForUserBetweenDates(userId: String, fromDate: Long, toDate: Long): LiveData<List<Transaction>> {
        val liveData = MutableLiveData<List<Transaction>>()
        transactionsRef
            .whereEqualTo("userOwnerId", userId)
            .whereGreaterThanOrEqualTo("date", fromDate)
            .whereLessThanOrEqualTo("date", toDate)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }
                val transactions = snapshots.documents.mapNotNull { it.toObject(Transaction::class.java) }
                liveData.value = transactions
            }
        return liveData
    }
}