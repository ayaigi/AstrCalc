package com.ayaigi.astrcalc.lib.units

sealed interface AstronomicalUnit {
    /**
     * in Hours / Degrees
     */
    val value: Float

    fun decimal() = value

    fun format(): Format
}