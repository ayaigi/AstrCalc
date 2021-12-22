package com.ayaigi.core

import org.junit.Assert.*

import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField

class ElseKtTest {

    @Test
    fun julianDay() {
        val i = OffsetDateTime.of(
            2020,
            12,
            3,
            12,
            24,
            53,
            0,
            ZoneOffset.UTC).toInstant()
        val d = i.atOffset(ZoneOffset.UTC).get(ChronoField.SECOND_OF_DAY)

        println()
        println(d)
        println()
    }
}