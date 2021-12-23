package com.ayaigi.astrcalc.lib.coorsytems

import com.ayaigi.astrcalc.lib.units.julianDay
import com.ayaigi.astrcalc.lib.units.Degree
import java.time.Instant
import kotlin.math.pow

data class EclipticSystem (val Lambda: Degree, val Betta: Degree){
    companion object {
        internal fun epsilon(instant: Instant): Degree {
            val t = (instant.julianDay() - 2415020) / 36525.0
            val delta = Degree(((46.845f * t + 0.0059f * t.pow(2) - 0.00181f * t.pow(3)) / 3600).toFloat())
            return Degree(23.452294f) - delta
        }
    }
    fun toEquatorialSys(instant: Instant): EquatorialSystem {
        val epsilon = epsilon(instant)
        val declination = run {
            val p1 = Betta.sin() * epsilon.cos()
            val p2 = Betta.cos() * epsilon.sin() * Lambda.sin()
            Degree.aSin(p1 + p2)
        }
        val rightAscension = run {
            val y = run {
                val p1 = Lambda.sin() * epsilon.cos()
                val p2 = Betta.tan() * epsilon.sin()
                p1 - p2
            }
            val x = Lambda.cos()
            Degree.aTan2(y, x).hour()
        }
        return EquatorialSystem(rightAscension, declination)
    }
}
