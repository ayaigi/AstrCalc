package com.ayaigi.astrcalc.lib.units

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.JulianFields


fun LocalDateTime.julianDay(): Double {
    val d = toLocalDate()
    val t = toLocalTime()
    return d.get(JulianFields.JULIAN_DAY) + Hour.fromLocalTime(t).toDecimalDay()
}

fun Instant.julianDay(): Double = atOffset(ZoneOffset.UTC).run {
    getLong(JulianFields.JULIAN_DAY) + get(ChronoField.SECOND_OF_DAY) / 86400.0
}

fun Instant.date(): LocalDate = LocalDate.ofEpochDay(this.epochSecond / 86400)

fun Instant.startOfDay(): Instant {
    return this.atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC)
}
fun Instant.plusDays(v: Float): Instant {
    return plusSeconds((v * 86400).toLong())
}
fun Instant.plusDays(v: Int): Instant {
    return plusSeconds(v * 86400L)
}


fun LocalDateTime.epochDay1970(): Double {
    return this.toLocalDate().toEpochDay() + Hour.fromLocalTime(this.toLocalTime()).toDecimalDay()
}

fun LocalDateTime.epochDay1980(): Double {
    return this.toLocalDate().toEpochDay() + Hour.fromLocalTime(this.toLocalTime())
        .toDecimalDay() - 3651
}

fun Instant.epochDay70() = this.epochSecond / 86400.0
fun Instant.epochDay80() = this.epochSecond / 86400.0 - 3651.0

fun OffsetDateTime.toUTC() = this.toLocalDateTime().minusSeconds(this.offset.totalSeconds.toLong()).atOffset(ZoneOffset.UTC)

fun invalid(message: String): Nothing = throw Exception(message)