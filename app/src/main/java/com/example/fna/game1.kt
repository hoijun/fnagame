package com.example.fna

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fna.databinding.ActivityGame1Binding
import com.example.fna.fnagamematerials.Companion.fnakeywords
import java.util.*
import kotlin.concurrent.timer

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityGame1Binding
lateinit var mtimer : Timer
private var backPressedTime : Long = 0
var defaultsecond = 5
var nowsecond = 0
var solvequiznum = 0

class game1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)
        binding = ActivityGame1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.raw.dancinbbang).into(binding.dancebbang)

        val fnamemimages = arrayOf(
            resources.obtainTypedArray(R.array.saerom),
            resources.obtainTypedArray(R.array.gyuri),
            resources.obtainTypedArray(R.array.hayoung),
            resources.obtainTypedArray(R.array.jiwon),
            resources.obtainTypedArray(R.array.jisun),
            resources.obtainTypedArray(R.array.seoyeon),
            resources.obtainTypedArray(R.array.chaeyoung),
            resources.obtainTypedArray(R.array.nagyung),
            resources.obtainTypedArray(R.array.jiheon),
        )

        binding.solvedquiz.text = "맞힌 문제: $solvequiznum"
        // 타이머 시작 시간
        defaultsecond = 3
        // 타이머 시작
        funTimer(0, this)

        var randomkeysindex = mutableListOf<Int>()
        randomkeysindex = getrandomnodup(randomkeysindex, 3, 8)

        var randomimagesindex = mutableListOf<Int>()
        randomimagesindex = getrandomdup(randomimagesindex, 3, 2)

        val randommemnameindex = (0..3).random()

        val imageViewarray = arrayOf(binding.imageView1, binding.imageView2, binding.imageView3)
        var randomimageviewarrayindex = mutableListOf<Int>()
        randomimageviewarrayindex = getrandomnodup(randomimageviewarrayindex, 3, 2)

        // 배열로 부터 키워드를 가져와 키워드 텍스트뷰에 적용
        val key = fnainfo(fnakeywords[randomkeysindex[0]], fnamemimages[randomkeysindex[0]])
        val fake1 = fnainfo(fnakeywords[randomkeysindex[1]], fnamemimages[randomkeysindex[1]])
        val fake2 = fnainfo(fnakeywords[randomkeysindex[2]], fnamemimages[randomkeysindex[2]])

        binding.keyword.text = key.getkeyword(randommemnameindex)
        imageViewarray[randomimageviewarrayindex[0]].setImageDrawable(key.getimage(randomimagesindex[0]))
        imageViewarray[randomimageviewarrayindex[1]].setImageDrawable(
            fake1.getimage(
                randomimagesindex[1]
            )
        )
        imageViewarray[randomimageviewarrayindex[2]].setImageDrawable(
            fake2.getimage(
                randomimagesindex[2]
            )
        )

        if(solvequiznum > 0 && solvequiznum % 10 == 0){
            Handler().postDelayed({
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[0])
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[1])
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[2])
            }, 1000) // 0.5초 정도 딜레이를 준 후 시작
        }

        imageViewarray[randomimageviewarrayindex[0]].setOnClickListener {
            mtimer.cancel()
            solvequiznum++
            val intent = Intent(this, game1::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);

        }
        imageViewarray[randomimageviewarrayindex[1]].setOnClickListener {
            mtimer.cancel()
            val customdialog2 = mydialog2(this,"오답!")
            customdialog2.show()
        }
        imageViewarray[randomimageviewarrayindex[2]].setOnClickListener {
            mtimer.cancel()
            val customdialog2 = mydialog2(this, "오답!")
            customdialog2.show()
        }

        binding.btnquitgame.setOnClickListener {
            nowsecond = defaultsecond // 타이머 남은 시간 저장
            mtimer.cancel() // 타이머 중지
            val customdialog = mydialog(this)
            customdialog.show() // 다이얼로그 실행
        }

    }

    // 종료 버튼이랑 동일
    override fun onBackPressed() {
        nowsecond = defaultsecond
        mtimer.cancel()
        val customdialog = mydialog(this)
        customdialog.show()
    }

    @SuppressLint("SetTextI18n")
    // 타이머 동작
    fun funTimer(delay: Long, acti: Context) {
        mtimer = timer(initialDelay = delay, period = 1000) {
            runOnUiThread {
                // 시간이 0이되면 타이머 중지하고 다이얼로그 실행
                if (defaultsecond == 0) {
                    mtimer.cancel()
                    val customdialog2 = mydialog2(acti, "시간 종료!")
                    customdialog2.show()
                    binding.texttimer.text = "시간: 0"
                }
                else {
                    // 시간을 1초씩 줄임
                    binding.texttimer.text = "시간: $defaultsecond"
                    defaultsecond--
                }
            }
        }
    }

    fun getrandomnodup(list: MutableList<Int>, size: Int, range: Int): MutableList<Int> {
        while (list.size < size) {
            val tempint = (0..range).random()
            if (list.contains(tempint))
                continue
            list.add(tempint)
        }
        return list
    }

    fun getrandomdup(list: MutableList<Int>, size: Int, range: Int): MutableList<Int> {
        for (i: Int in 0 until size)
            list.add((0..range).random())
        return list
    }
}