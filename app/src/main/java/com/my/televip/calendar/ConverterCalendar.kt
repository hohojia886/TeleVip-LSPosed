package com.my.televip.calendar

import com.my.televip.configs.ConfigManager
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import ir.huri.jcal.JalaliCalendar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

object ConverterCalendar {

    @JvmStatic
    fun toHijri(date: Long): HijrahDate {
        val zdt = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault())
        return HijrahDate.from(zdt)
    }

    @JvmStatic
    fun toHijri(calendar: Calendar): HijrahDate {
        val zdt = Instant.ofEpochMilli(calendar.timeInMillis).atZone(ZoneId.systemDefault())
        return HijrahDate.from(zdt)
    }

    @JvmStatic
    fun toHijri(date: Date): HijrahDate {
        val zdt = Instant.ofEpochMilli(date.time).atZone(ZoneId.systemDefault())
        return HijrahDate.from(zdt)
    }

    @JvmStatic
    fun toJalali(date: Long): JalaliCalendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return JalaliCalendar(calendar.time)
    }

    @JvmStatic
    fun toJalali(calendar: Calendar): JalaliCalendar {
        return JalaliCalendar(calendar.time)
    }

    @JvmStatic
    fun toJalali(date: Date): JalaliCalendar {
        return JalaliCalendar(date)
    }

    @JvmStatic
    fun toCalendar(date: Long): CalendarDate {
        val customCal = ConfigManager.customCalendar
        if (customCal == null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            return CalendarDate(calendar)
        }
        return when (customCal.getCustomCalendar()) {
            1 -> CalendarDate(toHijri(date))
            2 -> CalendarDate(toJalali(date))
            else -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date
                CalendarDate(calendar)
            }
        }
    }

    @JvmStatic
    fun toCalendar(date: Date): CalendarDate? {
        val customCal = ConfigManager.customCalendar ?: return null
        return when (customCal.getCustomCalendar()) {
            1 -> CalendarDate(toHijri(date))
            2 -> CalendarDate(toJalali(date))
            else -> null
        }
    }

    @JvmStatic
    fun toCalendar(calendar: Calendar): CalendarDate {
        val customCal = ConfigManager.customCalendar
        if (customCal == null) {
            return CalendarDate(calendar)
        }
        return when (customCal.getCustomCalendar()) {
            1 -> CalendarDate(toHijri(calendar))
            2 -> CalendarDate(toJalali(calendar))
            else -> CalendarDate(calendar)
        }
    }

    @JvmStatic
    fun formatDate(date: Long): String? {
        try {
            val now = Calendar.getInstance()
            now.timeInMillis = System.currentTimeMillis()
            val rightNowNow = toCalendar(now)

            val day = rightNowNow.day
            val year = rightNowNow.year

            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val dateHour = sdf.format(Date(date))

            val rightNow = toCalendar(date)

            val dateDay = rightNow.day
            val dateYear = rightNow.year
            val dateMonth = rightNow.month
            val dateMonthName = rightNow.monthName

            return when {
                dateDay == day && year == dateYear -> Translator.get(Keys.TodayAt) + dateHour
                dateDay + 1 == day && year == dateYear -> Translator.get(Keys.YesterdayAt) + dateHour
                abs(System.currentTimeMillis() - date) < 31536000000L -> Translator.get(Keys.Edited) + dateDay + " " + dateMonthName + " " + dateHour
                else -> Translator.get(Keys.Edited) + dateYear + "/" + dateMonth + "/" + dateDay + " " + dateHour
            }
        } catch (e: Exception) {
            // Ignored
        }
        return null
    }
}
