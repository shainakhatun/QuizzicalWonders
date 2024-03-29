package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var btnTextvicePractice:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTextvicePractice=findViewById(R.id.btnPractice)

        btnTextvicePractice.setOnClickListener {
            startActivity(Intent(this,Quizz::class.java))
        }
    }
}