package com.example.playlistmarket.utils

fun formatTextByNumbers(number: Int): String {
    val preLastDigit = number % 100 / 10
    if (preLastDigit == 1) return "$number треков"

    return when (number % 10) {
        1 -> "$number трек"
        2 -> "$number трека"
        3 -> "$number трека"
        4 -> "$number трека"
        else -> "$number треков"
    }
}

fun formatTextByMinutes(minutesCount: Int): String {
    val preLastDigit = minutesCount % 100 / 10
    if (preLastDigit == 1) return "$minutesCount минут"

    return when (minutesCount % 10) {
        1 -> "$minutesCount минута"
        2 -> "$minutesCount минуты"
        3 -> "$minutesCount минуты"
        4 -> "$minutesCount минуты"
        else -> "$minutesCount минут"
    }
}