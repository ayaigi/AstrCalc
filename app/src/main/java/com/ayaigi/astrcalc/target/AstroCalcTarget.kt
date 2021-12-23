package com.ayaigi.astrcalc.target

import com.ayaigi.astrcalc.lib.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.core.Observer
import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import java.time.Instant

interface AstroCalcTarget {
    val id: AstroTarget
    val position: EquatorialSystem
    val instant: Instant
    fun riseAndSet(lat: Degree, lon: Degree, altitude: Float): EquatorialSystem.RiseAndSet
    fun riseAndSet(observer: Observer): EquatorialSystem.RiseAndSet = riseAndSet(observer.lat, observer.lon, observer.altitude)
}