package com.example.bbudaesik.utils

import android.icu.util.Calendar

data class WeekInfo(
    val dates: List<Int>,
    val currentDate: Int,
)

fun getDate(): WeekInfo {
    val calendar = Calendar.getInstance()
    val currentDate = calendar.get(Calendar.DAY_OF_MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_WEEK)

    calendar.add(Calendar.DAY_OF_MONTH, -(currentDay - 1))

    val weekDates = mutableListOf<Int>()

    for (i in 0..6) {
        weekDates.add(calendar.get(Calendar.DAY_OF_MONTH))
        calendar.add(Calendar.DAY_OF_MONTH, 1) // 다음 날로 이동
    }

    return WeekInfo(
        dates = weekDates,
        currentDate = currentDate,
    )
}
