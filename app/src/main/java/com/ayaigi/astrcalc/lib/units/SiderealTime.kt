package com.ayaigi.astrcalc.lib.units

import com.ayaigi.astrcalc.core.GeoInstant
import java.time.*

data class SiderealTime(val value: Double) {
    fun tH() = Hour(value)
    constructor(h: Hour) : this(h.value)

    companion object {
        fun fromOffsetDateTime(gI: GeoInstant): SiderealTime {
            val gst = run {
                val T0 = run {
                    val D = gI.instant.dayOfYear
                    val B = ParaB(gI.instant.toLocalDate())
                    val p1 = D * 0.0657098f
                    val T0 = p1 - B
                    T0
                }
                val T1 = Hour.fromLocalTime(gI.instant.toLocalTime()) * 1.002738
                (T1 + Hour(T0)).correct24()
            }
            val lst = (gst + gI.lon.hour()).correct24()
            return SiderealTime(lst.value)
        }

        private fun ParaB(i: LocalDate): Double {
            val D1900 = LocalDate.of(1899, 12, 31).toEpochDay()
            val S = i.toEpochDay() - D1900
            val T = S / 36525
            val R = 6.6460656 + (2400.051262 * T) + (0.00002581 * (T * T))
            val U = R - (24 * (i.year - 1900))
            val B = 24 - U
            return B
        }
    }
    fun convertToLocalTime(gI: GeoInstant): OffsetTime {
        val dt = gI.instant.toUTC()
        val gst = (Hour(this.value) - gI.lon.hour()).correct24()
        val gmt = run {
            val T0 = dt.run { dayOfYear * 0.0657098 - ParaB(this.toLocalDate()) }
            val T1 = (gst - Hour(T0)).correct24() * 0.99727
            T1
        }
        return gmt.toLocalTime().atOffset(ZoneOffset.UTC).withOffsetSameInstant(gI.instant.offset)
    }
}

fun main() {
    val t1 = OffsetDateTime.of(2023, 2, 19, 16,33,0,0, ZoneOffset.ofHours(2))
    val t2 = t1.toUTC()
    val t3 = t2.toLocalTime().atOffset(ZoneOffset.UTC).withOffsetSameInstant(t1.offset)
    println(t1)
    println(t2)
    println(t3)
}