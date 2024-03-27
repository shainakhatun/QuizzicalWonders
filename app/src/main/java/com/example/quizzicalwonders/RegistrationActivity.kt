package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        val loginButton = findViewById<Button>(R.id.Loginbutton)
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val registerButton = findViewById<Button>(R.id.Registers)
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = findViewById<TextInputEditText>(R.id.email_edit).text.toString()
        val password = findViewById<TextInputEditText>(R.id.password_edit).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
