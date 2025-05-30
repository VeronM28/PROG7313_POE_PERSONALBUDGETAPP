package com.st10083866.prog7313_poe_personalbudgetapp.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects

import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Budget
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class BudgetRepository {

    private val db = FirebaseFirestore.getInstance()
    private val budgetsRef = db.collection("budgets")
    private val categoriesRef = db.collection("categories")
    private val categoriesLiveData = MutableLiveData<List<Category>>()

    private var budgetsListener: ListenerRegistration? = null
    private val budgetsLiveData = MutableLiveData<List<Budget>>()
    private var categoriesListener: ListenerRegistration? = null


    // Insert or update budget (Firestore `set` replaces or creates)
    fun insertOrUpdateBudget(budget: Budget, onComplete: (Boolean) -> Unit) {
        val id = budget.id.takeIf { it.isNotEmpty() } ?: budgetsRef.document().id
        budget.id = id // Ensure budget has an ID

        budgetsRef.document(id)
            .set(budget)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Delete budget
    fun deleteBudget(budget: Budget, onComplete: (Boolean) -> Unit) {
        if (budget.id.isEmpty()) {
            onComplete(false)
            return
        }
        budgetsRef.document(budget.id)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // Get all budgets as LiveData, with real-time updates
    fun getAllBudgets(): LiveData<List<Budget>> {
        budgetsListener?.remove() // Remove any previous listener
        budgetsListener = budgetsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                budgetsLiveData.value = emptyList()
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val budgets = snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                budgetsLiveData.value = budgets
            }
        }
        return budgetsLiveData
    }
    fun getAllCategories(): LiveData<List<Category>> {
        categoriesListener?.remove()
        categoriesListener = categoriesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                categoriesLiveData.value = emptyList()
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val categories = snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
                categoriesLiveData.value = categories
            }
        }
        return categoriesLiveData
    }

    // Call this when repository is no longer used to avoid memory leaks
    fun clearListener() {
        budgetsListener?.remove()
        categoriesListener?.remove()

    }
}