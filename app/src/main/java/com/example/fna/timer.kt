package com.example.fna

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class timer {
    private lateinit var mtimer : Timer
    private var defaultsecond by Delegates.notNull<Int>()
    private var nowsecond = 0

    fun gettimer(): Timer {
        return mtimer
    }

    fun settimer(temp : Timer) {
        mtimer = temp
    }

    fun setdefaultsecond(second : Int){
        defaultsecond = second
    }

    fun getdefaultsecond(): Int {
        return defaultsecond
    }

    fun decsecond() {
        defaultsecond--
    }

    fun getnowsecond(): Int {
        return nowsecond
    }

    fun setnowsecond(second: Int){
        nowsecond = second
    }
}