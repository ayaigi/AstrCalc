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
import kotlin.math.pow

class Moon(override val instant: Instant) : SolarSystemCalc {
    override val id: AstroTarget = SolarSystemTargets.Moon

    companion object {
        /**Moon's mean longitude at the epoch - in Deg*/
        val l0 = 64.975464.deg

        /**Mean longitude of the perigee at the epoch - in Deg*/
        val P0 = 349.383063.deg

        /**Mean longitude of the node at the epoch - in Deg*/
        val N0 = 151.950429.deg

        /**inclination of the Moon's Orbit - in Deg*/
        val i = 5.145396.deg

        /**Eccentricity of the Moon's Orbit*/
        val e = 0.0549

        /**Semi-major axis of Moon's orbit - in km*/
        val a = Distance.fromKm(384401)

        /**Moon's angular size at distance "a" from earth - in Deg*/
        val Theta0 = 0.5181.deg

        /**Parallax at distance "a" from earth - in Deg*/
        val PI0 = 0.9507.deg
    }

    //override val angularSize: Degree by lazy { angularSize() }

    override fun angularSize(): Degree {
        return Theta0 / disA
    }

    //val horizontalParallax: Degree by lazy { horizontalParallax() }

    fun horizontalParallax(): Degree {
        return P0 / disA
    }

    override fun riseAndSet(
        gI: GeoInstant
    ): EquatorialSystem.RiseAndSet {

        val r = distance.toEarthRadii()
        val d = instant.startOfDay().plusDays(0.5f)

        var d1 = d
        var posi1 = Moon(d1).position
        var h1 = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.tan())
        var eqPa1 = posi1.correctParallax(r, gI.lat, h1, gI.altitude)
        var riSe1 = eqPa1.riseAndSet(gI)
        var hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
        for (i in 0..3){
            posi1 = Moon(d1).position
            h1 = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.tan())
            eqPa1 = posi1.correctParallax(r, gI.lat, h1, gI.altitude)
            riSe1 = eqPa1.riseAndSet(gI)
            d1 = gI.instant.toLocalDate().atTime(riSe1.STr.convertToLocalTime(gI)).toInstant()
            hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
            true
        }


        var d2 = d
        var posi2 = Moon(d2).position
        var h2 = Degree.aCos(-(gI.lat.tan()) * posi2.Declination.tan())
        var eqPa2 = posi2.correctParallax(r, gI.lat, h2, gI.altitude)
        var riSe2 = eqPa2.riseAndSet(gI)
        var hori2 = posi2.toHorizonSys(gI.lat, riSe2.STs)
        for (i in 0..3){
            posi2 = Moon(d2).position
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
        var posi1 = Moon(d1).position
        var posi2 = Moon(d2).position
        var h = Degree.aCos(-(gI.lat.tan()) * posi1.Declination.average(posi2.Declination).tan())
        var eqPa1 = posi1.correctParallax(r, gI.lat, h, gI.altitude)
        var eqPa2 = posi2.correctParallax(r, gI.lat, h, gI.altitude)
        var riSe1 = eqPa1.riseAndSet(gI)
        var riSe2 = eqPa2.riseAndSet(gI)
        var hori1 = posi1.toHorizonSys(gI.lat, riSe1.STr)
        var hori2 = posi2.toHorizonSys(gI.lat, riSe2.STs)
        for (i in 0..3){
            posi1 = Moon(d1).position
            posi2 = Moon(d2).position
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

    private val positionValues: MoonValues = positionValues()

    /** Distance as Fraction of a */
    private val disA: Double = disA()

    override val distance: Distance = a * disA

    override val ecliptic: EclipticSystem = run {
        val (_, _, l3, N2, _) = positionValues

        val Lambda = run {
            val y = run {
                val p1 = (l3 - N2).sin()
                val p2 = i.cos()
                p1 * p2
            }
            val x = (l3 - N2).cos()
            Degree.aTan2(y, x) + N2
        }
        val Betta = run {
            val p1 = (l3 - N2).sin()
            val p2 = i.sin()
            Degree.aSin(p1 * p2)
        }
        EclipticSystem(Lambda, Betta)
    }
    override val position: EquatorialSystem = ecliptic.toEquatorialSys(instant)

    private fun disA(): Double {
        val y = (1 - e.pow(2))
        val x = run {
            val (Mm, _, _, _, Ec) = positionValues
            val p1 = Mm + Ec
            val p2 = p1.cos() * e
            1 + p2
        }
        return (y / x)
    }

    override fun phase(): SolarPhase {
        fun F(Lambda: Degree): Double {
            val l = positionValues.l
            val D = l - Lambda
            return 0.5 * (1 - D.cos())
        }

        val F0: Double = F(Sun(instant).ecliptic.Lambda)
        val F1: Double = F(Sun(instant.plusDays(1)).ecliptic.Lambda)
        return SolarPhase(F0, F0 > F1)
    }

    private fun positionValues(): MoonValues {
        val sun = Sun(instant).positionValues
        val D = instant.epochDay80()

        val l = (Degree(13.1763966) * D + l0).correct360()
        val Mm = (l - Degree(0.1114041) * D - P0).correct360()
        val N = (N0 - Degree(0.0529539) * D).correct360()
        val Ev = run {
            val C = l - sun.Lambda
            val p1 = C * 2 - Mm
            (p1.sin() * 1.2739f).deg
        }
        val Ae = (sun.M.sin() * 0.1858).deg
        val A3 = (sun.M.sin() * 0.37).deg
        val Mm2 = Mm + Ev - Ae - A3
        val Ec = (Mm2.sin() * 6.2886).deg
        val A4 = ((Mm2 * 2).sin() * 0.214).deg
        val l2 = l + Ev + Ec - Ae + A4
        val V = run {
            val C = (l2 - sun.Lambda) * 2
            (C.sin() * 0.6583).deg
        }
        val l3 = l2 + V
        val N2 = run {
            val p1 = (sun.M.sin() * 0.16).deg
            N - p1
        }
        return MoonValues(Mm2, Ev, l3, N2, Ec)
    }

    data class MoonValues(
        val Mm: Degree,
        val Ev: Degree,
        val l: Degree,
        val N: Degree,
        val Ec: Degree
    )

/*
    private fun riseAndSet_backup(
        lat: Degree,
        lon: Degree,
        altitude: Double
    ): EquatorialSystem.RiseAndSet {

        val r = distance.toEarthRadii()
        val date0 = instant.startOfDay()
        val date24 = instant.plusDays(0.5f).startOfDay()

        val posiEc0 = Moon(date0).position
        val posiEq0 = posiEc0
        val posiEc24 = Moon(date24).position
        val posiEq24 = posiEc24

        val H =
            Degree.aCos(-(lat.tan()) * posiEq0.Declination.average(posiEq24.Declination).tan())

        val eqPa0 = posiEq0.correctParallax(r, lat, H, altitude)
        val eqPa24 = posiEq24.correctParallax(r, lat, H, altitude)

        val riSe0 = eqPa0.riseAndSet(lat, lon)
        val riSe24 = eqPa24.riseAndSet(lat, lon)

        val rise =
            SiderealTime((riSe0.STr.tH() * 12.03) / (12.03.hour + riSe0.STr.tH() - riSe24.STr.tH()).value)
        val set =
            SiderealTime((riSe0.STs.tH() * 12.03) / (12.03.hour + riSe0.STs.tH() - riSe24.STs.tH()).value)
        val Ar = riSe0.Ar.average(riSe24.Ar)
        val As = riSe0.As.average(riSe24.As)
        val hA = riSe0.hA.average(riSe24.hA)

        return EquatorialSystem.RiseAndSet(rise, set, Ar, As, hA)
    }

 */
}