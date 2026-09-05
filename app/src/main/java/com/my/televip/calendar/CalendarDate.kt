package com.my.televip.calendar

import com.my.televip.language.Keys
import com.my.televip.language.Translator
import ir.huri.jcal.JalaliCalendar
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.Calendar

class CalendarDate {

    @JvmField val year: Int
    @JvmField val month: Int
    @JvmField val monthName: String
    @JvmField val day: Int

    constructor(hijrahDate: HijrahDate) {
        day = hijrahDate.get(ChronoField.DAY_OF_MONTH)
        month = hijrahDate.get(ChronoField.MONTH_OF_YEAR)
        year = hijrahDate.get(ChronoField.YEAR)
        val hijriMonths = arrayOf(
            Translator.get(Keys.HijriMonthMuharram),
            Translator.get(Keys.HijriMonthSafar),
            Translator.get(Keys.HijriMonthRabiAlAwal),
            Translator.get(Keys.HijriMonthRabiAlThani),
            Translator.get(Keys.HijriMonthJumadaAlAwal),
            Translator.get(Keys.HijriMonthJumadaAlThani),
            Translator.get(Keys.HijriMonthRajab),
            Translator.get(Keys.HijriMonthShaban),
            Translator.get(Keys.HijriMonthRamadan),
            Translator.get(Keys.HijriMonthShawwal),
            Translator.get(Keys.HijriMonthDhulQidah),
            Translator.get(Keys.HijriMonthDhulHijjah)
        )
        monthName = hijriMonths[month - 1]
    }

    constructor(jalaliCalendar: JalaliCalendar) {
        year = jalaliCalendar.year
        month = jalaliCalendar.month
        day = jalaliCalendar.day
        val persianMonths = arrayOf(
            Translator.get(Keys.PersianMonthFarvardin),
            Translator.get(Keys.PersianMonthOrdibehesht),
            Translator.get(Keys.PersianMonthKhordad),
            Translator.get(Keys.PersianMonthTir),
            Translator.get(Keys.PersianMonthMordad),
            Translator.get(Keys.PersianMonthShahrivar),
            Translator.get(Keys.PersianMonthMehr),
            Translator.get(Keys.PersianMonthAban),
            Translator.get(Keys.PersianMonthAzar),
            Translator.get(Keys.PersianMonthDey),
            Translator.get(Keys.PersianMonthBahman),
            Translator.get(Keys.PersianMonthEsfand)
        )
        monthName = persianMonths[month - 1]
    }

    constructor(calendar: Calendar) {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_WEEK)
        val gregorianMonths = arrayOf(
            Translator.get(Keys.GregorianMonthJanuary),
            Translator.get(Keys.GregorianMonthFebruary),
            Translator.get(Keys.GregorianMonthMarch),
            Translator.get(Keys.GregorianMonthApril),
            Translator.get(Keys.GregorianMonthMay),
            Translator.get(Keys.GregorianMonthJune),
            Translator.get(Keys.GregorianMonthJuly),
            Translator.get(Keys.GregorianMonthAugust),
            Translator.get(Keys.GregorianMonthSeptember),
            Translator.get(Keys.GregorianMonthOctober),
            Translator.get(Keys.GregorianMonthNovember),
            Translator.get(Keys.GregorianMonthDecember)
        )
        monthName = gregorianMonths[month - 1]
    }
}
