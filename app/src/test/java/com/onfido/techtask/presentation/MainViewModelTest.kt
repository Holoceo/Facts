package com.onfido.techtask.presentation

import android.content.res.Resources
import androidx.annotation.StringRes
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.onfido.techtask.R
import com.onfido.techtask.domain.Fact
import com.onfido.techtask.domain.ObserveFactsInteractor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val mainScheduler = Schedulers.trampoline()
    private val resources: Resources = mock()
    private val observeFactsInteractor: ObserveFactsInteractor = mock()

    @Test
    fun `UnknownHostException shows no internet connection error message`() =
        testError(
            error = UnknownHostException(),
            expectedMessage = R.string.error_no_internet
        )

    @Test
    fun `error other than UnknownHostException shows generic error message`() =
        testError(
            error = IllegalArgumentException(),
            expectedMessage = R.string.error_generic
        )

    @Test
    fun `state is updated to Loading when retry is clicked`() {
        val result = PublishSubject.create<Result<List<Fact>>>()
        whenever(observeFactsInteractor.invoke(any(), any())).thenReturn(result)
        val viewModel = viewModel()
        val observer = viewModel.observeState().test()
        viewModel.onRetryClicked()
        assert(observer.values().last() is UiState.Loading)
    }

    private fun testError(error: Throwable, @StringRes expectedMessage: Int) {
        val message = "Message"
        mockInteractor(Result.failure(error))
        whenever(resources.getString(expectedMessage)).thenReturn(message)
        viewModel().observeState().test().assertValue(UiState.Error(message))
    }

    private fun viewModel() = MainViewModel(mainScheduler, resources, observeFactsInteractor)

    private fun mockInteractor(result: Result<List<Fact>>) {
        whenever(observeFactsInteractor.invoke(any(), any())).thenReturn(
            Observable.just(result)
        )
    }
}