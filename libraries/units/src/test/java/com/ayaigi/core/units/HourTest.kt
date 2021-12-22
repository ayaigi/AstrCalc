package com.ayaigi.core.units

import com.ayaigi.astrcalc.units.Hour
import org.junit.Test

class HourTest {

    @Test
    fun div() {
        val t1 = com.ayaigi.astrcalc.units.Hour(6f)
        val t2 = 3f

        val r = t1 / t2

        println(r)
    }

    @Test
    fun minus() {
        val t1 = com.ayaigi.astrcalc.units.Hour(5f)
        val t2 = com.ayaigi.astrcalc.units.Hour(3f)

        val r = t1 - t2

        println(r)
    }
}