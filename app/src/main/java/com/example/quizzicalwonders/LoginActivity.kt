package com.example.quizzicalwonders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleLoginButton = findViewById<SignInButton>(R.id.googleLogin)
        googleLoginButton.setOnClickListener {
            signInWithGoogle()
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
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Google Sign-In success")
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                    Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(TAG, "Google Sign-In canceled or failed, Result Code: $resultCode")
            }
            Log.d(TAG, "Google Sign-In canceled or failed, Result Code: $resultCode")

        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Log.d(TAG, "User is signed in")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.d(TAG, "User is signed out")
        }
    }
    companion object {
        private const val RC_SIGN_IN = 9001
    }

}
