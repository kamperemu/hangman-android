package com.example.hangman

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hide systemUI at beginning of creation
        hideSystemUI()

        MobileAds.initialize(this@MainActivity)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        // opens game activity if play button is clicked
        val playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener {
            val intent = Intent(this, game::class.java)
            startActivity(intent)
            mInterstitialAd.show()
            finish()
        }
        // opens credit activity if credits button is clicked
        val creditButton = findViewById<Button>(R.id.credit_button)
        creditButton.setOnClickListener {
            val intent = Intent(this, Credits::class.java)
            startActivity(intent)
            mInterstitialAd.show()
            finish()
        }

    }








    // common functions - i have copied and pasted cause i don't know another way to do this right now


    // hide systemUI as long as window has focus
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    // hides status bar and navigation bar
    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    // flip animation on changing
    override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.grow_from_middle,R.anim.shrink_to_middle);
        var player = MediaPlayer.create(this,R.raw.transition)
        player.start()
    }
}