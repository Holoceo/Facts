package com.onfido.techtask.data

import com.onfido.techtask.data.database.FactsDao
import com.onfido.techtask.data.mapper.DtoToEntityMapper
import com.onfido.techtask.data.mapper.EntityToDomainMapper
import com.onfido.techtask.data.network.CatFactAPI
import com.onfido.techtask.data.network.dto.CatFactDto
import com.onfido.techtask.di.IoScheduler
import com.onfido.techtask.domain.Fact
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject

class FactsRepository @Inject constructor(
    @IoScheduler private val ioScheduler: Scheduler,
    private val api: CatFactAPI,
    private val dao: FactsDao,
    private val dtoToEntityMapper: DtoToEntityMapper,
    private val entityToDomainMapper: EntityToDomainMapper
) {

    fun getFacts(query: String): Single<List<Fact>> =
        fetchFactsIfAbsent()
            .andThen(searchFacts(query))
            .subscribeOn(ioScheduler)

    private fun fetchFactsIfAbsent(): Completable =
        dao.getCount().flatMapCompletable { count ->
            if (count == 0) refreshFacts() else Completable.complete()
        }

    private fun refreshFacts(): Completable =
        api.get().flatMapCompletable { facts ->
            dao.insertAll(dtoToEntityMapper.toEntity(facts))
        }

    private fun searchFacts(query: String): Single<List<Fact>> {
        val facts = if (query.isEmpty()) dao.getAll() else dao.search(query)
        return facts.map { entityToDomainMapper.toDomain(it) }
    }
}