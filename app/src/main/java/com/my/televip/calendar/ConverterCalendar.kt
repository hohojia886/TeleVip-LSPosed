package com.my.televip.calendar

import com.my.televip.Configs.ConfigManager
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import ir.huri.jcal.JalaliCalendar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
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
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
        }
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
        val customCalendar = ConfigManager.customCalendar
        if (customCalendar.getCustomCalendar() == 1) {
            return CalendarDate(toHijri(date))
        } else if (customCalendar.getCustomCalendar() == 2) {
            return CalendarDate(toJalali(date))
        }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
        }
        return CalendarDate(calendar)
    }

    @JvmStatic
    fun toCalendar(date: Date): CalendarDate? {
        val customCalendar = ConfigManager.customCalendar
        if (customCalendar.getCustomCalendar() == 1) {
            return CalendarDate(toHijri(date))
        } else if (customCalendar.getCustomCalendar() == 2) {
            return CalendarDate(toJalali(date))
        }
        return null
    }

    @JvmStatic
    fun toCalendar(calendar: Calendar): CalendarDate {
        val customCalendar = ConfigManager.customCalendar
        if (customCalendar.getCustomCalendar() == 1) {
            return CalendarDate(toHijri(calendar))
        } else if (customCalendar.getCustomCalendar() == 2) {
            return CalendarDate(toJalali(calendar))
        }
        return CalendarDate(calendar)
    }

    @JvmStatic
    fun formatDate(date: Long): String? {
        return try {
            val now = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            var rightNow = toCalendar(now)

            val day = rightNow.day
            val year = rightNow.year

            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val dateHour = sdf.format(Date(date))

            rightNow = toCalendar(date)

            val dateDay = rightNow.day
            val dateYear = rightNow.year
            val dateMonth = rightNow.month
            val dateMonthName = rightNow.monthName

            when {
                dateDay == day && year == dateYear -> Translator.get(Keys.TodayAt) + dateHour
                dateDay + 1 == day && year == dateYear -> Translator.get(Keys.YesterdayAt) + dateHour
                abs(System.currentTimeMillis() - date) < 31536000000L -> Translator.get(Keys.Edited) + dateDay + " " + dateMonthName + " " + dateHour
                else -> Translator.get(Keys.Edited) + dateYear + "/" + dateMonth + "/" + dateDay + " " + dateHour
            }
        } catch (_: Exception) {
            null
        }
    }
}
