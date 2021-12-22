package com.ayaigi.solarsystem.solar

fun main() {
    PlanetTest()
}

private fun PlanetTest() {
    val org = "MERCURY(\n" +
            "        1,\n" +
            "        true,\n" +
            "        0.24085,\n" +
            "        Degrees.fromDecimal(231.2973),\n" +
            "        Degrees.fromDecimal(77.1442128),\n" +
            "        0.2056306,\n" +
            "        0.3870986,\n" +
            "        Degrees.fromDecimal(7.0043579),\n" +
            "        Degrees.fromDecimal(48.0941733),\n" +
            "        6.74,\n" +
            "        1.918 * 10E-6\n" +
            "    ),\n" +
            "    XVENUS(\n" +
            "        2,\n" +
            "        true,\n" +
            "        0.61521,\n" +
            "        Degrees.fromDecimal(355.73352),\n" +
            "        Degrees.fromDecimal(131.2895792),\n" +
            "        0.0067826,\n" +
            "        0.7233316,\n" +
            "        Degrees.fromDecimal(3.394435),\n" +
            "        Degrees.fromDecimal(76.4997524),\n" +
            "        16.92,\n" +
            "        1.721 * 10E-5\n" +
            "    ),\n" +
            "    XEARTH(\n" +
            "        3,\n" +
            "        true,\n" +
            "        1.00004,\n" +
            "        Degrees.fromDecimal(98.833540),\n" +
            "        Degrees.fromDecimal(102.596403),\n" +
            "        0.016718,\n" +
            "        1.0,\n" +
            "        Degrees.fromDecimal(1.0),\n" +
            "        Degrees.fromDecimal(0.0),\n" +
            "        0.0,\n" +
            "        0.0\n" +
            "    ),\n" +
            "    XMARS(\n" +
            "        4,\n" +
            "        false,\n" +
            "        1.88089,\n" +
            "        Degrees.fromDecimal(126.30783),\n" +
            "        Degrees.fromDecimal(335.6908166),\n" +
            "        0.0933865,\n" +
            "        1.5236883,\n" +
            "        Degrees.fromDecimal(1.8498011),\n" +
            "        Degrees.fromDecimal(49.4032001),\n" +
            "        9.36,\n" +
            "        4.539 * 10E-6\n" +
            "    ),\n" +
            "    XJUPITER(\n" +
            "        5,\n" +
            "        false,\n" +
            "        11.86224,\n" +
            "        Degrees.fromDecimal(146.966365),\n" +
            "        Degrees.fromDecimal(14.0095493),\n" +
            "        0.0484658,\n" +
            "        5.202561,\n" +
            "        Degrees.fromDecimal(1.3041819),\n" +
            "        Degrees.fromDecimal(100.2520175),\n" +
            "        196.74,\n" +
            "        1.994 * 10E-4\n" +
            "    ),\n" +
            "    XSATURN(\n" +
            "        6,\n" +
            "        false,\n" +
            "        29.45771,\n" +
            "        Degrees.fromDecimal(165.322242),\n" +
            "        Degrees.fromDecimal(92.6653974),\n" +
            "        0.0556155,\n" +
            "        9.554747,\n" +
            "        Degrees.fromDecimal(2.4893741),\n" +
            "        Degrees.fromDecimal(113.4888341),\n" +
            "        165.60,\n" +
            "        1.740 * 10E-4\n" +
            "    ),\n" +
            "    XURANUS(\n" +
            "        7,\n" +
            "        false,\n" +
            "        84.01247,\n" +
            "        Degrees.fromDecimal(228.0708551),\n" +
            "        Degrees.fromDecimal(172.7363288),\n" +
            "        0.0463232,\n" +
            "        19.21814,\n" +
            "        Degrees.fromDecimal(0.7729895),\n" +
            "        Degrees.fromDecimal(73.8768642),\n" +
            "        65.80,\n" +
            "        7.768 * 10E-5\n" +
            "    ),\n" +
            "    XNEPTUNE(\n" +
            "        8,\n" +
            "        false,\n" +
            "        164.79558,\n" +
            "        Degrees.fromDecimal(260.3578998),\n" +
            "        Degrees.fromDecimal(47.8672148),\n" +
            "        0.0090021,\n" +
            "        30.10957,\n" +
            "        Degrees.fromDecimal(1.7716017),\n" +
            "        Degrees.fromDecimal(131.5606494),\n" +
            "        62.20,\n" +
            "        7.597 * 10E-5\n" +
            "    ),\n" +
            "    XPLUTO(\n" +
            "        9,\n" +
            "        false,\n" +
            "        250.9,\n" +
            "        Degrees.fromDecimal(209.4399),\n" +
            "        Degrees.fromDecimal(222.972),\n" +
            "        0.25387,\n" +
            "        39.78459,\n" +
            "        Degrees.fromDecimal(17.137),\n" +
            "        Degrees.fromDecimal(109.941),\n" +
            "        8.20,\n" +
            "        4.073 * 10E-6\n" +
            "    )"

    val pl = org.split('X')

    val li = pl.map {
        it
            .split('\n')
            .map {
                it.toCharArray().dropWhile {
                    it == ' '
                }.joinToString(separator = "")
            }
            .map {
                it.split(',')[0]
            }
            .dropWhile {
                it.isBlank()
            }
            .map {
                it.toCharArray().dropWhile {
                    it == ' ' || it == ','
                }.joinToString(separator = "")
            }
    }

    println(li[0].joinToString(separator = ":"))
}

private val boolL = listOf(null, null, null, false,true, true, false, false, true, true, false, )