package com.example.playlistmarket.unsorted

fun interface Listener {
    fun listen()
}

fun interface StartPlayerListener : Listener
fun interface PausePlayerListener : Listener
fun interface PlayerOnPreparedListener : Listener
fun interface PlayerOnCompletionListener : Listener
fun interface TimeFragmentListener : Listener

