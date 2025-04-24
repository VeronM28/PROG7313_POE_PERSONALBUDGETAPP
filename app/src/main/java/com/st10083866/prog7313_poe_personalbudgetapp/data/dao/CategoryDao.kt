package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM categories WHERE userOwnerId = :userId")
    fun getCategory(userId: Int): LiveData<List<Category>>
}