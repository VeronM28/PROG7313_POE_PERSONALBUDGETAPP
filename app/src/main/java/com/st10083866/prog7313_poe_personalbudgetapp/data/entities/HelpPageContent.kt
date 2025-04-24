package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "help_page_content")
data class HelpPageContent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectionTitle: String,
    val contentText: String,
    val pageTag: String? = null,
    val displayOrder: Int? = null
)