package com.onfido.techtask.presentation

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.onfido.techtask.R
import com.onfido.techtask.di.MainScheduler
import com.onfido.techtask.domain.Fact
import com.onfido.techtask.domain.ObserveFactsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    private val resources: Resources,
    observeFactsInteractor: ObserveFactsInteractor
): ViewModel() {

    private val state = BehaviorSubject.createDefault<UiState>(UiState.Loading)
    private val queries = PublishSubject.create<String>()
    private val retries = PublishSubject.create<Unit>()
    private var disposable: Disposable? = null

    init {
        disposable = observeFactsInteractor(queries, retries)
            .map { it.toUiState() }
            .observeOn(mainScheduler)
            .subscribe { state.onNext(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun observeState(): Observable<UiState> = state

    fun onSearchClicked(query: String) {
        queries.onNext(query)
    }

    fun onRetryClicked() {
        state.onNext(UiState.Loading)
        retries.onNext(Unit)
    }

    private fun Result<List<Fact>>.toUiState(): UiState = fold(
        onSuccess = { UiState.Content(it) },
        onFailure = { UiState.Error(getErrorMessage(it)) }
    )

    private fun getErrorMessage(error: Throwable): String {
        val resId = if (error is UnknownHostException) R.string.error_no_internet else R.string.error_generic
        return resources.getString(resId)
    }
}