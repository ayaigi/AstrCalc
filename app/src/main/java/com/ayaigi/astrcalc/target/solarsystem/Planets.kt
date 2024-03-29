package com.ayaigi.astrcalc.target.solarsystem

import com.ayaigi.astrcalc.lib.units.Degree
import com.ayaigi.astrcalc.lib.units.deg

enum class Planets(
    val id: Int,
    val innerOuter: Boolean,
    val periodTp: Double,
    val longEpsilon: Degree,
    val longPerihelionOmega: Degree,
    val eccentricityE: Double,
    val semiMajorAxisAAU: Double,
    val inclinationI: Degree,
    val longAscendingNodeGOmega: Degree,
    val angularSize1AUTheta0: Double,
    val brightnissfactorA: Double,
) {
    MERCURY(1,
        true,
        0.24085,
        231.2973.deg,
        77.1442128.deg,
        0.2056306,
        0.3870986,
        7.0043579.deg,
        48.0941733.deg,
        6.74,
        (1.918 * 10E-6),
    ),
    VENUS(2,
        true,
        0.61521,
        355.73352.deg,
        131.2895792.deg,
        0.0067826,
        0.7233316,
        3.394435.deg,
        76.4997524.deg,
        16.92,
        (1.721 * 10E-5),
    ),
    Earth(3,
        true,
        1.00004,
        98.833540.deg,
        102.596403.deg,
        0.016718,
        1.0,
        1.deg,
        0.deg,
        0.0,
        (0.0),
    ),
    Mars(4,
        false,
        1.88089,
        126.30783.deg,
        335.6908166.deg,
        0.0933865,
        1.5236883,
        1.8498011.deg,
        49.4032001.deg,
        9.36,
        (4.539 * 10E-6),
    ),
    Jupiter(5,
        false,
        11.86224,
        146.966365.deg,
        14.0095493.deg,
        0.0484658,
        5.202561,
        1.3041819.deg,
        100.2520175.deg,
        196.74,
        (1.994 * 10E-4),
    ),
    Saturn(6,
        false,
        29.45771,
        165.322242.deg,
        92.6653974.deg,
        0.0556155,
        9.554747,
        2.4893741.deg,
        113.4888341.deg,
        165.60,
        (1.740 * 10E-4),
    ),
    Uranus(7,
        false,
        84.01247,
        228.0708551.deg,
        172.7363288.deg,
        0.0463232,
        19.21814,
        0.7729895.deg,
        73.8768642.deg,
        65.80,
        (7.768 * 10E-5),
    ),
    Neptune(8,
        false,
        164.79558,
        260.3578998.deg,
        47.8672148.deg,
        0.0090021,
        30.10957,
        1.7716017.deg,
        131.5606494.deg,
        62.20,
        (7.597 * 10E-5),
    ),
    Pluto(9,
        false,
        250.9,
        209.4399.deg,
        222.972.deg,
        0.25387,
        39.78459,
        17.137.deg,
        109.941.deg,
        8.20,
        (4.073 * 10E-6),
    )
}