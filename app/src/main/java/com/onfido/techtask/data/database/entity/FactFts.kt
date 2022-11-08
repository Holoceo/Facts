package com.onfido.techtask.data.database.entity

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = FactEntity::class)
@Entity(tableName = "factsFts")
class FactFts(
    val id: String,
    val text: String
)