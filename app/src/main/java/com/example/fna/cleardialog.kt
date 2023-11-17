package com.example.fna

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.fna.databinding.ActivityMydialog3Binding

class cleardialog(context: Context) : Dialog(context) {
    private lateinit var binding: ActivityMydialog3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMydialog3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 다이얼로그 배경 투명
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 크기 조절
        window!!.setLayout(1250, LayoutParams.WRAP_CONTENT)

        // imageview에 gif 파일 적용
        Glide.with(this.context).load(R.raw.perfect).into(binding.perfectGif)

        binding.btnyes.setOnClickListener {
            dismiss()
            timer.gettimer().cancel()
            val intent = Intent(this.context, mainchange::class.java)
            this.context.startActivity(intent) // 게임 선택창으로 이동
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