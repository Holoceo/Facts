package com.onfido.techtask.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.onfido.techtask.data.database.FactsDao
import com.onfido.techtask.data.database.entity.FactEntity
import com.onfido.techtask.data.mapper.DtoToEntityMapper
import com.onfido.techtask.data.mapper.EntityToDomainMapper
import com.onfido.techtask.data.network.CatFactAPI
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FactsRepositoryTest {

    private val scheduler = Schedulers.trampoline()
    private val api: CatFactAPI = mock()
    private val dao: FactsDao = mock()
    private val dtoToEntityMapper: DtoToEntityMapper = mock()
    private val entityToDomainMapper: EntityToDomainMapper = mock()
    private lateinit var repository: FactsRepository

    @Before
    fun init() {
        repository = FactsRepository(
            ioScheduler = scheduler,
            api = api,
            dao = dao,
            dtoToEntityMapper = dtoToEntityMapper,
            entityToDomainMapper = entityToDomainMapper
        )

        whenever(dtoToEntityMapper.toEntity(any())).thenReturn(ENTITIES)
        whenever(entityToDomainMapper.toDomain(any())).thenReturn(emptyList())
        whenever(api.get()).thenReturn(Single.just(emptyList()))
        whenever(dao.getAll()).thenReturn(Single.just(emptyList()))
        whenever(dao.search(any())).thenReturn(Single.just(emptyList()))
        mockDaoFactsCount(2)
    }

    @Test
    fun `facts are downloaded from backend if database is empty`() {
        mockDaoFactsCount(0)
        repository.getFacts("").test().await()
        verify(api).get()
        verify(dao).insertAll(ENTITIES)
    }

    @Test
    fun `facts are not downloaded from backend if database is not empty`() {
        repository.getFacts("").test().await()
        verify(api, times(0)).get()
    }

    @Test
    fun `all entities are loaded if query is empty`() {
        repository.getFacts("").test().await()
        verify(dao).getAll()
    }

    @Test
    fun `entities are searched if query is not empty`() {
        val query = "abc"
        repository.getFacts(query).test().await()
        verify(dao).search(query)
    }

    private fun mockDaoFactsCount(count: Int) {
        whenever(dao.getCount()).thenReturn(Single.just(count))
    }

    companion object {
        private val ENTITIES = listOf(
            FactEntity("id", "text", true, "2022")
        )
    }

}