package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemCalc
import com.ayaigi.astrcalc.target.solarsystem.*
import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.target.AstroCalcTarget
import com.ayaigi.astrcalc.target.solarsystem.calc.Moon
import com.ayaigi.astrcalc.target.solarsystem.calc.Planet
import com.ayaigi.astrcalc.target.solarsystem.calc.Sun
import java.time.Instant
import java.time.OffsetDateTime

class Astronomy private constructor(
    val instant: OffsetDateTime,
    val observer: Observer
) {
    companion object {
        operator fun invoke(instant: OffsetDateTime, Observer: Observer) = Astronomy(instant, Observer)

        operator fun invoke(
            instant: OffsetDateTime,
            Latitude: Degree,
            Longitude: Degree,
            Altitude: Float
        ) = Astronomy(
            instant,
            Observer(Latitude, Longitude, Altitude)
        )

        private fun solarCalcByTarget(id: Int, instant: Instant): SolarSystemCalc {
            return when (id) {
                0 -> Sun(instant)
                1, 2 -> Planet(id, instant)
                in 4..9 -> Planet(id, instant)
                3 -> Moon(instant)
                else -> throw InvalidTargetExpression("targetId: $id")
            }
        }
        private fun targetFromId(id: Int, instant: Instant): AstroCalcTarget {
            val t = if(SolarSystemTargets.SolarSystemIds.contains(id)) SolarSystemTargets.fromId(id)
            else TODO("Not yet implemented")
            return solarCalcByTarget(t.id, instant)
        }
    }

    /**
     * use SolarSystemTargets or Companion by ID
     *
     * target:
     *
     *      Solar -> 'SolarResult'
     */
    fun calc(target: AstroTarget): AstronomicalResults {
        return when {
            target.isSolar -> SolarResult(instant, observer, solarCalcByTarget(target.id, instant.toInstant()))
            else -> AstronomicalResult(instant, observer, targetFromId(target.id, instant.toInstant()))
        }
    }
}