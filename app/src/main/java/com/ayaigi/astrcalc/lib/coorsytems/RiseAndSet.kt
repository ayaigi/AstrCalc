package com.ayaigi.astrcalc.lib.coorsytems

import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.lib.units.Hour
import java.time.OffsetTime

data class RiseAndSet(val Rise: OffsetTime, val Set: OffsetTime, val RiseAzimuth: Degree, val SetAzimuth: Degree, val HourAngle: Hour)
