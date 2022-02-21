package com.ayaigi.astrcalc.target.solarsystem.calc

import com.ayaigi.astrcalc.lib.cons.PI
import com.ayaigi.astrcalc.lib.coorsytems.Distance
import com.ayaigi.astrcalc.lib.coorsytems.EclipticSystem
import com.ayaigi.astrcalc.lib.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.lib.units.*
import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import com.ayaigi.astrcalc.target.solarsystem.SolarPhase
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemCalc
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import java.time.Instant
import kotlin.math.*

class Sun(override val instant: Instant) : SolarSystemCalc {

    override val id: AstroTarget = SolarSystemTargets.Sun

    companion object {
        internal val epsilonG = 278.83354f.deg
        internal val omegaG = 282.596403f.deg
        internal val rho = 0.016718f
        internal val r0 = com.ayaigi.astrcalc.lib.coorsytems.Distance.fromAU(1)
        internal val Theta0 = 0.533128f.deg

        /**
         * E, v
         */
        fun routineR2(M: Degree, rho: Float): Pair<Degree, Degree> {
            val epsilon = 10E-6
            val M = M.toRadians()// * (180 / PI)
            var E = M
            var decli = E - rho * sin(E) - M
            var delta: Float

            var counter = 0
            while (abs(decli) > epsilon) {
                decli = E - rho * sin(E) - M
                delta = decli / (1 - rho * cos(E))
                E -= delta
                counter++
            }
            //println("iterations: $counter")
            val v = atan((((1 + rho) / (1 - rho)).pow(0.5f)) * tan(E / 2)) * 2
            return Pair(Degree.fromRadians(E), Degree.fromRadians(v))
        }
    }

    internal val positionValues: SunValues = positionValues()

    override val ecliptic: EclipticSystem =
        EclipticSystem(positionValues.Lambda, 0f.deg)

    override val position: EquatorialSystem =
        ecliptic.toEquatorialSys(instant)

    private val paraF: Float = run {
        val (_, v, _) = positionValues
        val y = 1 + rho * v.cos()
        val x = 1 - rho.pow(2)
        y / x
    }

    override val distance: Distance = r0 / paraF

    override fun angularSize(): Degree {
        return Theta0 * paraF
    }

    override fun phase(): SolarPhase = SolarPhase(1f, true)


    private fun positionValues(): SunValues {
        val D = instant.epochDay80()
        val N = Degree((360f / 365.2422f) * D).correct360()
        val M = (N + epsilonG - omegaG).correct360()
        val R2 = routineR2(M, rho)
        val eR = R2.first.toRadians()
        val v = R2.second
        val Lambda = (v + omegaG).correct360()
        return SunValues(M, v, Lambda)
    }

    data class SunValues(val M: Degree, val v: Degree, val Lambda: Degree)

    override fun riseAndSet(
        lat: Degree,
        lon: Degree,
        altitude: Float
    ): EquatorialSystem.RiseAndSet {
        val posi0 = run {
            val instant = instant.startOfDay()
            Sun(instant).position
        }
        val posi24 = run {
            val instant = instant.plusDays(1).startOfDay()
            Sun(instant).position
        }
        val riSe0 = posi0.riseAndSet(lat, lon)
        val riSe24 = posi24.riseAndSet(lat, lon)

        val AziR = riSe0.Ar.averageCircle(riSe24.Ar)
        val AziS = riSe0.As.averageCircle(riSe24.As)

        val tR = run {
            val p1 = riSe0.STr.tH() * 24.07f
            val p2 = 24.07f.hour + riSe0.STr.tH() - riSe24.STr.tH()
            Hour(p1.value / p2.value)
        }
        val tS = run {
            val p1 = riSe0.STs.tH() * 24.07f
            val p2 = 24.07f.hour + riSe0.STs.tH() - riSe24.STs.tH()
            Hour(p1.value / p2.value)
        }
        val delta = run {
            0.0f.deg
        }.hour()
        val hA = riSe0.hA.deg().averageCircle(riSe24.hA.deg())
        val STr = SiderealTime(tR - delta)
        val STs = SiderealTime(tS - delta)
        return EquatorialSystem.RiseAndSet(
            STr,
            STs,
            AziR,
            AziS,
            hA.hour()
        )
    }
}