package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val forgetPasswordTextView = findViewById<TextView>(R.id.textView)
        forgetPasswordTextView.setOnClickListener {
            showResetPasswordDialog()
        }


        auth = FirebaseAuth.getInstance()

        val registerButton = findViewById<Button>(R.id.Register)
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        val loginButton = findViewById<Button>(R.id.button)
        loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = findViewById<TextInputEditText>(R.id.email_edit).text.toString()
        val password = findViewById<TextInputEditText>(R.id.password_edit).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showResetPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.reset_password_dialog, null)
        val resetEmailEdit = dialogView.findViewById<EditText>(R.id.reset_email_edit)
        val resetPasswordButton = dialogView.findViewById<Button>(R.id.reset_password_button)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Reset Password")
            .setCancelable(true)
            .create()

        resetPasswordButton.setOnClickListener {
            val email = resetEmailEdit.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


}
