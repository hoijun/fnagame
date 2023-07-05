package com.example.fna

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.fna.databinding.ActivityMydialogBinding
import java.util.*
import kotlin.concurrent.timer


class mydialog(context: Context) : Dialog(context) {
    private lateinit var binding: ActivityMydialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMydialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다이얼로그 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 크기 조절
        window!!.setLayout(1250, LayoutParams.WRAP_CONTENT)

        binding.solvedquiztext.text = "맞춘 문제: ${game1().getsolvenum()}"

        // imageview에 gif 파일 적용
        Glide.with(this.context).load(R.raw.lowflover).into(binding.hafloverGif)

        binding.btnyes.setOnClickListener{
            game1().getgame1timer().gettimer().cancel() // 타이머 중지
            val intent = Intent(this.context, mainchange::class.java) 
            this.context.startActivity(intent) // 게임 선택 창으로 이동
        }

        binding.btnno.setOnClickListener {
            dismiss() // 다이얼로그 닫음
            game1().getgame1timer().setdefaultsecond(game1().getgame1timer().getnowsecond())// 타이머의 남은 시간 저장
            game1().getgame1timer().gettimer().cancel() // 타이머 중지
            game1().funTimer(500, this.context) // 저장한 남은 시간으로 다시 타이머 시작
        }
    }

    override fun onBackPressed() {
        return
    }

    override fun show() {
        super.show()
        this.setCancelable(false)
    }
}