package com.ayaigi.astrcalc.coorsytems

import com.ayaigi.astrcalc.units.SiderealTime
import com.ayaigi.astrcalc.units.Degree

data class HorizontalSystem(val Azimuth: Degree, val Altitude: Degree) {
    fun toEquatorialSys(lat: Degree, ST: SiderealTime): EquatorialSystem {
        val declination = run {
            val t1 = Altitude.sin() * lat.sin()
            val t2 = Altitude.cos() * lat.cos() * Azimuth.cos()
            Degree.aSin(t1 + t2)
        }
        var hA = run {
            val o = Altitude.sin() - (lat.sin() * declination.sin())
            val u = lat.cos() * declination.cos()
            Degree.aCos(o / u)
        }
        if(Azimuth.sin() > 0) {
            hA = Degree(360f) - Azimuth
        }
        val rightAscension = (ST.tH() - hA.hour())
        return EquatorialSystem(rightAscension, declination)
    }
}