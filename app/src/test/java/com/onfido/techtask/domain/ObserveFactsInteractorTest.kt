package com.onfido.techtask.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.onfido.techtask.data.FactsRepository
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ObserveFactsInteractorTest {

    private val repository: FactsRepository = mock()
    private val queries = PublishSubject.create<String>()
    private val retries = PublishSubject.create<Unit>()

    @InjectMocks
    private lateinit var interactor: ObserveFactsInteractor

    @Before
    fun init() {
        whenever(repository.getFacts(any())).thenReturn(Single.just(emptyList()))
    }

    @Test
    fun `result for empty query is returned initially`() {
        interactor.invoke(queries, retries).test().assertValue(Result.success(emptyList()))
        verify(repository).getFacts("")
    }

    @Test
    fun `error result is returned in case of an error`() {
        val error = IllegalArgumentException()
        whenever(repository.getFacts(any())).thenReturn(Single.error(error))
        interactor.invoke(queries, retries).test().assertValue(Result.failure(error))
    }

    @Test
    fun `facts are fetched for each query`() {
        val observer = interactor.invoke(queries, retries).test()
        queries.onNext("a")
        queries.onNext("b")
        assertEquals(3, observer.valueCount())
        verify(repository).getFacts("a")
        verify(repository).getFacts("b")
    }

    @Test
    fun `facts are fetched on retry event`() {
        val error = IllegalArgumentException()
        whenever(repository.getFacts(any())).thenReturn(Single.error(error))
        val observer = interactor.invoke(queries, retries).test()
        whenever(repository.getFacts(any())).thenReturn(Single.just(emptyList()))
        retries.onNext(Unit)
        observer.assertValues(
            Result.failure(error),
            Result.success(emptyList())
        )
    }
}