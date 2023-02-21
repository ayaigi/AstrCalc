package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.lib.units.deg
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import java.time.OffsetDateTime
import com.ayaigi.astrcalc.core.Astronomy
import java.time.ZoneOffset

fun main() {
    calc()
}

fun calc() {
    val gI_0 = GeoInstant(53.deg, 8.deg, 20.0, OffsetDateTime.now())
    val gI_1 = GeoInstant(53.deg, 8.deg, 0.0, OffsetDateTime.of(2023, 2, 19, 16,10,0,0, ZoneOffset.ofHours(1)))
    val gI_2 = GeoInstant(52.deg, 0.deg, 0.0, OffsetDateTime.of(1979, 9, 6, 0,0,0,0, ZoneOffset.ofHours(0)))
    val gI_seoul = GeoInstant(37.deg, 126.deg, 0.0, OffsetDateTime.of(2023, 2, 22, 8,30,0,0, ZoneOffset.ofHours(9)))
    val gI_4 = GeoInstant(0.deg, 0.deg, 0.0, OffsetDateTime.of(1980, 7, 27, 18,3,0,0, ZoneOffset.ofHours(0)))
    val gI_moPo = GeoInstant(0.deg, 0.deg, 0.0, OffsetDateTime.of(1979, 2, 26, 5,45,26,0, ZoneOffset.ofHours(0)))
    val astro = Astronomy(gI_1)
    println(astro.geoInstant)
    val t = SolarSystemTargets.Moon
    val res = astro.calc(t)
    (res as SolarResult)
    println(res)
}
