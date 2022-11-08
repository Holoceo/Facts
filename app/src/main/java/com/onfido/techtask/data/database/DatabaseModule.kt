package com.onfido.techtask.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FactsDb =
        Room.databaseBuilder(context, FactsDb::class.java, "facts").build()

    @Provides
    @Singleton
    fun provideFactsDao(db: FactsDb): FactsDao = db.factsDao()
}