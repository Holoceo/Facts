package com.onfido.techtask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onfido.techtask.data.database.entity.FactEntity
import com.onfido.techtask.data.database.entity.FactFts

@Database(entities = [FactEntity::class, FactFts::class], version = 1)
abstract class FactsDb : RoomDatabase() {
    abstract fun factsDao(): FactsDao
}