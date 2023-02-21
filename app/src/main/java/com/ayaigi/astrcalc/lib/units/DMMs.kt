package com.ayaigi.astrcalc.lib.units

import java.time.LocalTime
import kotlin.math.absoluteValue
import kotlin.math.sign

internal data class DMMs(
    val int: Int,
    val min: Int,
    val sec: Int,
    val sign: Int = 1,
    val milli: Int = 0
) {
    companion object {
        fun fromAsUnit(v: AstronomicalUnit): DMMs {
            val sign = v.value.sign.toInt()
            val va = v.value.absoluteValue
            val int = va.toInt()
            val minS = (va - int) * 60
            val min = minS.toInt()
            val secMilli = ((minS - min) * 60)
            val sec = secMilli.toInt()
            val milli = ((secMilli - sec) * 1000).toInt()
            return DMMs(int, min, sec, sign, milli)
        }

        fun fromLocalTime(v: LocalTime) = DMMs(v.hour, v.minute, v.second)
    }

    private fun toDecimal(): Double {
        val s = sec + milli / 1000.0
        val m = s / 60 + min
        val t = m / 60 + int
        return t * sign
    }

    fun toHour() = Hour(toDecimal())

    fun toDegree() = Degree(toDecimal())

    fun toLocalTime(): LocalTime {
        return if (toHour().correct24() > 24.hour) fromAsUnit(toDegree().correct360() / 15).toLocalTime()
        else LocalTime.of(int, min, sec)
    }
}