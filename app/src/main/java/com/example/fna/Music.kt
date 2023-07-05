package com.example.fna

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field

class Music(private var context : Context) : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private var ismediaplayerpaused = false
    private val songlist: Array<out Field> = R.raw::class.java.fields
    private var number = 0
    private var nextclick = true

    fun runsong(number : Int) {
        var n = number
        while (MediaPlayer.create(this.context, songlist[n].getInt(songlist[n])) == null && nextclick) {
            n += 1
        }
        while (MediaPlayer.create(this.context, songlist[n].getInt(songlist[n])) == null && !nextclick) {
            n -= 1
        }
        mediaPlayer = MediaPlayer.create(this.context, songlist[n].getInt(songlist[n]))
        mediaPlayer.isLooping = false
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            nextsong()
        }

        this.number = n
    }

    fun getsongname() : String {
        return songlist[number].name + ".."
    }

    fun nextsong() {
        nextclick = true
        mediaPlayer.stop()
        if (number == songlist.size - 1) {
            number = 0
            runsong(number)
        }
        else {
            number += 1
            runsong(number)
        }
        ismediaplayerpaused = false
    }

    fun previousong() {
        nextclick = false
        mediaPlayer.stop()
        if (number == 0) {
            number = songlist.size - 1
            runsong(number)
        }
        else {
            number -= 1
            runsong(number)
        }
        ismediaplayerpaused = false
    }

    fun getismediaplayerpaused() : Boolean {
        return ismediaplayerpaused
    }

    fun setismediaplayerpaused(value : Boolean) {
        ismediaplayerpaused = value
    }

    fun getnumber() : Int {
        return number
    }

    fun getplayer() : MediaPlayer {
        return mediaPlayer
    }
}