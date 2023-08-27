package com.example.fna

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fna.databinding.ActivityGame1Binding
import com.example.fna.fnagamematerials.Companion.fnakeywords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import kotlin.concurrent.timer
import kotlin.math.abs
import kotlin.random.Random


private var game1timer = timer()
private lateinit var binding: ActivityGame1Binding

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
    private var x1 = 0f
    private var y1 = 0f
    private var x2 = 0f
    private var y2 = 0f
    private var x3 = 0f
    private var y3 = 0f
    private lateinit var imageViewlist : List<ImageView>
    private var ismoving1 = false
    private var ismoving2 = false
    private var ismoving3 = false
    private val job1 = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1)
        binding = ActivityGame1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.raw.dancinbbang).into(binding.dancebbang)
        val number = (songlist.indices).random()
        music.runsong(number)
        x1 = binding.imageView1.translationX
        y1 = binding.imageView1.translationY
        x2 = binding.imageView2.translationX
        y2 = binding.imageView2.translationY
        x3 = binding.imageView3.translationX
        y3 = binding.imageView3.translationY
        imageViewlist = listOf(binding.imageView1, binding.imageView2, binding.imageView3)
        ori1x = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
        ori1y = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
        ori2x = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
        ori2y = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
        ori3x = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
        ori3y = if (Random(System.currentTimeMillis()).nextInt(2) == 0) -3 else 3
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

        game1timer.setdefaultsecond(3)
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
        setimageviewindex(
            imageViewlist,
            randomimageviewarrayindex,
            randomimagesindex,
            listOf(key, fake1, fake2)
        )

        if (solvequiznum in 1..30 && solvequiznum % 10 == 0) {
            binding.keyword.text = fake1.getkeyword(randommemnameindex) + " 말고 " + key.getkeyword(
                fakerandommemnameindex
            )
        }

        if (solvequiznum in 21..100 && solvequiznum % 10 == 0) {
            if (solvequiznum > 50)
                binding.keyword.text =
                    fake1.getkeyword(randommemnameindex) + " 말고 " + key.getkeyword(
                        fakerandommemnameindex
                    )
            if (solvequiznum == 71) {
                for(view in imageViewlist) {
                    view.layoutParams.height = resources.getDimension(R.dimen.size_100dp).toInt()
                    view.layoutParams.width = resources.getDimension(R.dimen.size_100dp).toInt()
                }
                CoroutineScope(Dispatchers.Main + job1).launch {
                    delay(100)
                    moveimage1()
                    moveimage2()
                    moveimage3()
                }
            }
            if (solvequiznum == 100) {
                game1timer.gettimer().cancel() // 타이머 중지
                val customdialog = mydialog3(this)
                customdialog.show() // 다이얼로그 실행
            }
            Handler(Looper.getMainLooper()).postDelayed({
                Glide.with(this).load(R.raw.itsme).into(imageViewlist[0])
                Glide.with(this).load(R.raw.itsme).into(imageViewlist[1])
                Glide.with(this).load(R.raw.itsme).into(imageViewlist[2])
            }, 1000)
        }




        imageViewlist[randomimageviewarrayindex[0]].setOnClickListener {
            onCorrectImageClick()
        }

        imageViewlist[randomimageviewarrayindex[1]].setOnClickListener {
            onWrongImageClick()
        }

        imageViewlist[randomimageviewarrayindex[2]].setOnClickListener {
            onWrongImageClick()
        }

        binding.btnquitgame.setOnClickListener {
            onQuitGameClick()
        }
    }

    // 이미지뷰에 이미지 할당
    private fun setimageviewindex(imageViewList:List<ImageView>, imagearrayIndexList:List<Int>, imageIndexList:List<Int>, infolist : List<fnainfo>) {
        for(i in imageViewList.indices) {
            imageViewList[imagearrayIndexList[i]].setImageDrawable(infolist[i].getimage(imageIndexList[i]))
        }
    }

    private fun onCorrectImageClick() {
        binding.imageView1.translationX = x1
        binding.imageView1.translationY = y1
        binding.imageView2.translationX = x2
        binding.imageView2.translationY = y2
        binding.imageView3.translationX = x3
        binding.imageView3.translationY = y3
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
        binding.imageView1.translationX = x1
        binding.imageView1.translationY = y1
        binding.imageView2.translationX = x2
        binding.imageView2.translationY = y2
        binding.imageView3.translationX = x3
        binding.imageView3.translationY = y3
        super.onPause()
        ismoving1 = false
        ismoving2 = false
        ismoving3 = false
        job1.cancel()
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
        job1.cancel()
        music.getplayer().stop();
        music.getplayer().release();
    }

    private fun moveimage1() {
        if (ismoving1) return
        ismoving1 = true
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                moveimagecondition(binding.imageView1, ori1x, ori1y, System.currentTimeMillis())
                delay(3)
            }
        }
    }

    private fun moveimage2() {
        if (ismoving2) return
        ismoving2 = true
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                moveimagecondition(binding.imageView2, ori2x, ori2y, System.currentTimeMillis())
                delay(3)
            }
        }
    }

    private fun moveimage3() {
        if (ismoving3) return
        ismoving3 = true
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                moveimagecondition(binding.imageView3, ori3x, ori3y, System.currentTimeMillis())
                delay(3)
            }
        }
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
        val collisionlist = checkCollision(imageView)
        val check = collisionlist[0] as Boolean
        var collisiontop: Boolean
        var collisionleft: Boolean
        var collisionDetected = false

        if (check) {
            collisiontop = false
            collisionleft = false
            val other = collisionlist[1] as ImageView

            if (abs(imageView.bottom - other.top) < 15 || abs(imageView.top - other.bottom) < 15)
                collisiontop = true
            if (abs(imageView.left - other.right) < 15 || abs(imageView.right - other.left) < 15)
                collisionleft = true

            if ((collisiontop && collisionleft) || (!collisiontop && !collisionleft))
            else if (collisiontop) {
                oriy *= -1
                collisionDetected = true
            } else if (collisionleft) {
                orix *= -1
                collisionDetected = true
            }
        }

        if (currentLeft <= 0 || currentLeft >= rightBoundary) {
            if (!collisionDetected)
                orix *= -1
        }

        if (currentTop <= 0 || currentTop >= bottomBoundary) {
            if (!collisionDetected)
                oriy *= -1
        }

        newLeft += orix
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

        val availableLeft = newLeft.coerceIn(0, rightBoundary)
        val availableTop = newTop.coerceIn(0, bottomBoundary)

        imageView.layout(availableLeft, availableTop, availableLeft + imageView.width, availableTop + imageView.height)
    }

    private fun checkCollision(imageView: ImageView): List<Any>{
        for (otherView in imageViewlist) {
            if (otherView != imageView) {
                if (Rect.intersects(imageView.getBounds(), otherView.getBounds())) {
                    return listOf(true, otherView) // 충돌 발생
                }
            }
        }
        return listOf(false) // 충돌 없음
    }

    private fun ImageView.getBounds(): Rect {
        val rect = Rect()
        getDrawingRect(rect)
        rect.offset(left, top)
        return rect
    }

    // 타이머 동작
     fun funTimer(delay: Long, acti: Context) {
        game1timer.settimer(timer(initialDelay = delay, period = 1000) {
            CoroutineScope(Dispatchers.Main).launch {
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