package com.example.fna

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class pageradapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    // 슬라이드 페이지 갯수
    override fun getItemCount(): Int = 2

    // 포지션에 따른 프래그먼트 정보 반환
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> firstgamepage()
            else -> secondgamepage()
        }
    }
}