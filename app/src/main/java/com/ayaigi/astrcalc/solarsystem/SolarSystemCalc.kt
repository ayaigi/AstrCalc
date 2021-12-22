package com.ayaigi.astrcalc.solarsystem

import com.ayaigi.astrcalc.units.Degree

sealed interface SolarSystemCalc : AstroCalcTarget {
    val angularSize: Degree
    //override val position: EquatorialSystem
    val distance: com.ayaigi.astrcalc.coorsytems.Distance
    fun phase(): SolarPhase
}