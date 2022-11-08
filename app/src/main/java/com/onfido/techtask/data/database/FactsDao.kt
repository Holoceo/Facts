package com.onfido.techtask.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onfido.techtask.data.database.entity.FactEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FactsDao {

    @Query("SELECT * FROM facts JOIN factsFts ON facts.id == factsFts.id WHERE factsFts.text MATCH :query")
    fun search(query: String): Single<List<FactEntity>>

    @Query("SELECT COUNT(id) FROM facts")
    fun getCount(): Single<Int>

    @Query("SELECT * FROM facts")
    fun getAll(): Single<List<FactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(facts: List<FactEntity>): Completable
}