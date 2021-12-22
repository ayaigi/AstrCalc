package com.ayaigi.core.units

import com.ayaigi.astrcalc.units.Degree
import org.junit.Assert.*

import org.junit.Test

class DegreeTest {

    @Test
    fun tan() {
        val y = -1f
        val x = 1f
        val d = com.ayaigi.astrcalc.units.Degree.aTan2(y, x)
        println()
        println(d)
        println()
    }
}