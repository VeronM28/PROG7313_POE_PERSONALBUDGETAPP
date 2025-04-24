package com.st10083866.prog7313_poe_personalbudgetapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.HelpPageContent

@Dao
interface HelpPageContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: HelpPageContent)

    @Query("SELECT * FROM help_page_content ORDER BY displayOrder ASC")
    fun getHelpContent(): LiveData<List<HelpPageContent>>

    @Query("SELECT * FROM help_page_content WHERE pageTag = :tag ORDER BY displayOrder ASC")
    fun getHelpContentByPage(tag: String): LiveData<List<HelpPageContent>>
}