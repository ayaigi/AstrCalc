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


data class Degree(override val value: Float) : AstronomicalUnit {

    fun correct360(): Degree {
        var d = value
        while (d > 360f || d < 0f) {
            if (d > 360f) d -= 360f
            if (d < 0f) d += 360f
        }
        return Degree(d)
    }

    companion object {
        fun fromRadians(r: Float): Degree = Degree(r * (180 / PI))
        private const val PI = PId.toFloat()
        fun aCos(v: Float): Degree = fromRadians(mAcos(v))
        fun aSin(v: Float): Degree = fromRadians(mAsin(v))
        fun aTan(v: Float): Degree = fromRadians(mAtan(v))
        fun aTan2(y: Float, x: Float): Degree {
            return Degree(atan2(y, x) * (180 / PI)).correct360()
        }

    }

    internal fun toRad() = (PI / 180) * value

    fun cos(): Float = mCos(toRad())
    fun sin(): Float = mSin(toRad())
    fun tan(): Float = mTan(toRad())

    override fun format(): Format = Format(this)

    override fun toString(): String {
        return format().Int2D
    }

    fun hour(): Hour = Hour(value / 15)

    operator fun div(i: Int): Degree = Degree(value / i)
    operator fun div(i: Float): Degree = Degree(value / i)
    operator fun times(i: Int): Degree = Degree(value * i)
    operator fun times(i: Float): Degree = Degree(value * i)

    operator fun minus(i: Degree): Degree = Degree(value - i.value)
    operator fun plus(i: Degree): Degree = Degree(value + i.value)

    fun abs(): Degree = Degree(value.absoluteValue)

    fun pow(i: Float) = value.pow(i).deg

    fun averageCircle(v: Degree): Degree {
        val x = v
        val div = (x - v).abs()
        return if (div > 180.deg) {
            when {
                x > v -> {
                    val toMax = 360f.deg - x
                    (v + toMax) / 2
                }
                x < v -> {
                    val toMax = 360f.deg - v
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


internal val Float.deg: Degree
    get() = Degree(this)
internal val Int.deg: Degree
    get() = Degree(this.toFloat())