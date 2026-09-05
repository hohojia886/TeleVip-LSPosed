package com.my.televip.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.LinkedHashMap
import java.util.Locale
import java.util.TimeZone
import kotlin.math.floor
import kotlin.math.max

object IdDateEstimator {

    class AgeResult(
        @JvmField val status: Int,
        @JvmField val timestampMs: Long
    )

    private val AGES: MutableMap<Long, Long> = LinkedHashMap()
    private val IDS: List<Long>

    init {
        put(2768409L, 1383264000000L)
        put(7679610L, 1388448000000L)
        put(11538514L, 1391212000000L)
        put(15835244L, 1392940000000L)
        put(23646077L, 1393459000000L)
        put(38015510L, 1393632000000L)
        put(44634663L, 1399334000000L)
        put(46145305L, 1400198000000L)
        put(54845238L, 1411257000000L)
        put(63263518L, 1414454000000L)
        put(101260938L, 1425600000000L)
        put(101323197L, 1426204000000L)
        put(111220210L, 1429574000000L)
        put(103258382L, 1432771000000L)
        put(103151531L, 1433376000000L)
        put(116812045L, 1437696000000L)
        put(122600695L, 1437782000000L)
        put(109393468L, 1439078000000L)
        put(112594714L, 1439683000000L)
        put(124872445L, 1439856000000L)
        put(130029930L, 1441324000000L)
        put(125828524L, 1444003000000L)
        put(133909606L, 1444176000000L)
        put(157242073L, 1446768000000L)
        put(143445125L, 1448928000000L)
        put(148670295L, 1452211000000L)
        put(152079341L, 1453420000000L)
        put(171295414L, 1457481000000L)
        put(181783990L, 1460246000000L)
        put(222021233L, 1465344000000L)
        put(225034354L, 1466208000000L)
        put(278941742L, 1473465000000L)
        put(285253072L, 1476835000000L)
        put(294851037L, 1479600000000L)
        put(297621225L, 1481846000000L)
        put(328594461L, 1482969000000L)
        put(337808429L, 1487707000000L)
        put(341546272L, 1487782000000L)
        put(352940995L, 1487894000000L)
        put(369669043L, 1490918000000L)
        put(400169472L, 1501459000000L)
        put(805158066L, 1563208000000L)
        put(1974255900L, 1634000000000L)
        put(5795034000L, 1662076800000L)
        put(6227468000L, 1679270400000L)
        put(7583599300L, 1739664000000L)
        put(7947063900L, 1754092800000L)
        put(8235679900L, 1758758400000L)
        IDS = ArrayList(AGES.keys)
    }

    private fun put(id: Long, ts: Long) {
        AGES[id] = ts
    }

    @JvmStatic
    fun getDate(id: Long): AgeResult {
        val minId = IDS[0]
        val maxId = IDS[IDS.size - 1]

        if (id < minId) {
            return AgeResult(-1, AGES[minId]!!)
        }

        if (id > maxId) {
            return AgeResult(1, AGES[maxId]!!)
        }

        for (i in IDS.indices) {
            val currentId = IDS[i]
            if (id <= currentId) {
                if (i == 0) {
                    return AgeResult(0, AGES[minId]!!)
                }
                val lowerId = IDS[i - 1]
                val lowerAge = AGES[lowerId]!!
                val upperAge = AGES[currentId]!!

                val idRatio = (id - lowerId).toDouble() / (currentId - lowerId).toDouble()
                val midDate = floor(idRatio * (upperAge - lowerAge) + lowerAge).toLong()
                return AgeResult(0, midDate)
            }
        }

        return AgeResult(1, AGES[maxId]!!)
    }

    @JvmStatic
    fun getYearAndMethod(id: Long): String {
        val d = getDate(id)
        val sdf = SimpleDateFormat("M/yyyy", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        return sdf.format(Date(d.timestampMs))
    }

    @JvmStatic
    fun getAge(id: Long): Int {
        val d = getDate(id)
        val created = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = d.timestampMs
        }
        val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        var age = now.get(Calendar.YEAR) - created.get(Calendar.YEAR)
        if (now.get(Calendar.MONTH) < created.get(Calendar.MONTH)) {
            age--
        }
        return max(age, 0)
    }
}
