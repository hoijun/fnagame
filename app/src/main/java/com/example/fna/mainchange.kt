package com.example.fna

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.fna.databinding.ActivityMainchangeBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.reflect.Field

class mainchange : AppCompatActivity() {
    private lateinit var binding: ActivityMainchangeBinding
    private val songlist: Array<out Field> = R.raw::class.java.fields
    private var music = Music(this)

    // 탭 레이아웃 항목 이름
    private val tabTextList = listOf("그림 맞추기", "짝 맞추기")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = (songlist.indices).random()
        music.runsong(number)

        // 뷰페이저에 프래그먼트 연결?
        binding.viewPager01.apply {
            adapter = pageradapter(context as FragmentActivity)
        }
        
        // 뷰페이저와 탭 레이아웃 연결 및 탭 레이아웃 항목 이름 적용
        TabLayoutMediator(binding.tabLayout01, binding.viewPager01) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }

    //뒤로가기 버튼 누르면 처음화면으로
    override fun onBackPressed() {
        val songnumber = intent.getIntExtra("song", 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("resong", songnumber)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
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
}