package com.ayaigi.astrcalc.target

import com.ayaigi.astrcalc.core.GeoInstant
import com.ayaigi.astrcalc.lib.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.lib.coorsytems.EclipticSystem
import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import java.time.Instant

interface AstroCalcTarget {
    val id: AstroTarget
    val position: EquatorialSystem
    val ecliptic: EclipticSystem
    val instant: Instant
    //fun riseAndSet(lat: Degree, lon: Degree, altitude: Double): EquatorialSystem.RiseAndSet
    fun riseAndSet(observer: GeoInstant): EquatorialSystem.RiseAndSet //= riseAndSet(observer.lat, observer.lon, observer.altitude)
}