package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var register=findViewById<Button>(R.id.Registerbuttom)

        register.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}