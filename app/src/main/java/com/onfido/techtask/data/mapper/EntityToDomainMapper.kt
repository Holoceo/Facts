package com.onfido.techtask.data.mapper

import com.onfido.techtask.data.database.entity.FactEntity
import com.onfido.techtask.domain.Fact
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class EntityToDomainMapper @Inject constructor() {

    fun toDomain(entities: List<FactEntity>): List<Fact> =
        entities.map { it.toDomain() }

    private fun FactEntity.toDomain(): Fact {
        val date = ZonedDateTime.parse(createdAt, DATE_FORMATTER)
        val currentDate = ZonedDateTime.now()
        val daysBetween = ChronoUnit.DAYS.between(date, currentDate)
        return Fact(
            id = id,
            text = text,
            verified = verified,
            isNew = daysBetween <= NEW_DAYS_THRESHOLD
        )
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss XXX")
        private const val NEW_DAYS_THRESHOLD = 90
    }
}