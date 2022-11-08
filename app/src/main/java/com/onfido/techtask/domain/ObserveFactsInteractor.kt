package com.onfido.techtask.domain

import com.onfido.techtask.data.FactsRepository
import io.reactivex.Observable
import javax.inject.Inject

class ObserveFactsInteractor @Inject constructor(
    private val repository: FactsRepository
) {

    operator fun invoke(
        queries: Observable<String>,
        retries: Observable<Unit>
    ): Observable<Result<List<Fact>>> =
        queries
            .startWith("")
            .switchMapSingle { query ->
                repository.getFacts(query).map { Result.success(it) }
            }
            .onErrorReturn { Result.failure(it) }
            .repeatWhen { completed ->
                completed.flatMap { retries }
            }
}