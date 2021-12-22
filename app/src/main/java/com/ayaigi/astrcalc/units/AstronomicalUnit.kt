package com.ayaigi.astrcalc.units

sealed interface AstronomicalUnit {
    /**
     * in Hours / Degrees
     */
    val value: Float

    fun decimal() = value

    fun format(): Format
}