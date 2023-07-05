package com.example.fna

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.fna.databinding.ActivityMainBinding
import java.lang.reflect.Field


class MainActivity () : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var backPressedTime : Long = 0
    private val songlist: Array<out Field> = R.raw::class.java.fields
    private var music = Music(this)

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

        val number = (songlist.indices).random()

        music.runsong(number)

        binding.presentsong.text = music.getsongname()

        binding.nextsong.setOnClickListener{
            music.nextsong()
            binding.presentsong.text = music.getsongname()
            binding.stopplaysong.setImageResource(R.drawable.stop)
        }

        binding.previoussong.setOnClickListener {
            music.previousong()
            binding.presentsong.text = music.getsongname()
            binding.stopplaysong.setImageResource(R.drawable.stop)
        }

        binding.stopplaysong.setOnClickListener {
            if (!music.getismediaplayerpaused()) {
                binding.stopplaysong.setImageResource(R.drawable.play)
                onPause()
            }

            else if (music.getismediaplayerpaused()) {
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