package com.ayaigi.astrcalc.solarsystem

import com.ayaigi.astrcalc.units.*
import java.time.Instant
import kotlin.math.pow

class Moon(override val instant: Instant) : SolarSystemCalc {
    override val id: AstroTarget = SolarSystemTargets.Moon
    companion object {
        /**Moon's mean longitude at the epoch - in Deg*/
        val l0 = 64.975464f.deg

        /**Mean longitude of the perigee at the epoch - in Deg*/
        val P0 = 349.383063f.deg

        /**Mean longitude of the node at the epoch - in Deg*/
        val N0 = 151.950429f.deg

        /**inclination of the Moon's Orbit - in Deg*/
        val i = 5.145396f.deg

        /**Eccentricity of the Moon's Orbit*/
        val e = 0.0549f

        /**Semi-major axis of Moon's orbit - in km*/
        val a = com.ayaigi.astrcalc.coorsytems.Distance.fromKm(384401)

        /**Moon's angular size at distance "a" from earth - in Deg*/
        val Theta0 = 0.5181f.deg

        /**Parallax at distance "a" from earth - in Deg*/
        val PI0 = 0.9507f.deg
    }

    override val angularSize: Degree by lazy {
        angularSize()
    }

    private fun angularSize(): Degree {
        return Theta0 / disA
    }

    val horizontalParallax: Degree by lazy {
        horizontalParallax()
    }

    private fun horizontalParallax(): Degree {
        return P0 / disA
    }

    override fun riseAndSet(
        lat: Degree,
        lon: Degree,
        altitude: Float
    ): com.ayaigi.astrcalc.coorsytems.EquatorialSystem.RiseAndSet {
        val (riSe0, riSe24) = run {
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

            Pair(riSe0, riSe24)
        }
        val rise =
            SiderealTime((riSe0.STr.tH() * 12.03f) / (12.03f.hour + riSe0.STr.tH() - riSe24.STr.tH()).value)
        val set =
            SiderealTime((riSe0.STs.tH() * 12.03f) / (12.03f.hour + riSe0.STs.tH() - riSe24.STs.tH()).value)
        val Ar = riSe0.Ar.average(riSe24.Ar)
        val As = riSe0.As.average(riSe24.As)
        val hA = riSe0.hA.average(riSe24.hA)

        return com.ayaigi.astrcalc.coorsytems.EquatorialSystem.RiseAndSet(rise, set, Ar, As, hA)
    }

    override val position: com.ayaigi.astrcalc.coorsytems.EquatorialSystem by lazy {
        position()
    }
    override val distance: com.ayaigi.astrcalc.coorsytems.Distance by lazy {
        distance()
    }

    private fun distance(): com.ayaigi.astrcalc.coorsytems.Distance = a * disA

    /** Distance as Fraction of a */
    private val disA: Float by lazy {
        disA()
    }

    private fun disA(): Float {
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
        fun F(Lambda: Degree): Float {
            val l = positionValues.l
            val D = l - Lambda
            return 0.5f * (1 - D.cos())
        }

        val F0: Float = F(Sun(instant).ecliptic.Lambda)
        val F1: Float = F(Sun(instant.plusDays(1)).ecliptic.Lambda)
        return SolarPhase(F0, F0 > F1)
    }

    private fun position(): com.ayaigi.astrcalc.coorsytems.EquatorialSystem =
        ecliptic().toEquatorialSys(instant)


    private fun ecliptic(): com.ayaigi.astrcalc.coorsytems.EclipticSystem {
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
        return com.ayaigi.astrcalc.coorsytems.EclipticSystem(Lambda, Betta)
    }

    val positionValues: MoonValues by lazy {
        positionValues()
    }

    private fun positionValues(): MoonValues {
        val sun = Sun(instant).positionValues
        val D = instant.epochDay80()

        val l = (Degree(13.1763966f) * D + l0).correct360()
        val Mm = (l - Degree(0.1114041f) * D - P0).correct360()
        val N = (N0 - Degree(0.0529539f) * D).correct360()
        val Ev = run {
            val C = l - sun.Lambda
            val p1 = C * 2 - Mm
            (p1.sin() * 1.2739f).deg
        }
        val Ae = (sun.M.sin() * 0.1858f).deg
        val A3 = (sun.M.sin() * 0.37f).deg
        val Mm2 = Mm + Ev - Ae - A3
        val Ec = (Mm2.sin() * 6.2886f).deg
        val A4 = ((Mm2 * 2).sin() * 0.214f).deg
        val l2 = l + Ev + Ec - Ae + A4
        val V = run {
            val C = (l2 - sun.Lambda) * 2
            (C.sin() * 0.6583f).deg
        }
        val l3 = l2 + V
        val N2 = run {
            val p1 = (sun.M.sin() * 0.16f).deg
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

}