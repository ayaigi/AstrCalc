package com.ayaigi.astrcalc

import com.ayaigi.astrcalc.core.Astronomy
import com.ayaigi.astrcalc.core.Observer
import com.ayaigi.astrcalc.lib.units.deg
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun main() {
    val time = OffsetDateTime.of(1978, 7, 27, 0, 0, 0, 0, ZoneOffset.UTC)
    val ob = Observer(20.deg, 0.deg, 0f)
    var astr = Astronomy(
        time.plusMinutes(2080L),
        ob
    )
    var res = astr.calc(SolarSystemTargets.Sun)
    //println(res)
    //println("Eq: " + res.positionEq.run { "RiseAs: " + RightAscension.format().IntZMin_Sec__ + " ---- Decli: " + Declination.format().IntZMin_Sec__ })
    println("Ho: " + res.position.run {
        "Alt: " + Altitude.format().IntZMin_Sec__ + " ---- Azi: " + Azimuth.format().IntZMin_Sec__
    })
    println("----")
    astr = Astronomy(
        time.plusMinutes(2160L),
        ob
    )
    res = astr.calc(SolarSystemTargets.Sun)
    println("Ho: " + res.position.run {
        "Alt: " + Altitude.format().IntZMin_Sec__ + " ---- Azi: " + Azimuth.format().IntZMin_Sec__
    })
    println("----")
    astr = Astronomy(
        time.plusMinutes(2180L),
        ob
    )
    res = astr.calc(SolarSystemTargets.Sun)
    println("Ho: " + res.position.run {
        "Alt: " + Altitude.format().IntZMin_Sec__ + " ---- Azi: " + Azimuth.format().IntZMin_Sec__
    })
}
