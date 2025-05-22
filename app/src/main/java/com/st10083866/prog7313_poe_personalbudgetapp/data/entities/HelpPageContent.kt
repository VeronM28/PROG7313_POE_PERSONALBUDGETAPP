package com.st10083866.prog7313_poe_personalbudgetapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class HelpPageContent(
    var id: String = "",
    var sectionTitle: String = "",
    var contentText: String = "",
    var pageTag: String? = null,
    var displayOrder: Int? = null
)