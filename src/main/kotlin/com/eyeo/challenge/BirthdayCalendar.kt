package com.eyeo.challenge

import com.eyeo.challenge.utils.StdIn.SEPARATOR
import com.eyeo.challenge.utils.StdIn.readInput
import com.eyeo.challenge.utils.StdOut.printLine
import com.eyeo.challenge.utils.DateUtils
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object BirthdayCalendar {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val currentDate = DateUtils.getCurrentDate()
    private val currentMonth = currentDate.monthValue
    private val nextMonth = currentDate.plusMonths(1).monthValue
    private val afterNextMonth = currentDate.plusMonths(2).monthValue

    fun printBirthdayPartyCalendar() {
        readInput()
            // transform to list of pair<birthday, name>
            .map { line ->
                val (birthday, name) = line.split(SEPARATOR)
                birthday.toNextWeekendDay() to name
            }
            // filter birthday on next month
            // keep also last day of current month if saturday
            // keep also 1st of after next month if sunday
            .filter {
                it.first?.run {
                    monthValue == nextMonth
                            || (monthValue == currentMonth && dayOfWeek == DayOfWeek.SATURDAY && dayOfMonth == lengthOfMonth())
                            || (monthValue == afterNextMonth && dayOfWeek == DayOfWeek.SUNDAY && dayOfMonth == 1)
                } ?: false
            }
            // transform to map<birthday, list<name>>
            .groupBy({ it.first }, { it.second })
            .also { birthdayMap ->
                birthdayMap
                    // merge birthday on same weekend
                    .map {
                        val currentBirthday = it.key
                        val names = it.value
                        val dayAfterBirthday = currentBirthday?.plusDays(1)
                        val newBirthday =
                            if (currentBirthday?.dayOfWeek == DayOfWeek.SATURDAY
                                && birthdayMap.containsKey(dayAfterBirthday)
                            ) {
                                dayAfterBirthday
                            } else {
                                currentBirthday
                            }
                        newBirthday to names
                    }
                    // filter birthday on next month
                    .filter { it.first?.monthValue == nextMonth }
                    // sort by date
                    .sortedBy { it.first }
                    // group birthday on same day
                    .groupBy({ it.first }, { it.second })
                    // print output
                    .forEach { (birthday, names) ->
                        val birthdayOutput = birthday?.format(dateFormatter)
                        val namesOutput = names.flatten().sorted().joinToString(", ")
                        printLine("$birthdayOutput $namesOutput")
                    }
            }
    }

    private fun String.toNextWeekendDay() =
        LocalDate.parse(this, dateFormatter)
            // update day to march 1st if birthday is february 29th and current is not leap
            ?.let { day ->
                if (!currentDate.isLeapYear && day.month == Month.FEBRUARY && day.dayOfMonth == 29) {
                    day.plusDays(1)
                } else {
                    day
                }
            }
            // update date with current year (or next year if before current day)
            ?.let {
                val dayForCurrentYear = it.withYear(currentDate.year)
                if (dayForCurrentYear.isBefore(currentDate)) {
                    dayForCurrentYear.plusYears(1)
                } else {
                    dayForCurrentYear
                }
            }
            // update date to next saturday if current day is before weekend
            ?.let { day ->
                if (day.dayOfWeek < DayOfWeek.SATURDAY) {
                    day.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
                } else {
                    day
                }
            }
}


