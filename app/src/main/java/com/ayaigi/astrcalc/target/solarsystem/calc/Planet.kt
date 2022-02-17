package com.ayaigi.astrcalc.target.solarsystem.calc

import com.ayaigi.astrcalc.lib.coorsytems.Distance
import com.ayaigi.astrcalc.lib.coorsytems.EclipticSystem
import com.ayaigi.astrcalc.lib.coorsytems.EquatorialSystem
import com.ayaigi.astrcalc.lib.units.*
import com.ayaigi.astrcalc.target.solarsystem.*
import java.time.Instant
import kotlin.math.pow

class Planet : SolarSystemCalc {

    companion object {
        fun getPlanet(i: Int) = Planets.values().first {
            it.id == i
        }
    }

    override lateinit var instant: Instant
    val planet: Planets

    constructor(planet: Planets, instant: Instant) {
        this.instant = instant
        this.planet = planet
    }

    constructor(planetId: Int, instant: Instant) {
        this.instant = instant
        this.planet = getPlanet(planetId)
    }

    override val id: AstroTarget
        get() = SolarSystemTargets.fromId(planet.id)


    private val positionValues: PlanetValues = run {
        val D = instant.epochDay80()
        val vp = calcV(D)
        var lp = (vp + planet.longPerihelionOmega).correct360()
        if (planet == Planets.Jupiter || planet == Planets.Saturn) {
            val T = (instant.julianDay() - 2415020) / 36525
            val A = T / 5 + 0.1f
            val P = (237.47555f + 3034.9061f * T).deg
            val Q = (265.91650f + 1222.1139f * T).deg
            val V = Q * 5 - P * 2
            val B = Q - P
            val dL: Degree = if (planet.id == 5) {
                val t1 = (0.3314f - 0.0103f * A).deg * V.sin()
                val t2 = (0.0644f * A * V.cos()).deg
                t1 - t2
            } else {
                val t1 = V.cos() * (0.1609f * A - 0.0105f)
                val t2 = V.sin() * (0.0182f * A - 0.8142f)
                val t3 = B.sin() * 0.1488f - 0.0408f * (B * 2).sin()
                val t4 = B.sin() * 0.0856f * Q.cos()
                val t5 = B.cos() * 0.0813f * Q.sin()
                (t1 + t2 - t3 + t4 + t5).deg
            }
            lp += dL
        }

        val rp = calcR(vp)

        val earth = getPlanet(3)
        val ve = calcV(D, earth)
        val le = (ve + earth.longPerihelionOmega).correct360()
        val re = calcR(ve, earth)
        PlanetValues(rp, re, lp, le)
    }

    private val ecliptic: EclipticSystem = run {
        val (rp, re, lp, le) = positionValues

        val psi = run {
            val p1 = (lp - planet.longAscendingNodeGOmega).sin()
            val p2 = planet.inclinationI.sin()
            Degree.aSin(p1 * p2)
        }
        val l2 = planet.run {
            val y = run {
                val p1 = (lp - longAscendingNodeGOmega).sin()
                val p2 = inclinationI.cos()
                p1 * p2
            }
            val x = (lp - longAscendingNodeGOmega).cos()
            val L2 = Degree.aTan2(y, x) + longAscendingNodeGOmega
            L2
        }
        val r2 = psi.cos() * rp

        val Lambda = if (planet.innerOuter) {
            val A = run {
                val y = (le - l2).sin() * r2
                val x = re - r2 * (le - l2).cos()
                Degree.aTan(y / x)
            }
            le + 180f.deg + A
        } else {
            val A = run {
                val y = (l2 - le).sin() * re
                val x = r2 - (l2 - le).cos() * re
                Degree.aTan(y / x)
            }
            A + l2
        }
        val Betta = run {
            val y = run {
                val p1 = psi.tan() * r2
                val p2 = (Lambda - l2).sin()
                p1 * p2
            }
            val x = (l2 - le).sin() * re
            Degree.aTan(y / x)
        }
        EclipticSystem(Lambda, Betta)
    }

    override val position: EquatorialSystem = ecliptic.toEquatorialSys(instant)

    override val distance: Distance = run {
        val (r, R, l, L) = positionValues
        val p2 = R.pow(2) + r.pow(2) - (2 * R * r * (l - L).cos())
        Distance.fromAU(p2.pow(0.5f))
    }

    override fun angularSize(): Degree {
        return Degree(planet.angularSize1AUTheta0 / distance.toAU())
    }

    override fun phase(): SolarPhase {
        fun F(Lambda: Degree): Float {
            val l = positionValues.lp
            val D = l - Lambda
            return 0.5f * (1 - D.cos())
        }

        val F0: Float = F(Planet(planet, instant).ecliptic.Lambda)
        val F1: Float = F(Planet(planet, instant.plusSeconds(86400)).ecliptic.Lambda)
        return SolarPhase(F0, F0 > F1)
    }

    data class PlanetValues(val rp: Float, val re: Float, val lp: Degree, val le: Degree)

    private fun calcV(D: Float, planet: Planets = this.planet): Degree {
        return planet.run {
            val np = ((360 / 365.2422f) * (D / periodTp)).deg.correct360()
            val mp = np + longEpsilon - longPerihelionOmega
            val ep = Sun.routineR2(mp, eccentricityE)
            ep.second
        }
    }

    private fun calcR(v: Degree, planet: Planets = this.planet): Float {
        planet.run {
            val y = semiMajorAxisAAU * (1 - eccentricityE.pow(2))
            val x = 1 + eccentricityE * v.cos()
            return y / x
        }
    }

    override fun riseAndSet(
        lat: Degree,
        lon: Degree,
        altitude: Float
    ): EquatorialSystem.RiseAndSet {
        val posi0 = run {
            val instant = instant.startOfDay()
            Planet(planet, instant).position
        }
        val posi24 = run {
            val instant = instant.plusDays(1).startOfDay()
            Planet(planet, instant).position
        }
        val riSe0 = posi0.riseAndSet(lat, lon)
        val riSe24 = posi24.riseAndSet(lat, lon)

        val AziR = riSe0.Ar.averageCircle(riSe24.Ar)
        val AziS = riSe0.As.averageCircle(riSe24.As)

        val tR = run {
            val p1 = riSe0.STr.tH() * 24.07f
            val p2 = 24.07f.hour + riSe0.STr.tH() - riSe24.STr.tH()
            Hour(p1.value / p2.value)
        }
        val tS = run {
            val p1 = riSe0.STs.tH() * 24.07f
            val p2 = 24.07f.hour + riSe0.STs.tH() - riSe24.STs.tH()
            Hour(p1.value / p2.value)
        }
        val delta = run {
            0.0f.deg
        }.hour()
        val hA = riSe0.hA.deg().averageCircle(riSe24.hA.deg())
        val STr = SiderealTime(tS - delta)
        val STs = SiderealTime(tR - delta)
        return EquatorialSystem.RiseAndSet(STr, STs, AziR, AziS, hA.hour())
    }
}