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