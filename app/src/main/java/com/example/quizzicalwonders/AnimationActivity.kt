package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.airbnb.lottie.LottieAnimationView

class AnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        val animationView = findViewById<LottieAnimationView>(R.id.animationView)
        animationView.setAnimation(R.raw.animation)
        animationView.playAnimation()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@AnimationActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
        }, 3000)
    }
}