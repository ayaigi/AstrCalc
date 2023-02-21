package com.ayaigi.astrcalc.lib.units

import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.PI as PId
import kotlin.math.acos as mAcos
import kotlin.math.asin as mAsin
import kotlin.math.atan as mAtan
import kotlin.math.cos as mCos
import kotlin.math.sin as mSin
import kotlin.math.tan as mTan


data class Degree(override val value: Double) : AstronomicalUnit {

    fun correct360(): Degree {
        var d = value
        var big = d > 360
        var small = d < 0
        while (big || small) {
            if (big) d -= 360
            if (small) d += 360
            big = d > 360
            small = d < 0
        }
        return Degree(d)
    }

    companion object {
        fun fromRadians(r: Double): Degree = Degree(r * (180 / PI))
        private const val PI = PId
        fun aCos(v: Double): Degree = fromRadians(mAcos(v))
        fun aSin(v: Double): Degree = fromRadians(mAsin(v))
        fun aTan(v: Double): Degree = fromRadians(mAtan(v))
        fun aTan2(y: Double, x: Double): Degree {
            return Degree(atan2(y, x) * (180 / PI)).correct360()
        }
        fun of(int: Int, min: Int, sec: Int, milli: Int = 0, sign: Int = 1) = DMMs(int, min, sec, sign, milli).toDegree()
    }

    fun toRadians() = (PI / 180) * value

    fun cos(): Double = mCos(toRadians())
    fun sin(): Double = mSin(toRadians())
    fun tan(): Double = mTan(toRadians())

    override fun format(): Format = Format(this)

    override fun toString(): String {
        return format().Int2D
    }

    fun hour(): Hour = Hour(value / 15)

    operator fun div(i: Int): Degree = Degree(value / i)
    operator fun div(i: Double): Degree = Degree(value / i)
    operator fun times(i: Int): Degree = Degree(value * i)
    operator fun times(i: Double): Degree = Degree(value * i)

    operator fun minus(i: Degree): Degree = Degree(value - i.value)
    operator fun plus(i: Degree): Degree = Degree(value + i.value)

    fun abs(): Degree = Degree(value.absoluteValue)

    fun pow(i: Double) = value.pow(i).deg

    fun averageCircle(v: Degree): Degree {
        val x = v
        val div = (x - v).abs()
        return if (div > 180.deg) {
            when {
                x > v -> {
                    val toMax = 360.deg - x
                    (v + toMax) / 2
                }
                x < v -> {
                    val toMax = 360.deg - v
                    (x + toMax) / 2
                }
                else -> throw Exception("invalid")
            }
        } else {
            (x + v) / 2
        }
    }

    fun average(v: Degree): Degree {
        return (v + this) / 2
    }

    operator fun compareTo(degree: Degree): Int = value.compareTo(degree.value)

}


internal val Double.deg: Degree
    get() = Degree(this)
internal val Int.deg: Degree
    get() = Degree(this.toDouble())