package com.example.fna

import android.content.Context
import com.example.fna.databinding.ActivityGame1Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class timer {
    companion object {
        lateinit var mytimer : Timer
        private var defaultsecond by Delegates.notNull<Int>()
        private var nowsecond = 0

        fun gettimer(): Timer {
            return mytimer
        }

        fun settimer(temp: Timer) {
            mytimer = temp
        }

        fun setdefaultsecond(second: Int) {
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

        fun setnowsecond(second: Int) {
            nowsecond = second
        }

        fun game1Timer(delay: Long, acti: Context, binding: ActivityGame1Binding) {
            this.settimer(timer(initialDelay = delay, period = 1000) {
                CoroutineScope(Dispatchers.Main).launch {
                    // 시간이 0이되면 타이머 중지하고 다이얼로그 실행
                    if (getdefaultsecond() == 0) {
                        gettimer().cancel()
                        val customdialog2 = wronganswerdialog(acti, "시간 종료!")
                        customdialog2.show()
                        binding.texttimer.text = "시간: 0"
                    }
                    else {
                        // 시간을 1초씩 줄임
                        binding.texttimer.text = "시간: ${getdefaultsecond()}"
                        decsecond()
                    }
                }
            })
        }
    }
}