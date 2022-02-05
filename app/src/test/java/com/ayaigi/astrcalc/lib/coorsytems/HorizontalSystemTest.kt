package com.ayaigi.astrcalc.lib.coorsytems

import com.ayaigi.astrcalc.lib.units.*
import org.junit.Assert.*

fun main() {
    val eq = EquatorialSystem(0.hour, Degree.of(23,13,10))
    val ho = eq.toHorizonSys(52.deg, SiderealTime(Hour.of(5,51,44)))
    println(ho.Altitude.format().IntZMin_Sec__ + " -- " + ho.Azimuth.format().IntZMin_Sec__)
}