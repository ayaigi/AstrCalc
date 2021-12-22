package com.ayaigi.astrcalc.coorsytems

import com.ayaigi.astrcalc.units.Degree
import com.ayaigi.astrcalc.units.Hour
import java.time.OffsetTime

data class RiseAndSet(val Rise: OffsetTime, val Set: OffsetTime, val RiseAzimuth: Degree, val SetAzimuth: Degree, val HourAngle: Hour)
