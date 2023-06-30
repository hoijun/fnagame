package com.example.fna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.fragment.app.FragmentActivity
import com.example.fna.databinding.ActivityMainchangeBinding
import com.google.android.material.tabs.TabLayoutMediator

class mainchange : AppCompatActivity() {
    private lateinit var binding: ActivityMainchangeBinding

    // 탭 레이아웃 항목 이름
    private val tabTextList = listOf("그림 맞추기", "짝 맞추기")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}