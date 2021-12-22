package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.solarsystem.SolarSystemTargets
import com.ayaigi.astrcalc.units.deg

import org.junit.Test
import java.time.OffsetDateTime

class AstronomyTest {

    @Test
    fun calc() {
        val astro = Astronomy(OffsetDateTime.now(), 0f.deg, 0f.deg, 500f)
        val t = SolarSystemTargets.Sun
        val res = astro.calc(t)
        (res as SolarResult)
    }
}