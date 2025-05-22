package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.BudgetDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BudgetRepository {

    private val db = FirebaseFirestore.getInstance()
    private val budgetsCollection = db.collection("budgets")

    suspend fun insertOrUpdateBudget(budget: Budget) {
        val docRef = if (budget.id.isBlank()) {
            budgetsCollection.document() // New document
        } else {
            budgetsCollection.document(budget.id) // Existing budget
        }
        budget.id = docRef.id
        docRef.set(budget).await()
    }

    suspend fun deleteBudget(budget: Budget) {
        if (budget.id.isNotBlank()) {
            budgetsCollection.document(budget.id).delete().await()
        }
    }

    fun getAllBudgets(): Flow<List<Budget>> = callbackFlow {
        val listener = budgetsCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot.toObjects())
        }
        awaitClose { listener.remove() }
    }

    fun getBudgetsForUser(userId: String): Flow<List<Budget>> = callbackFlow {
        val listener = budgetsCollection
            .whereEqualTo("userOwnerId", userId)
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