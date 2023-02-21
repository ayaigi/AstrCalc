package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.lib.units.Degree
import java.time.OffsetDateTime

data class Observer(val lat: Degree, val lon: Degree, val altitude: Double)
data class GeoInstant(val lat: Degree, val lon: Degree, val altitude: Double, val instant: OffsetDateTime)