package com.onfido.techtask.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts")
data class FactEntity(
    @PrimaryKey val id: String,
    val text: String,
    val verified: Boolean,
    val createdAt: String
)