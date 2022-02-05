package com.ayaigi.astrcalc.lib.units

import java.time.LocalTime

data class Hour (override val value: Float) : AstronomicalUnit {

    override fun format(): Format = Format(this)

    fun average(v: Hour): Hour {
        return (v + this) / 2
    }

    override fun toString(): String {
        return "%.2f".format(value)
    }

    fun toDecimalDay() = value / 24

    companion object {
        fun fromLocalTime(v: LocalTime): Hour = DMMs.fromLocalTime(v).toHour()
        fun of(int: Int, min: Int, sec: Int, milli: Int = 0, sign: Int = 1) = DMMs(int, min, sec, sign, milli).toHour()
    }

    fun correct24(): Hour {
        var t = value
        while (t > 24f || t < 0f) {
            if (t > 24f) t -= 24f
            if (t < 0f) t += 24f
        }
        return Hour(t)
    }

    fun deg(): Degree = Degree(value * 15)

    operator fun div(i: Int): Hour = Hour(value / i)
    operator fun div(i: Float): Hour = Hour(value / i)
    operator fun times(i: Int): Hour = Hour(value * i)
    operator fun times(i: Float): Hour = Hour(value * i)

    operator fun minus(i: Hour): Hour = Hour(value - i.value)
    operator fun plus(i: Hour): Hour = Hour(value + i.value)

    fun toLocalTime(): LocalTime = DMMs.fromAsUnit(this).toLocalTime()

    operator fun compareTo(hour: Hour): Int = value.compareTo(hour.value)
}

val Float.hour: Hour
    get() = Hour(this)
val Int.hour: Hour
    get() = Hour(this.toFloat())