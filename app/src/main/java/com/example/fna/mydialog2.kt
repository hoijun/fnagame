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
import com.example.fna.databinding.ActivityMydialog2Binding
import java.security.cert.CertPathValidatorException.Reason
import java.util.*

class mydialog2(context: Context, reason: String) : Dialog(context) {
    private lateinit var binding: ActivityMydialog2Binding
    private var endreason = reason
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMydialog2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(1250, LayoutParams.WRAP_CONTENT)
        Glide.with(this.context).load(R.raw.cromsae).into(binding.cromsaeGif)
        binding.solvedquiztext.text = "맞춘 문제: $solvequiznum"
        binding.endreason.text = endreason
        binding.btnregame.setOnClickListener{
            solvequiznum = 0
            val intent = Intent(this.context, game1::class.java)
            this.context.startActivity(intent) // 게임 재시작
        }
        binding.btnend.setOnClickListener {
            solvequiznum = 0
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