package com.ayaigi.astrcalc.units

import java.time.LocalTime
import kotlin.math.absoluteValue
import kotlin.math.sign

internal data class DMMs (val int: Int, val min: Int, val sec: Int, val sign: Int = 1){
    companion object {
        fun fromAsUnit(v: AstronomicalUnit): DMMs {
            val sign = v.value.sign.toInt()
            val va = v.value.absoluteValue
            val int = va.toInt()
            val minS = (va - int) * 60
            val min = minS.toInt()
            val sec = ((minS - min) * 60).toInt()
            return DMMs(int, min, sec, sign)
        }
        fun fromLocalTime(v: LocalTime) = DMMs(v.hour, v.minute, v.second)
    }
    fun toDecimalH(): Hour {
        val s = sec
        val m = s / 60f + min
        val t = m / 60f + int
        return Hour(t) * sign
    }
    fun toDecimalD(): Degree {
        val s = sec
        val m = s / 60f + min
        val t = m / 60f + int
        return Degree(t) * sign
    }
    fun toLocalTime(): LocalTime {
        return if(toDecimalH().correct24() > 24f.hour) fromAsUnit(toDecimalD().correct360() / 15f).toLocalTime()
        else LocalTime.of(int, min, sec)

    }
}