package com.onfido.techtask.data.mapper

import com.onfido.techtask.data.database.entity.FactEntity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class EntityToDomainMapperTest {

    private val mapper = EntityToDomainMapper()

    @Test
    fun `fact is considered as new when it has been created in the last 90 days`() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss XXX")
        val currentDate = ZonedDateTime.now()
        val tooOld = currentDate.minusDays(91)
        val newEnough = currentDate.minusDays(90)

        val facts = mapper.toDomain(
            listOf(
                factEntity(formatter.format(tooOld)),
                factEntity(formatter.format(newEnough))
            )
        )

        assertFalse(facts[0].isNew)
        assertTrue(facts[1].isNew)
    }

    private fun factEntity(createdAt: String) =
        FactEntity(
            id = "id",
            text = "text",
            verified = true,
            createdAt = createdAt
        )
}