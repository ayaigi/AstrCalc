package com.ayaigi.astrcalc.solarsystem

import com.ayaigi.astrcalc.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.core.Observer
import com.ayaigi.astrcalc.units.Degree
import java.time.Instant

sealed interface AstroCalcTarget {
    val id: AstroTarget
    val position: EquatorialSystem
    val instant: Instant
    fun riseAndSet(lat: Degree, lon: Degree, altitude: Float): EquatorialSystem.RiseAndSet
    fun riseAndSet(observer: Observer): EquatorialSystem.RiseAndSet = riseAndSet(observer.lat, observer.lon, observer.altitude)
}