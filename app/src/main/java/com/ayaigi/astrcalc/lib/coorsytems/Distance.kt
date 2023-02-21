package com.ayaigi.astrcalc.lib.coorsytems

import com.ayaigi.astrcalc.lib.units.Hour
import com.ayaigi.astrcalc.lib.units.invalid
import kotlin.math.roundToInt

data class Distance private constructor(private val value: Double) { //value in meter
    companion object {
        /** in m*/
        private const val earthRadii = 6_371_009
        /** in m*/
        private const val AU = 149_597_870_700.0
        fun fromMeter(v: Double) = Distance(v)
        fun fromMeter(v: Int) = Distance(v.toDouble())
        fun fromAU(v: Double) = Distance((v * AU))
        fun fromAU(v: Int) = Distance((v * AU))
        fun fromAU(v: Float) = Distance((v * AU))
        fun fromKm(v: Double) = Distance((v * 1000))
        fun fromKm(v: Int) = Distance((v * 1000.0))
        fun fromKm(v: Float) = Distance((v * 1000.0))
        fun fromEarthRadii(v: Double) = Distance(v * earthRadii)
        fun fromEarthRadii(v: Int) = Distance((v * earthRadii).toDouble())
    }

    fun toMeter() = value
    fun toAU() = value / AU
    fun toKm() = value / 1000.0f
    fun toEarthRadii() = value / earthRadii
    override fun toString(): String {
        val au = toAU()
        return when {
            au < 0.1 -> {
                toKm().roundToInt().toString() + "km"
            }
            au < 1.0 -> {
                "0." + (au * 100).roundToInt().toString() + "AU"
            }
            au >= 10 -> {
                (au.roundToInt()).toString() + "AU"
            }
            au >= 1.0 -> {
                (au * 10).roundToInt().toString().toCharArray().joinToString(".") + "AU"
            }
            else -> invalid(au.toString())
        }
    }

    fun lightTime() : Hour {
        return Hour(value / 3E8)
    }

    operator fun div(v: Double): Distance = Distance(value / v)
    operator fun times(v: Double): Distance = Distance(value * v)

    operator fun div(v: Distance): Double = value / v.value
}