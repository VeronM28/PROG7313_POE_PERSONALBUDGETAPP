package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE userOwnerId = :userId")
    suspend fun getCategoriesForUser(userId: Int): List<Category>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)


}