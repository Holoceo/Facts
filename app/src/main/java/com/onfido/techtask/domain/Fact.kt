package com.onfido.techtask.domain

data class Fact(
    val id: String,
    val text: String,
    val verified: Boolean,
    val isNew: Boolean
)