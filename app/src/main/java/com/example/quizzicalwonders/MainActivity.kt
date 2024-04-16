package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var btnTextvicePractice:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTextvicePractice=findViewById(R.id.btnPractice)

        btnTextvicePractice.setOnClickListener {
            startActivity(Intent(this,Quizz::class.java))
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        // Add flags to clear the back stack and prevent the user from navigating back to Quizz activity
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Finish th
    }
}