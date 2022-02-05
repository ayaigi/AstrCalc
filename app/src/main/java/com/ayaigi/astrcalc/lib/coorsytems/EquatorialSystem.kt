package com.ayaigi.astrcalc.lib.coorsytems

import com.ayaigi.astrcalc.core.Observer
import com.ayaigi.astrcalc.lib.units.*
import java.time.Instant

data class EquatorialSystem(val RightAscension: Hour, val Declination: Degree) {
    fun toHorizonSys(lat: Degree, ST: SiderealTime): HorizontalSystem {
        val hA = (ST.tH() - RightAscension).deg()
        val altitude = run {
            val t1 = Declination.sin() * lat.sin()
            val t2 = Declination.cos() * lat.cos() * hA.cos()
            Degree.aSin(t1 + t2)
        }
        var azimuth = run {
            val o = Declination.sin() - (lat.sin() * altitude.sin())
            val u = lat.cos() * altitude.cos()
            Degree.aCos(o / u)
        }
        if (hA.sin() > 0) {
            azimuth = 360.deg - azimuth
        }
        return HorizontalSystem(azimuth, altitude)
    }

    fun toEclipticSys(instant: Instant): EclipticSystem {
        val rightAscension = RightAscension.deg()
        val epsilon = EclipticSystem.epsilon(instant)

        val Betta = run {
            val p1 = Declination.sin() * epsilon.cos()
            val p2 = Declination.cos() * epsilon.sin() * rightAscension.sin()
            Degree.aSin(p1 - p2)
        }
        val Lambda = run {
            val y = run {
                val p1 = rightAscension.sin() * epsilon.cos()
                val p2 = Declination.tan() * epsilon.sin()
                p1 + p2
            }
            val x = rightAscension.cos()
            Degree.aTan2(y, x)
        }
        return EclipticSystem(Lambda, Betta)
    }

    fun riseAndSet(lat: Degree, lon: Degree): RiseAndSet {
        val rightAscension = RightAscension.deg()

        val Ar = run {
            val y = Declination.sin()
            val x = lat.cos()
            Degree.aCos(y / x).correct360()
        }
        val As = (360.deg - Ar).correct360()

        val hA = run {
            val p1 = lat.tan() * Declination.tan() * -1
            Degree.aCos(p1).hour()
        }

        val STr = SiderealTime((24.hour + rightAscension.hour() - hA).correct24())
        val STs = SiderealTime((rightAscension.hour() + hA).correct24())

        return RiseAndSet(STr, STs, Ar, As, hA)
    }

    /**
     * r in Earth-radii
     */
    fun correctParallax(r: Float, lat: Degree, hourAngle: Degree, altitude: Float): EquatorialSystem {
        val rightAscension = RightAscension.deg()

        val (cos, sin) = run {
            val u = Degree.aTan(lat.tan() * 0.996647f)
            val h = altitude / 6378140
            val cos = u.cos() + lat.cos() * h
            val sin = u.sin() * 0.996647f + lat.sin() * h
            Pair(cos, sin)
        }

        val delta = run {
            val p1 = cos * hourAngle.sin()
            val p2 = Declination.cos() * r
            val p3 = cos * hourAngle.cos()
            Degree.aTan(p1 / (p2 - p3))
        }
        val hA2 = hourAngle + delta
        val rightAscension2 = rightAscension - delta
        val declination2 = run {
            val sinDecli = Declination.sin()
            val y = r * sinDecli - sin
            val x = r * Declination.cos() * hourAngle.cos() - cos
            val p1 = y / x
            Degree.aTan(hA2.cos() * (y / x))
        }
        return EquatorialSystem(rightAscension2.hour(), declination2)
    }
    fun correctParallax(d: Distance, observer: Observer, siderealTime: SiderealTime): EquatorialSystem {
        val r = d.toEarthRadii()
        val hA = (siderealTime.tH() - RightAscension).deg()
        return correctParallax(r, observer.lat, hA, observer.altitude)
    }

    data class RiseAndSet(
        val STr: SiderealTime,
        val STs: SiderealTime,
        val Ar: Degree,
        val As: Degree,
        val hA: Hour
    ) {
        fun toList() = listOf(STr, STs, Ar, As, hA)
    }
}