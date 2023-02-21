package com.ayaigi.astrcalc.lib.units

import java.util.*

fun main() {
    test1()
}

private fun test1() {
    val deg = 0.000003.deg
    val f = deg.format().OneUnit
    println(f)
}

private fun test2() {
    val i = 581.34f
    val s = "%.1f".format(Locale.ENGLISH, i)
    println(s)
}