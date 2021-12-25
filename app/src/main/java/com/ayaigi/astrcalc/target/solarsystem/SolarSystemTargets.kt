package com.ayaigi.astrcalc.target.solarsystem

object SolarSystemTargets {
    val Sun = AstroTarget(0)
    val Moon = AstroTarget(3)
    val Mercury = AstroTarget(1)
    val Venus = AstroTarget(2)
    val Mars = AstroTarget(4,)
    val Jupiter = AstroTarget(5)
    val Saturn = AstroTarget(6)
    val Uranus = AstroTarget(7)
    val Neptune = AstroTarget(8)
    val Pluto = AstroTarget(9)
    val Planets: List<AstroTarget> = listOf(
        Mercury, Venus, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto
    )

    /**
     * List is sorted by Id
     */
    val SolarSystem: List<AstroTarget> = listOf(
        Sun, Mercury, Venus, Moon, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto
    )
    /**
     * List is sorted
     */
    val SolarSystemIds = SolarSystem.map { it.id }

    fun fromId(id: Int): AstroTarget {
        val index = SolarSystemIds.binarySearch(id)
        if(index < 0) throw InvalidTargetExpression("targetId: $id")
        return SolarSystem[index]
    }

    internal fun nameById(id: Int): String = when (id) {
        0 -> "Sun"
        1 -> "Mercury"
        2 -> "Venus"
        3 -> "Moon"
        4 -> "Mars"
        5 -> "Jupiter"
        6 -> "Saturn"
        7 -> "Uranus"
        8 -> "Neptune"
        9 -> "Pluto"
        else -> throw InvalidTargetExpression("id: $id")
    }
}

internal class InvalidTargetExpression(override val message: String?) : Exception(message)

data class AstroTarget internal constructor(
    val id: Int
) {
    val isSolar: Boolean
        get() = SolarSystemTargets.SolarSystemIds.contains(id)

    val name: String
        get() = if (isSolar) SolarSystemTargets.nameById(id)
        else throw InvalidTargetExpression("id: $id")
}



