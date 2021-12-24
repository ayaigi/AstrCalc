package com.ayaigi.astrcalc.target.solarsystem

data class SolarPhase(val percentage: Float, val dir: Boolean) {
    override fun toString(): String =
        (if (dir) "" else "-") + (percentage * 100).toInt().toString() + "%"
}
