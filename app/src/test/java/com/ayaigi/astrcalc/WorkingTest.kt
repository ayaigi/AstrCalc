package com.ayaigi.astrcalc

/*
import com.ayaigi.astrcalc.core.AstronomicalResults
import com.ayaigi.astrcalc.core.Astronomy
import com.ayaigi.astrcalc.lib.units.deg
import com.ayaigi.astrcalc.target.solarsystem.SolarSystemTargets
import com.ayaigi.loging.PrintLog
import java.time.OffsetDateTime
import java.time.ZoneOffset
val LogUtil = PrintLog()

fun main() {
    LogUtil.Fox.init(listOf("Time", "Alt", "Azi", "Rise", "Dec"))
    LogUtil.Dog.init(listOf("Time", "Alt", "Azi", "Gam", "Del"))

    val v = SolarValues().values

    LogUtil.Fox.print()
}

private const val mul = 10
const val startH = 0

class SolarValues {
    private val timeStart: OffsetDateTime =
        OffsetDateTime.of(1978, 7, 27, startH, 0, 0, 0, ZoneOffset.UTC)
    private var time = timeStart
    private var astr: Astronomy = Astronomy(time, lat, lon, alt)
    lateinit var res: AstronomicalResults
    private val _values: MutableList<SolarLog> = mutableListOf<SolarLog>().apply {
        for (i in 0..(4320 / mul)) {
            time = timeStart.plusMinutes((i * mul).toLong())
            LogUtil.Bee.add(listOf(time.toEpochSecond().toFloat()))
            //println(time)
            astr = Astronomy(time, lat, lon, alt)
            res = astr.calc(target)
            res.also {
                val l = listOf(
                    ((i * mul) + startH).toFloat(),
                    it.position.Altitude.value,
                    it.position.Azimuth.value,
                    it.positionEq.RightAscension.value,
                    it.positionEq.Declination.value
                )
                LogUtil.Fox.add(l)
                add(
                    SolarLog(
                        (i * mul + startH).toFloat(),
                        it.position.Altitude.value,
                        it.position.Azimuth.value
                    )
                )
            }
        }
    }
    val values: List<SolarLog>
        get() = _values

    companion object {
        private val lat = (55).deg
        private val lon = 0.deg
        private val alt = 0f
        private val target = SolarSystemTargets.Sun

        data class SolarLog(val x: Float, val y1: Float, val y2: Float) {
            fun list() = listOf(x, y1, y2)
        }
    }
}

 */