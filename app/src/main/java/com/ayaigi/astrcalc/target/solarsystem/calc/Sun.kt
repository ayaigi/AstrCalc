package com.ayaigi.astrcalc.target.solarsystem.calc

import com.ayaigi.astrcalc.core.GeoInstant
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
        internal val epsilonG = 278.83354.deg
        internal val omegaG = 282.596403.deg
        internal val rho = 0.016718
        internal val r0 = com.ayaigi.astrcalc.lib.coorsytems.Distance.fromAU(1)
        internal val Theta0 = 0.533128.deg

        /**
         * E, v
         */
        fun routineR2(M: Degree, rho: Double): Pair<Degree, Degree> {
            val epsilon = 10E-6
            val M = M.toRadians()// * (180 / PI)
            var E = M
            var decli = E - rho * sin(E) - M
            var delta: Double

            var counter = 0
            while (abs(decli) > epsilon) {
                decli = E - rho * sin(E) - M
                delta = decli / (1 - rho * cos(E))
                E -= delta
                counter++
            }
            //println("iterations: $counter")
            val v = atan((((1 + rho) / (1 - rho)).pow(0.5)) * tan(E / 2)) * 2
            val vDeg = Degree.fromRadians(v)
            val eDeg = Degree.fromRadians(E)
            return Pair(eDeg, vDeg)
        }
    }

    internal val positionValues: SunValues = positionValues()

    override val ecliptic: EclipticSystem =
        EclipticSystem(positionValues.Lambda, 0.deg)

    override val position: EquatorialSystem =
        ecliptic.toEquatorialSys(instant)

    private val paraF: Double = run {
        val v = positionValues.v.correct360()
        val y = 1 + rho * v.cos()
        val x = 1 - rho.pow(2)
        val e = y / x
        e
    }

    override val distance: Distance = run {
        r0
        paraF
        r0 / paraF
    }

    override fun angularSize(): Degree {
        return Theta0 * paraF
    }

    override fun phase(): SolarPhase = SolarPhase(1.0, true)


    /**
     * SunValues M, v, Lambda
     */
    private fun positionValues(): SunValues {
        val D = instant.epochDay80()
        val N = Degree((360.0 / 365.2422) * D).correct360()
        val M = (N + epsilonG - omegaG).correct360()
        val R2 = routineR2(M, rho)
        val eR = R2.first.toRadians()
        val v = R2.second
        val Lambda = (v + omegaG).correct360()
        return SunValues(M, v, Lambda)
    }

    data class SunValues(val M: Degree, val v: Degree, val Lambda: Degree)

    override fun riseAndSet(
        gI: GeoInstant
    ): EquatorialSystem.RiseAndSet {

        val r = distance.toEarthRadii()
        val d = instant.startOfDay().plusDays(0.5f)

        var d1 = d
        var posi1 = Sun(d1).position
        var h1 = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.tan())
        var eqPa1 = posi1.correctParallax(r, gI.lat, h1, gI.altitude)
        var riSe1 = eqPa1.riseAndSet(gI)
        var hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
        for (i in 0..5){
            posi1 = Sun(d1).position
            h1 = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.tan())
            eqPa1 = posi1.correctParallax(r, gI.lat, h1, gI.altitude)
            riSe1 = eqPa1.riseAndSet(gI)
            d1 = gI.instant.toLocalDate().atTime(riSe1.STr.convertToLocalTime(gI)).toInstant()
            hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
            true
        }


        var d2 = d
        var posi2 = Sun(d2).position
        var h2 = Degree.aCos(-(gI.lat.tan()) * posi2.Declination.tan())
        var eqPa2 = posi2.correctParallax(r, gI.lat, h2, gI.altitude)
        var riSe2 = eqPa2.riseAndSet(gI)
        var hori2 = posi2.toHorizonSys(gI.lat, riSe2.STs)
        for (i in 0..5){
            posi2 = Sun(d2).position
            h2 = Degree.aCos(-(gI.lat.tan()) * posi2.Declination.tan())
            eqPa2 = posi2.correctParallax(r, gI.lat, h2, gI.altitude)
            riSe2 = eqPa2.riseAndSet(gI)
            d2 = gI.instant.toLocalDate().atTime(riSe2.STs.convertToLocalTime(gI)).toInstant()
            hori2 = posi2.toHorizonSys(gI.lat, riSe1.STs)
            true
        }

        val rise = riSe1.STr
        val set = riSe2.STs
        val Ar = riSe1.Ar
        val As = (riSe2.As)
        val hA = riSe1.hA.average(riSe2.hA)

        val riseL = riSe1.STr.convertToLocalTime(gI)
        val setL = riSe2.STs.convertToLocalTime(gI)

        return EquatorialSystem.RiseAndSet(rise, set, Ar, As, hA)
    }

    private fun riseAndSet_backup2(
        gI: GeoInstant
    ): EquatorialSystem.RiseAndSet {

        val r = distance.toEarthRadii()

        var d1 = instant.startOfDay()
        var d2 = d1.plusDays(1)
        var posi1 = Sun(d1).position
        var posi2 = Sun(d2).position
        var h = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.average(posi2.Declination).tan())
        var eqPa1 = posi1.correctParallax(r, gI.lat, h, gI.altitude)
        var eqPa2 = posi2.correctParallax(r, gI.lat, h, gI.altitude)
        var riSe1 = eqPa1.riseAndSet(gI)
        var riSe2 = eqPa2.riseAndSet(gI)
        var hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
        var hori2 = posi2.toHorizonSys(gI.lat, riSe2.STs)
        for (i in 0..5){
            posi1 = Sun(d1).position
            posi2 = Sun(d2).position
            h = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.average(posi2.Declination).tan())
            eqPa1 = posi1.correctParallax(r, gI.lat, h, gI.altitude)
            eqPa2 = posi2.correctParallax(r, gI.lat, h, gI.altitude)
            riSe1 = eqPa1.riseAndSet(gI)
            riSe2 = eqPa2.riseAndSet(gI)
            d1 = gI.instant.toLocalDate().atTime(riSe1.STr.convertToLocalTime(gI)).toInstant()
            d2 = gI.instant.toLocalDate().atTime(riSe2.STs.convertToLocalTime(gI)).toInstant()
            hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
            hori2 = posi2.toHorizonSys(gI.lat, riSe1.STs)
            true
        }


        val rise = riSe1.STr
        val set = riSe2.STs
        val Ar = riSe1.Ar
        val As = (riSe2.As)
        val hA = riSe1.hA.average(riSe2.hA)

        val riseL = riSe1.STr.convertToLocalTime(gI)
        val setL = riSe2.STs.convertToLocalTime(gI)

        return EquatorialSystem.RiseAndSet(rise, set, Ar, As, hA)
    }

    private fun riseAndSet_backup(
        gI: GeoInstant
    ): EquatorialSystem.RiseAndSet {
        val posi0 = run {
            val instant = instant.startOfDay()
            Sun(instant).position
        }
        val posi24 = run {
            val instant = instant.plusDays(1).startOfDay()
            Sun(instant).position
        }
        val riSe0 = posi0.riseAndSet(gI)
        val riSe24 = posi24.riseAndSet(gI)

        val AziR = riSe0.Ar.averageCircle(riSe24.Ar)
        val AziS = riSe0.As.averageCircle(riSe24.As)

        val tR = run {
            val p1 = riSe0.STr.tH() * 24.07
            val p2 = 24.07.hour + riSe0.STr.tH() - riSe24.STr.tH()
            Hour(p1.value / p2.value)
        }
        val tS = run {
            val p1 = riSe0.STs.tH() * 24.07
            val p2 = 24.07.hour + riSe0.STs.tH() - riSe24.STs.tH()
            Hour(p1.value / p2.value)
        }
        val delta = run {
            0.0.deg
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