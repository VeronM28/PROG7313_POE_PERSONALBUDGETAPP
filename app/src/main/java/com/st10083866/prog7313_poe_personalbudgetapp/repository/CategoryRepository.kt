package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import com.st10083866.prog7313_poe_personalbudgetapp.data.dao.CategoryDao
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CategoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val categoriesCollection = db.collection("categories")

    suspend fun insertOrUpdateCategory(category: Category) {
        val docRef = if (category.id.isBlank()) {
            categoriesCollection.document()
        } else {
            categoriesCollection.document(category.id)
        }
        category.id = docRef.id
        docRef.set(category).await()
    }

    suspend fun deleteCategory(category: Category) {
        if (category.id.isNotBlank()) {
            categoriesCollection.document(category.id).delete().await()
        }
    }

    fun getCategoriesForUser(userId: String): Flow<List<Category>> = callbackFlow {
        val listener = categoriesCollection
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

    fun getAllCategories(): Flow<List<Category>> = callbackFlow {
        val listener = categoriesCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot.toObjects())
        }
        awaitClose { listener.remove() }
    }

    suspend fun getCategoryNameById(categoryId: String?): String? {
        if (categoryId == null || categoryId.isBlank()) return null
        val doc = categoriesCollection.document(categoryId).get().await()
        return if (doc.exists()) {
            doc.getString("name")
        } else null
    }
}