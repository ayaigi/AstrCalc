package com.ayaigi.astrcalc.coorsytems

import com.ayaigi.astrcalc.units.invalid
import java.time.LocalTime
import kotlin.math.roundToInt

data class Distance private constructor(private val value: Float) { //value in meter
    companion object {
        /** in m*/
        private const val earthRadii = 6_371_009f
        /** in m*/
        private const val AU = 149_597_870_700f
        fun fromMeter(v: Double) = Distance(v.toFloat())
        fun fromMeter(v: Int) = Distance(v.toFloat())
        fun fromMeter(v: Float) = Distance(v)
        fun fromAU(v: Double) = Distance((v * AU).toFloat())
        fun fromAU(v: Int) = Distance((v * AU))
        fun fromAU(v: Float) = Distance((v * AU))
        fun fromKm(v: Double) = Distance((v * 1000).toFloat())
        fun fromKm(v: Int) = Distance((v * 1000).toFloat())
        fun fromKm(v: Float) = Distance((v * 1000))
        fun fromEarthRadii(v: Double) = Distance((v * earthRadii).toFloat())
        fun fromEarthRadii(v: Int) = Distance((v * earthRadii))
        fun fromEarthRadii(v: Float) = Distance((v * earthRadii))
    }

    fun toMeter() = value
    fun toAU() = value / AU
    fun toKm() = value / 1000.0f
    fun toEarthRadii() = value / earthRadii
    override fun toString(): String {
        val au = toAU()
        return when {
            au < 0.1f -> {
                toKm().roundToInt().toString() + "km"
            }
            au < 1.0f -> {
                "0." + (au * 10).roundToInt().toString() + "AU"
            }
            au >= 10f -> {
                (au.roundToInt()).toString() + "AU"
            }
            au >= 1.0f -> {
                (au * 10).roundToInt().toString().toCharArray().joinToString(".") + "AU"
            }
            else -> invalid(au.toString())
        }
    }

    fun lightTime() : LocalTime {
        return LocalTime.ofSecondOfDay((value / 3E8).toLong())
    }

    operator fun div(v: Float): Distance = Distance(value / v)
    operator fun times(v: Float): Distance = Distance(value * v)

    operator fun div(v: Distance): Float = value / v.value
}