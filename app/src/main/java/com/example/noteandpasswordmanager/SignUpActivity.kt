package com.example.noteandpasswordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {
    private lateinit var login: TextView
    private lateinit var signup: RelativeLayout
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        login = findViewById(R.id.gotologin)
        signup = findViewById(R.id.signup)
        email = findViewById(R.id.signupemail)
        password = findViewById(R.id.signuppassword)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        login.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        signup.setOnClickListener {
            val mail = email.text.toString().trim()
            val password = password.text.toString().trim()

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "All Fields are Required", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 7) {
                Toast.makeText(
                    applicationContext,
                    "Password Should be Greater than 7 Digits",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        sendEmailVerification();
                    } else {
                        Toast.makeText(this, "Failed To Register", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private fun sendEmailVerification() {
        val firebaseUser = firebaseAuth.currentUser
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Verification Email is Sent,Verify and Log In Again",
                Toast.LENGTH_SHORT
            ).show()
            firebaseAuth.signOut()
            finish()
            startActivity(Intent(this, NoteActivity::class.java))
        }
            ?: Toast.makeText(
                applicationContext,
                "Failed To Send Verification Email",
                Toast.LENGTH_SHORT
            ).show()
    }
}