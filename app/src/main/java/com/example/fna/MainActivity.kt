package com.example.fna

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fna.databinding.ActivityMainBinding

private lateinit var binding:ActivityMainBinding
private lateinit var mediaPlayer: MediaPlayer
private var ismediaplayerpaused = false

private var backPressedTime : Long = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomjpgindex = (0..8).random()
        val fnaimages = resources.obtainTypedArray(R.array.fromis9)
        binding.startimage.setImageDrawable(fnaimages.getDrawable(randomjpgindex))
        YoYo.with(Techniques.Bounce).duration(1240).repeat(124).playOn(binding.fnagame)
        YoYo.with(Techniques.Bounce).duration(1240).repeat(124).playOn(binding.startimage)
        binding.fnalogo.setOnClickListener {
            YoYo.with(Techniques.Swing).duration(800).repeat(1).playOn(binding.fnalogo)
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.talkandtalk)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        // 게임 선택창으로 이동
        binding.btnselectgame.setOnClickListener { 
            val intent = Intent(this, mainchange::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        //2.5초이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
            return
        }
        Toast.makeText(this, "한번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            ismediaplayerpaused = true
        }
    }

    override fun onResume(){
        super.onResume()
        if (ismediaplayerpaused) {
            mediaPlayer.start();
            ismediaplayerpaused = false;
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}