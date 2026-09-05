package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.calendar.CalendarDate
import com.my.televip.calendar.ConverterCalendar
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.messenger.LocaleController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object HijriDate {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        if (ConfigManager.customCalendar.getCustomCalendar() == 0) return
        try {
            if (!isEnable) {
                isEnable = true
                val localeClass = ClassLoad.getClass(ClassNames.LOCALE_CONTROLLER)
                if (localeClass != null) {
                    HMethod.hookMethod(
                        localeClass,
                        AutomationResolver.resolve("LocaleController", "formatYearMont", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("formatYearMont", arrayOf<Class<*>>(Long::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.customCalendar.getCustomCalendar() == 0) return
                                    val date = param.args[0] as Long
                                    val alwaysShowYear = param.args[1] as Boolean
                                    val yearMont = formatYearMont(date, alwaysShowYear)
                                    if (!yearMont.isNullOrEmpty()) {
                                        param.result = yearMont
                                    }
                                }
                            }
                        )
                    )
                }

                val fastDateFormatClass = ClassLoad.getClass(ClassNames.FAST_DATE_FORMAT)
                if (fastDateFormatClass != null) {
                    HMethod.hookMethod(
                        fastDateFormatClass,
                        AutomationResolver.resolve("FastDateFormat", "format", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("format", arrayOf<Class<*>>(Long::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.customCalendar.getCustomCalendar() == 0) return
                                    val org = param.result as? String
                                    val date = param.args[0] as Long
                                    val formatDate = formatDate(date, org)
                                    if (!formatDate.isNullOrEmpty()) {
                                        param.result = formatDate
                                    }
                                }
                            }
                        )
                    )

                    HMethod.hookMethod(
                        fastDateFormatClass,
                        AutomationResolver.resolve("FastDateFormat", "format", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("format", arrayOf<Class<*>>(Calendar::class.java)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.customCalendar.getCustomCalendar() == 0) return
                                    val org = param.result as? String
                                    val calendar = param.args[0] as Calendar
                                    val formatDate = formatDate(calendar, org)
                                    if (!formatDate.isNullOrEmpty()) {
                                        param.result = formatDate
                                    }
                                }
                            }
                        )
                    )

                    HMethod.hookMethod(
                        fastDateFormatClass,
                        AutomationResolver.resolve("FastDateFormat", "format", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("format", arrayOf<Class<*>>(Date::class.java)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    if (ConfigManager.customCalendar.getCustomCalendar() == 0) return
                                    val org = param.result as? String
                                    val date = param.args[0] as Date
                                    val formatDate = formatDate(date, org)
                                    if (!formatDate.isNullOrEmpty()) {
                                        param.result = formatDate
                                    }
                                }
                            }
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    @JvmStatic
    fun formatDate(date: Any?, org: String?): String? {
        try {
            val type = detectFormatType(org)
            if (type == DateFormatType.UNKNOWN || type == DateFormatType.TIME_ONLY) return org

            var dateCalendar: CalendarDate? = null
            val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
            var dateHour: String? = null

            when (date) {
                is Long -> {
                    dateCalendar = ConverterCalendar.toCalendar(date)
                    dateHour = sdfTime.format(Date(date))
                }
                is Calendar -> {
                    dateCalendar = ConverterCalendar.toCalendar(date)
                    dateHour = sdfTime.format(date.time)
                }
                is Date -> {
                    dateCalendar = ConverterCalendar.toCalendar(date)
                    dateHour = sdfTime.format(date)
                }
            }

            if (dateCalendar == null) return null

            val dateDay = dateCalendar.day
            val dateYear = dateCalendar.year
            val dateMonth = dateCalendar.month
            val dateMonthName = dateCalendar.monthName

            return when (type) {
                DateFormatType.DAY_MONTH -> if (LocaleController.isRTL()) "$dateDay $dateMonthName" else "$dateMonthName $dateDay"
                DateFormatType.DAY_MONTH_YEAR -> if (LocaleController.isRTL()) "$dateDay $dateMonthName $dateYear" else "$dateMonthName $dateDay $dateYear"
                DateFormatType.TIME_DAY_MONTH_YEAR -> if (LocaleController.isRTL()) "$dateDay $dateMonthName $dateYear $dateHour" else "$dateMonthName $dateDay $dateYear $dateHour"
                DateFormatType.NUMERIC_DATE -> "$dateYear.$dateMonth.$dateDay"
                else -> null
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return null
    }

    @JvmStatic
    fun formatYearMont(dateParam: Long, alwaysShowYear: Boolean): String? {
        var date = dateParam
        try {
            date *= 1000

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            var rightNow = ConverterCalendar.toCalendar(calendar)
            val year = rightNow.year

            rightNow = ConverterCalendar.toCalendar(date)
            val dateYear = rightNow.year
            val dateMonthName = rightNow.monthName

            return if (year == dateYear && !alwaysShowYear) {
                dateMonthName
            } else {
                "$dateMonthName $dateYear"
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
        return null
    }

    enum class DateFormatType {
        TIME_ONLY,
        DAY_MONTH,
        DAY_MONTH_YEAR,
        TIME_DAY_MONTH_YEAR,
        NUMERIC_DATE,
        UNKNOWN
    }

    @JvmStatic
    fun detectFormatType(value: String?): DateFormatType {
        if (value.isNullOrEmpty()) return DateFormatType.UNKNOWN

        val v = value.trim()
        if (v.matches(Regex("\\d{1,4}[./]\\d{1,2}[./]\\d{1,4}"))) {
            return DateFormatType.NUMERIC_DATE
        }

        val hasTime = v.matches(Regex(".*\\d{1,2}:\\d{2}.*"))
        val withoutTime = v.replace(Regex("\\p{L}*\\s*\\d{1,2}:\\d{2}(:\\d{2})?\\s*\\p{L}*"), "").trim()

        val hasYear = withoutTime.matches(Regex(".*\\b\\d{4}\\b.*"))
        val hasDay = withoutTime.matches(Regex(".*\\b\\d{1,2}\\b.*"))
        val hasMonthText = withoutTime.matches(Regex(".*\\p{L}{2,}.*"))

        return when {
            hasTime && !hasDay && !hasMonthText -> DateFormatType.TIME_ONLY
            !hasTime && hasDay && hasMonthText && !hasYear -> DateFormatType.DAY_MONTH
            !hasTime && hasDay && hasMonthText -> DateFormatType.DAY_MONTH_YEAR
            hasTime && hasDay && hasMonthText -> DateFormatType.TIME_DAY_MONTH_YEAR
            else -> DateFormatType.UNKNOWN
        }
    }
}
