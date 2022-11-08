package com.onfido.techtask.data.network

import com.onfido.techtask.data.network.dto.CatFactDto
import io.reactivex.Single
import retrofit2.http.GET

interface CatFactAPI {

    @GET("18962a8a5d00e62a8d2a")
    fun get(): Single<List<CatFactDto>>
}
