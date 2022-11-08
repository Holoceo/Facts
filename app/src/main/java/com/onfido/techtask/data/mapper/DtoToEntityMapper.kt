package com.onfido.techtask.data.mapper

import com.onfido.techtask.data.database.entity.FactEntity
import com.onfido.techtask.data.network.dto.CatFactDto
import javax.inject.Inject

class DtoToEntityMapper @Inject constructor() {

    fun toEntity(dtos: List<CatFactDto>): List<FactEntity> =
        dtos.map { it.toEntity() }

    private fun CatFactDto.toEntity(): FactEntity =
        FactEntity(
            id = id,
            text = text,
            verified = status.verified,
            createdAt = createdAt
        )
}