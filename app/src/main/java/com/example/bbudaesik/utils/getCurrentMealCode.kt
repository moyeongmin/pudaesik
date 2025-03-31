package com.example.bbudaesik.utils

import java.util.Calendar

fun getCurrentMealCode(): String {
    val now = Calendar.getInstance()
    val hour = now.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 8..10 -> "조식"
        in 11..16 -> "중식"
        in 17..24 -> "석식"
        else -> "조기"
    }
}
