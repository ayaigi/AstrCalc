package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.lib.coorsytems.*
import com.ayaigi.astrcalc.target.AstroCalcTarget
import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import com.ayaigi.astrcalc.target.solarsystem.SolarPhase
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemCalc
import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.lib.units.SiderealTime

sealed interface AstronomicalResults {
    val id: AstroTarget
    val geoInstant: GeoInstant
    val position: HorizontalSystem
    val positionEq: EquatorialSystem
    val riseAndSet: RiseAndSet
}

sealed interface SolarResults : AstronomicalResults {
    val phase: SolarPhase
    val distance: Distance
    val angularSize: Degree
    val eclipticPosi: EclipticSystem
}

class AstronomicalResult(
    override val geoInstant: GeoInstant,
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
    override val geoInstant: GeoInstant,
    private val Target: SolarSystemCalc
) : SolarResults {


    override fun toString(): String {
        return id.toString() + "\n" +
                position.toString() + "\n" +
                positionEq.toString() + "\n" +
                riseAndSet.toString() + "\n" +
                "Phase=" + phase.toString() + "\n" +
                "Distance=" + distance.toString() + "\n" +
                "Size=" + angularSize.toString()
    }

    private val siderealTime = SiderealTime.fromOffsetDateTime(geoInstant)

    override val eclipticPosi: EclipticSystem
        get() = Target.ecliptic

    override val position: HorizontalSystem
        get() = run {
            val eq1 = Target.position
            val eq2 = eq1.correctParallax(distance, geoInstant, siderealTime)
            val ho = eq2.toHorizonSys(geoInstant.lat, siderealTime)
            ho
        }

    override val riseAndSet: RiseAndSet
        get() = run {
            val rsS = Target.riseAndSet(geoInstant)
            val rL = rsS.STr.convertToLocalTime(geoInstant)
            val sL = rsS.STs.convertToLocalTime(geoInstant)
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
        get() = Target.angularSize()

}