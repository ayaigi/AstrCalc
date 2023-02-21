package com.ayaigi.astrcalc.core

import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.lib.units.deg
import com.ayaigi.astrcalc.target.AstroCalcTarget
import com.ayaigi.astrcalc.target.solarsystem.AstroTarget
import com.ayaigi.astrcalc.target.solarsystem.InvalidTargetExpression
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemCalc
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import com.ayaigi.astrcalc.target.solarsystem.calc.Moon
import com.ayaigi.astrcalc.target.solarsystem.calc.Planet
import com.ayaigi.astrcalc.target.solarsystem.calc.Sun
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset


class Astronomy internal constructor(val geoInstant: GeoInstant) {
    companion object {
        val sample1: Astronomy
            get() = Astronomy(Instant.now().atOffset(ZoneOffset.UTC), 52.4960.deg, (13.2509).deg, 0.0)

        operator fun invoke(
            instant: OffsetDateTime,
            Latitude: Degree,
            Longitude: Degree,
            Altitude: Double
        ) = Astronomy(
            GeoInstant(Latitude, Longitude, Altitude, instant)
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
            val t =
                if (SolarSystemTargets.SolarSystemIds.contains(id)) SolarSystemTargets.fromId(id)
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
            target.isSolar -> SolarResult(
                geoInstant,
                solarCalcByTarget(target.id, geoInstant.instant.toInstant())
            )
            else -> AstronomicalResult(
                geoInstant,
                targetFromId(target.id, geoInstant.instant.toInstant())
            )
        }
    }
}