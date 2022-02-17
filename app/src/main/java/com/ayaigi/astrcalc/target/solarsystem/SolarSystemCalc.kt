package com.ayaigi.astrcalc.target.solarsystem

import com.ayaigi.astrcalc.lib.coorsytems.Distance
import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.target.AstroCalcTarget

internal interface SolarSystemCalc : AstroCalcTarget {
    fun angularSize(): Degree
    //override val position: EquatorialSystem
    val distance: Distance
    fun phase(): SolarPhase
}