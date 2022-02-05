package com.ayaigi.astrcalc

import com.ayaigi.astrcalc.lib.units.Degree
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.sin

private const val mul = 10

fun main() {
    LogUtil.Fox.init(listOf("x", "sin", "arc"))
    var i: Float
    for (iI in 0..(4 * PI * mul).toInt()) {
        i = iI / mul.toFloat()
        //LogUtil.Fox.add(listOf(i, Degree.fromRadians(i).sin(), Degree.aSin(Degree.fromRadians(i).sin()).value))
        LogUtil.Fox.add(listOf(i, sin(i), Degree.fromRadians(asin(sin(i))).value))
    }
    LogUtil.Fox.print()
}