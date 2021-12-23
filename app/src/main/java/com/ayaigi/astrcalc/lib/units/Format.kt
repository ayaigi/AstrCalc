package com.ayaigi.astrcalc.lib.units

import java.util.*

class Format(val unit: AstronomicalUnit, type: FormatType = FormatType.FromUnit) {
    companion object {
        enum class FormatType {
            FromUnit,
            Time,
            Degree,
            None
        }
    }

    private val dmms = DMMs.fromAsUnit(unit)
    private val type: FormatType = run {
        if (type == FormatType.FromUnit) {
            if (unit is Degree) FormatType.Degree
            else FormatType.Time
        } else type
    }
    private val signChar: Char? = if (dmms.sign == -1) '-' else null

    /**
     * Example: 56.32
     */
    val Int2D: String
        get() = "%.2f".format(Locale.ENGLISH, unit.value)

    /**
     * Example: 56° 37m
     */
    val INT_Z_MINm: String
        get() = run {
            val s = "${dmms.int}* ${dmms.min}min"
            format(s)
        }

    /**
     * Example: 56° 37' - 12h 42'
     */
    val IntZMin_: String
        get() = run {
            val s = "${dmms.int}* ${dmms.min}'"
            format(s)
        }

    /**
     * Example: 56° 37' - 12h 42'
     */
    val IntZMin_Sec__: String
        get() = run {
            val s = "${dmms.int}* ${dmms.min}' ${dmms.sec}''"
            format(s)
        }


    private fun format(s: String): String {
        val t = when (type) {
            FormatType.Time -> s.replace('*', 'h')
            FormatType.Degree -> s.replace('*', '°')
            FormatType.None -> s.dropWhile { it == '*' }
            else -> throw Exception("invalid Type")
        }
        return if (signChar != null) "-$t"
        else t
    }
}