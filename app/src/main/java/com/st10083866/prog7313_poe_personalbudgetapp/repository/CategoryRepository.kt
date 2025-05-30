package com.st10083866.prog7313_poe_personalbudgetapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CategoryRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val categoriesRef = firestore.collection("categories")

    // Insert or update category
    fun upsertCategory(category: Category, onResult: (Boolean) -> Unit) {
        val docId = category.id ?: categoriesRef.document().id  // Generate id if null
        category.id = docId

        categoriesRef.document(docId).set(category)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Get all categories for a specific user as LiveData
    fun getCategoriesForUser(userId: String): LiveData<List<Category>> {
        val liveData = MutableLiveData<List<Category>>()
        categoriesRef.whereEqualTo("userOwnerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
                    liveData.value = list
                } else {
                    liveData.value = emptyList()
                }
            }
        return liveData
    }

    // Get all categories (all users)
    fun getAllCategories(userId: String): LiveData<List<Category>> {
        val liveData = MutableLiveData<List<Category>>()
        categoriesRef.whereEqualTo("userOwnerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.mapNotNull {
                    it.toObject(Category::class.java)
                } ?: emptyList()

                liveData.value = categories
            }
        return liveData
    }


    // Delete category
    fun deleteCategory(categoryId: String, onResult: (Boolean) -> Unit) {
        categoriesRef.document(categoryId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Get category name by id (async)
    fun getCategoryNameById(categoryId: String, onResult: (String?) -> Unit) {
        categoriesRef.document(categoryId).get()
            .addOnSuccessListener { doc ->
                val category = doc.toObject(Category::class.java)
                onResult(category?.name)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


    fun getCategoriesByUser(userId: String): LiveData<List<Category>> {
        val liveData = MutableLiveData<List<Category>>()
        categoriesRef.whereEqualTo("userOwnerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }
                val categories = snapshot.documents.mapNotNull { it.toObject(Category::class.java) }
                liveData.value = categories
            }
        return liveData
    }
}
