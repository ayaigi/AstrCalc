package com.ayaigi.astrcalc.units

import java.time.*

data class SiderealTime(val value: Float) {
    fun tH() = Hour(value)
    constructor(h: Hour) : this(h.value)

    companion object {
        fun fromOffsetDateTime(v: OffsetDateTime, lon: Degree): SiderealTime {
            val gst = run {
                val T0 = run {
                    val D = v.dayOfYear
                    val B = ParaB(v.toLocalDate())
                    val p1 = D * 0.0657098f
                    val T0 = p1 - B
                    T0
                }
                val T1 = Hour.fromLocalTime(v.toLocalTime()) * 1.002738f
                (T1 + Hour(T0)).correct24()
            }
            val lst = (gst + lon.hour()).correct24()
            return SiderealTime(lst.value)
        }

        private fun ParaB(i: LocalDate): Float {
            val D1900 = LocalDate.of(1899, 12, 31).toEpochDay()
            val S = i.toEpochDay() - D1900
            val T = S / 36525
            val R = 6.6460656f + (2400.051262f * T) + (0.00002581f * (T * T))
            val U = R - (24 * (i.year - 1900))
            val B = 24 - U
            return B
        }
    }
    fun convertToLocalTime(date: LocalDate, lon: Degree): OffsetTime {
        val gst = (Hour(this.value) - lon.hour()).correct24()
        val gmt = run {
            val T0 = (date.dayOfYear * 0.0657098f) - ParaB(date)
            val T1 = (gst - Hour(T0)).correct24() * 0.99727f
            T1
        }
        return gmt.toLocalTime().atOffset(ZoneOffset.UTC)
    }
}
