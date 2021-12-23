package com.ayaigi.astrcalc.target.solarsystem

import java.lang.Exception

object SolarSystemTargets {
    val Sun = AstroTarget(0, "Sun")
    val Moon = AstroTarget(3, "Moon")
    val Mercury = AstroTarget(1, "Mercury")
    val Venus = AstroTarget(2, "Venus")
    val Mars = AstroTarget(4, "Mars")
    val Jupiter = AstroTarget(5, "Jupiter")
    val Saturn = AstroTarget(6, "Saturn")
    val Uranus = AstroTarget(7, "Uranus")
    val Neptune = AstroTarget(8, "Neptune")
    val Pluto = AstroTarget(9, "Pluto")
    val Planets: List<AstroTarget> = listOf(
        Mercury, Venus, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto
    )
    val SolarSystem: List<AstroTarget> = listOf(
        Sun, Moon, Mercury, Venus, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto
    )
    val SolarSystemIds = SolarSystem.map { it.id }
    fun fromId(id: Int): AstroTarget {
        val t = SolarSystem.find {
            it.id == id
        } ?: throw InvalidTargetExpression("targetId: $id")
        return t
    }
}

class InvalidTargetExpression(override val message: String?) : Exception(message)

data class AstroTarget internal constructor(
    val id: Int,
    val name: String
){
    val isSolar: Boolean
        get() = SolarSystemTargets.SolarSystemIds.contains(id)
}

