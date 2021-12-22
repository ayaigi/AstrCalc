package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.coorsytems.Distance
import com.ayaigi.astrcalc.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.coorsytems.HorizontalSystem
import com.ayaigi.astrcalc.coorsytems.RiseAndSet
import com.ayaigi.astrcalc.solarsystem.AstroCalcTarget
import com.ayaigi.astrcalc.solarsystem.AstroTarget
import com.ayaigi.astrcalc.solarsystem.SolarPhase
import com.ayaigi.astrcalc.solarsystem.SolarSystemCalc
import com.ayaigi.astrcalc.units.Degree
import com.ayaigi.astrcalc.units.SiderealTime
import java.time.OffsetDateTime

sealed interface AstronomicalResults {
    val id: AstroTarget
    val instant: OffsetDateTime
    val position: HorizontalSystem
    val positionEq: EquatorialSystem
    val riseAndSet: RiseAndSet
}

sealed interface SolarResults : AstronomicalResults {
    val phase: SolarPhase
    val distance: Distance
    val angularSize: Degree
}

class AstronomicalResult(
    override val instant: OffsetDateTime,
    private val observer: Observer,
    private val Target: AstroCalcTarget
) : AstronomicalResults {
    override val id: AstroTarget
        get() = TODO("Not yet implemented")
    override val position: HorizontalSystem
        get() = TODO("Not yet implemented")
    override val positionEq: EquatorialSystem
        get() = TODO("Not yet implemented")
    override val riseAndSet: RiseAndSet
        get() = TODO("Not yet implemented")
}

class SolarResult(
    final override val instant: OffsetDateTime,
    private val observer: Observer,
    private val Target: SolarSystemCalc
) : SolarResults {

    private val siderealTime = SiderealTime.fromOffsetDateTime(instant, observer.lon)

    override val position: HorizontalSystem
        get() = run {
            val eq1 = Target.position
            val eq2 = eq1.correctParallax(distance, observer, siderealTime)
            val ho = eq2.toHorizonSys(observer.lat, siderealTime)
            ho
        }

    override val riseAndSet: RiseAndSet
        get() = run {
            val rsS = Target.riseAndSet(observer)
            val rL = rsS.STr.convertToLocalTime(instant.toLocalDate(), observer.lon)
            val sL = rsS.STs.convertToLocalTime(instant.toLocalDate(), observer.lon)
            RiseAndSet(rL, sL, rsS.Ar, rsS.As, rsS.hA)
        }

    override val id: AstroTarget
        get() = Target.id

    override val positionEq: EquatorialSystem
        get() = Target.position

    override val phase: SolarPhase
        get() = Target.phase()

    override val distance: Distance
        get() = Target.distance

    override val angularSize: Degree
        get() = Target.angularSize

}