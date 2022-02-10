package com.ayaigi.astrcalc.lib.units

import java.util.*
import kotlin.math.roundToInt

/**
 * IntDDMin -> 56..32
 * Int2D -> 56.32
 * INT_Z_MINm -> 56° 37m
 * IntZMin_ -> 56° 37' - 12h 42'
 * IntZMin_Sec__ -> 56° 37' 43'' - 12h 42' 32''
 * OneUnit -> 56.2°; 23.4'; 42''
 */
class Format(val unit: AstronomicalUnit, type: FormatType? = null) {
    companion object {
        enum class FormatType {
            Time,
            Degree,
            None
        }
    }

    private val dmms = DMMs.fromAsUnit(unit)
    private var type: FormatType = run {
        type ?: if (unit is Degree) FormatType.Degree
        else FormatType.Time
    }
    private val signChar: Char? = if (dmms.sign == -1) '-' else null

    /**
     * Example: 56..32
     */
    val IntDDMin
        get() = "${dmms.int}..${dmms.min}"

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
     * Example: 56° 37' 43'' - 12h 42' 32''
     */
    val IntZMin_Sec__: String
        get() = run {
            val s = "${dmms.int}* ${dmms.min}' ${dmms.sec}''"
            format(s)
        }

    /**
     * Example: 56.2°; 23.4'; 42''
     */
    val OneUnit: String
        get() = run {
            println(dmms)
            val s: String = when {
                dmms.int > (24 * 30) -> {
                    (dmms.int / (24 * 30)).toString() + "d"
                }
                dmms.int > (24 * 10) -> {
                    (dmms.int / 24).toString() + "d"
                }
                dmms.int > (24) -> {
                    "%.1f".format(dmms.int / 24f) + "d"
                }
                dmms.int > 10 -> {
                    dmms.int.toString() + "*"
                }
                dmms.int >= 1 -> {
                    "%.1f".format((dmms.int + dmms.min / 60f)) + "*"
                }
                dmms.min > 10 -> {
                    dmms.min.toString() + "'"
                }
                dmms.min >= 1 -> {
                    "%.1f".format((dmms.min + dmms.sec / 60f)) + "'"
                }
                dmms.sec >= 10 -> {
                    dmms.sec.toString() + "''"
                }
                dmms.sec >= 1 -> {
                    "%.1f".format((dmms.sec + dmms.milli / 1000f)) + "''"
                }
                dmms.milli >= 100 -> {
                    (dmms.milli / 100f).roundToInt().toString() + "00" + "ms"
                }
                dmms.milli >= 10 -> {
                    (dmms.milli / 10f).roundToInt().toString() + "0" + "ms"
                }
                dmms.milli >= 1 -> {
                    dmms.milli.toString() + "ms"
                }
                else -> throw Exception("invalid: $unit")
            }
            format(s)
        }


    private fun format(s: String): String {
        val t = when (type) {
            FormatType.Time -> s.replace('*', 'h')
            FormatType.Degree -> s.replace('*', '°')
            FormatType.None -> s.dropWhile { it == '*' }
        }
        return if (signChar != null) "-$t"
        else t
    }
}