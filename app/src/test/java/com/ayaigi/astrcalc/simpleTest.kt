package com.ayaigi.astrcalc

import com.ayaigi.astrcalc.core.Astronomy
import com.ayaigi.astrcalc.core.Observer
import com.ayaigi.astrcalc.lib.units.SiderealTime
import com.ayaigi.astrcalc.lib.units.deg
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import java.sql.DriverManager.println
import java.time.OffsetDateTime
import java.time.ZoneOffset
/*
fun main() {
    sideTest()
    //astrTest()
}

private fun sideTest(){
    val t = OffsetDateTime.now()
    val t1 = SiderealTime.fromOffsetDateTime(t, 8.deg)
    println(t1.tH().toLocalTime())
}

private fun astrTest(){
    val ob = Observer(52.4960.deg, (13.2509).deg, 36f)
    val time = OffsetDateTime.of(2022, 10, 17, 7, 40, 0,0, ZoneOffset.ofHours(2))
    var astr = Astronomy(time, ob)

    val calc = astr.calc(SolarSystemTargets.Sun)

    println(calc.toString())
}


 */