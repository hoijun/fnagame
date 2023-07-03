package com.example.fna

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fna.databinding.ActivityMainBinding
import java.lang.reflect.Field


class MainActivity () : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer
    private var ismediaplayerpaused = false
    private var backPressedTime : Long = 0
    private val songlist: Array<out Field> = R.raw::class.java.fields
    private var number = 0
    private var nextclick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomjpgindex = (0..8).random()
        val fnaimages = resources.obtainTypedArray(R.array.fromis9)
        binding.startimage.setImageDrawable(fnaimages.getDrawable(randomjpgindex))
        YoYo.with(Techniques.Bounce).duration(1240).repeat(1240).playOn(binding.fnagame)
        YoYo.with(Techniques.Bounce).duration(1240).repeat(1240).playOn(binding.startimage)
        binding.fnalogo.setOnClickListener {
            YoYo.with(Techniques.Swing).duration(800).repeat(1).playOn(binding.fnalogo)
        }
        number = (songlist.indices).random()

        runsong(number)

        binding.nextsong.setOnClickListener{
            nextsong()
            binding.stopplaysong.setImageResource(R.drawable.stop)
        }

        binding.previoussong.setOnClickListener {
            previousong()
            binding.stopplaysong.setImageResource(R.drawable.stop)
        }

        binding.stopplaysong.setOnClickListener {
            if (!ismediaplayerpaused) {
                binding.stopplaysong.setImageResource(R.drawable.play)
                onPause()
            }

            else if (ismediaplayerpaused) {
                binding.stopplaysong.setImageResource(R.drawable.stop)
                onResume()
            }
        }

        // 게임 선택창으로 이동
        binding.btnselectgame.setOnClickListener {
            val intent = Intent(this, mainchange::class.java)
            intent.putExtra("song",songlist[number].getInt(songlist[number]))
            startActivity(intent)
        }
    }

    private fun runsong(number : Int) {
        var n = number
        while (MediaPlayer.create(this, songlist[n].getInt(songlist[n])) == null && nextclick) {
            n += 1
        }
        while (MediaPlayer.create(this, songlist[n].getInt(songlist[n])) == null && !nextclick) {
            n -= 1
        }
        mediaPlayer = MediaPlayer.create(this, songlist[n].getInt(songlist[n]))
        mediaPlayer.isLooping = false
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            nextsong()
        }
        
        binding.presentsong.text = songlist[n].name + ".."
        this.number = n
    }

    private fun nextsong() {
        nextclick = true
        mediaPlayer.stop()
        if (number == songlist.size - 1) {
            number = 0
            runsong(number)
        }
        else {
            number += 1
            runsong(number)
        }
        ismediaplayerpaused = false
    }

    private fun previousong() {
        nextclick = false
        mediaPlayer.stop()
        if (number == 0) {
            number = songlist.size - 1
            runsong(number)
        }
        else {
            number -= 1
            runsong(number)
        }
        ismediaplayerpaused = false
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