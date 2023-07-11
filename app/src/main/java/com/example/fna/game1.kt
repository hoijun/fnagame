package com.example.fna

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fna.databinding.ActivityGame1Binding
import com.example.fna.fnagamematerials.Companion.fnakeywords
import java.lang.reflect.Field
import kotlin.concurrent.timer
import kotlin.random.Random
import kotlin.random.nextInt


private var game1timer = timer()
private lateinit var binding: ActivityGame1Binding
private var moveHandler1: Handler? = null
private var moveHandler2: Handler? = null
private var moveHandler3: Handler? = null

class game1 : AppCompatActivity() {
    private val songlist: Array<out Field> = R.raw::class.java.fields
    private var music = Music(this)
    private var solvequiznum = 0
    private var ori1x = 5
    private var ori1y = 5
    private var ori2x = 5
    private var ori2y = 5
    private var ori3x = 5
    private var ori3y = 5
    private var x = 0f
    private var y = 0f
    private var ismoving1 = false
    private var ismoving2 = false
    private var ismoving3 = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)
        binding = ActivityGame1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.raw.dancinbbang).into(binding.dancebbang)
        val number = (songlist.indices).random()
        music.runsong(number)
        x = binding.imageView1.translationX
        y = binding.imageView1.translationY

        gaming() // 게임 시작
    }

    // 게임 로직
    private fun gaming() {
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

        game1timer.setdefaultsecond(30)
        funTimer(0, this)

        val randomkeysindex = getrandomnodup(mutableListOf(), 3, 8)
        val randomimagesindex = getrandomdup(mutableListOf(), 3, 2)

        val randommemnameindex = (0..3).random()
        val fakerandommemnameindex = (0..3).random()

        val randomimageviewarrayindex = getrandomnodup(mutableListOf(), 3, 2)

        // 배열로 부터 키워드를 가져와 키워드 텍스트뷰에 적용
        val key = fnainfo(fnakeywords[randomkeysindex[0]], fnamemimages[randomkeysindex[0]])
        val fake1 = fnainfo(fnakeywords[randomkeysindex[1]], fnamemimages[randomkeysindex[1]])
        val fake2 = fnainfo(fnakeywords[randomkeysindex[2]], fnamemimages[randomkeysindex[2]])

        binding.keyword.text = key.getkeyword(randommemnameindex)
        val imageViewarray = listOf(binding.imageView1, binding.imageView2, binding.imageView3)
        setimageviewindex(imageViewarray, randomimageviewarrayindex, randomimagesindex, listOf(key, fake1, fake2))

        if(solvequiznum in 1..10 && solvequiznum % 10 == 0){
            binding.keyword.text = fake1.getkeyword(randommemnameindex) + " 말고 " + key.getkeyword(fakerandommemnameindex)
        }

        if(solvequiznum in 11..30 && solvequiznum % 10 == 0){
            if (solvequiznum > 20)
                binding.keyword.text = fake1.getkeyword(randommemnameindex) + " 말고 " + key.getkeyword(fakerandommemnameindex)
            Handler(Looper.getMainLooper()).postDelayed({
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[0])
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[1])
                Glide.with(this).load(R.raw.itsme).into(imageViewarray[2])
            }, 1000)
        }

        imageViewarray[randomimageviewarrayindex[0]].setOnClickListener {
            onCorrectImageClick()
        }

        imageViewarray[randomimageviewarrayindex[1]].setOnClickListener {
            onWrongImageClick()
        }

        imageViewarray[randomimageviewarrayindex[2]].setOnClickListener {
            onWrongImageClick()
        }

        binding.btnquitgame.setOnClickListener {
            onQuitGameClick()
        }

        moveimage1()
        moveimage2()
        moveimage3()
    }

    // 이미지뷰에 이미지 할당
    private fun setimageviewindex(imageViewList:List<ImageView>, imagearrayIndexList:List<Int>, imageIndexList:List<Int>, infolist : List<fnainfo>) {
        for(i in imageViewList.indices) {
            imageViewList[imagearrayIndexList[i]].setImageDrawable(infolist[i].getimage(imageIndexList[i]))
        }
    }

    private fun onCorrectImageClick() {
        binding.imageView1.translationX = x
        binding.imageView1.translationY = y
        game1timer.gettimer().cancel()
        solvequiznum++
        gaming()
    }

    private fun onWrongImageClick() {
        game1timer.gettimer().cancel()
        val customdialog2 = mydialog2(this,"오답!")
        customdialog2.show()
    }

    private fun onQuitGameClick() {
        game1timer.setnowsecond(
            game1timer.getdefaultsecond()) // 타이머 남은 시간 저장
        game1timer.gettimer().cancel() // 타이머 중지
        val customdialog = mydialog(this)
        customdialog.show() // 다이얼로그 실행
    }

    private fun getrandomnodup(list: MutableList<Int>, size: Int, range: Int): MutableList<Int> {
        while (list.size < size) {
            val tempint = (0..range).random()
            if (list.contains(tempint))
                continue
            list.add(tempint)
        }
        return list
    }

    private fun getrandomdup(list: MutableList<Int>, size: Int, range: Int): MutableList<Int> {
        for (i: Int in 0 until size)
            list.add((0..range).random())
        return list
    }

    // 종료 버튼이랑 동일
    override fun onBackPressed() {
        game1timer.setnowsecond(
            game1timer.getdefaultsecond())
        game1timer.gettimer().cancel()
        val customdialog = mydialog(this)
        customdialog.show()
    }

    fun getgame1timer(): timer {
        return game1timer
    }

    fun getsolvenum(): Int {
        return solvequiznum
    }

    override fun onPause() {
        super.onPause()
        if (ismoving1) {
            ismoving1 = false
            moveHandler1?.removeCallbacksAndMessages(null)
            moveHandler1 = null
        }

        if (ismoving2) {
            ismoving2 = false
            moveHandler2?.removeCallbacksAndMessages(null)
            moveHandler2 = null
        }

        if (ismoving3) {
            ismoving3 = false
            moveHandler3?.removeCallbacksAndMessages(null)
            moveHandler3 = null
        }

        if (music.getplayer().isPlaying) {
            music.getplayer().pause();
            music.setismediaplayerpaused(true)
        }
    }

    override fun onResume(){
        super.onResume()
        if (music.getismediaplayerpaused()) {
            music.getplayer().start();
            music.setismediaplayerpaused(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        music.getplayer().stop();
        music.getplayer().release();
    }

    private fun moveimage1() {
        if (ismoving1) return
        ismoving1 = true
        moveHandler1 = Handler(Looper.getMainLooper())
        moveHandler1?.postDelayed(object : Runnable {
            override fun run() {
                runOnUiThread {
                    moveimagecondition(binding.imageView1, ori1x, ori1y, System.currentTimeMillis())
                    moveHandler1?.postDelayed(this, 3)
                }
            }
        }, 1)
    }

    private fun moveimage2() {
        if (ismoving2) return
        ismoving2 = true
        moveHandler2 = Handler(Looper.getMainLooper())
        moveHandler2?.postDelayed(object : Runnable {
            override fun run() {
                runOnUiThread {
                    moveimagecondition(binding.imageView2, ori2x, ori2y, System.currentTimeMillis())
                    moveHandler2?.postDelayed(this, 3)
                }
            }
        }, 1)
    }
    private fun moveimage3() {
        if (ismoving3) return
        ismoving3 = true
        moveHandler3 = Handler(Looper.getMainLooper())
        moveHandler3?.postDelayed(object : Runnable {
            override fun run() {
                runOnUiThread {
                    moveimagecondition(binding.imageView3, ori3x, ori3y, System.currentTimeMillis())
                    moveHandler3?.postDelayed(this, 3)
                }
            }
        }, 1)
    }
    private fun moveimagecondition(imageView: ImageView, tempx : Int, tempy : Int, seed : Long) {
        var orix = tempx
        var oriy = tempy
        val rightBoundary = binding.wall.width - imageView.width
        val bottomBoundary = binding.wall.height - imageView.height
        val currentLeft = imageView.left
        val currentTop = imageView.top
        var newLeft = currentLeft
        var newTop = currentTop

        val ran = Random(seed).nextInt(20)

        if (currentLeft <= 0) {
            orix *= -1
            newLeft += orix + ran
        } else if (currentLeft >= rightBoundary) {
            orix *= -1
            newLeft += orix - ran
        }
        else
            newLeft += orix

        if (currentTop <= 0) {
            oriy *= -1
            newTop += oriy + ran
        } else if(currentTop >= bottomBoundary) {
            oriy *= -1
            newTop += oriy - ran
        }
        else
            newTop += oriy


        when (imageView) {
            binding.imageView1 -> {
                ori1x = orix
                ori1y = oriy
            }

            binding.imageView2 -> {
                ori2x = orix
                ori2y = oriy
            }

            binding.imageView3 -> {
                ori3x = orix
                ori3y = oriy
            }
        }
        imageView.layout(newLeft, newTop, newLeft + imageView.width, newTop + imageView.height)
    }


    // 타이머 동작
     fun funTimer(delay: Long, acti: Context) {
        game1timer.settimer(timer(initialDelay = delay, period = 1000) {
            runOnUiThread {
                // 시간이 0이되면 타이머 중지하고 다이얼로그 실행
                if (game1timer.getdefaultsecond() == 0) {
                    game1timer.gettimer().cancel()
                    val customdialog2 = mydialog2(acti, "시간 종료!")
                    customdialog2.show()
                    binding.texttimer.text = "시간: 0"
                }
                else {
                    // 시간을 1초씩 줄임
                    binding.texttimer.text = "시간: ${game1timer.getdefaultsecond()}"
                    game1timer.decsecond()
                }
            }
        })
    }
}