package com.onfido.techtask.presentation

import com.onfido.techtask.domain.Fact

sealed interface UiState {
    object Loading : UiState
    data class Content(val facts: List<Fact>) : UiState
    data class Error(val message: String) : UiState
}