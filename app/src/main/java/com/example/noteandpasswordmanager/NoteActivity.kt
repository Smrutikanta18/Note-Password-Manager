package com.example.noteandpasswordmanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class NoteActivity : AppCompatActivity() {
    private lateinit var signUp:RelativeLayout
    private lateinit var login:RelativeLayout
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var forgot:TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mprogressbarofmainactivity:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        signUp = findViewById(R.id.gotosignup)
        login = findViewById(R.id.login)
        email = findViewById(R.id.loginemail)
        password = findViewById(R.id.loginpassword)
        forgot = findViewById(R.id.gotoforgotpassword)
        mprogressbarofmainactivity = findViewById(R.id.progressbarofmainactivity)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        supportActionBar?.hide()

        if (firebaseUser != null) {
            finish()
            startActivity(Intent(this, NotesActivity::class.java))
        }

        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        login.setOnClickListener {
            val mail = email.text.toString().trim()
            val password = password.text.toString().trim()

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "All Fields Are Required", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mprogressbarofmainactivity.visibility = View.VISIBLE

                firebaseAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            checkMailVerification()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Account Doesn't Exist",
                                Toast.LENGTH_SHORT
                            ).show()
                            mprogressbarofmainactivity.visibility = View.INVISIBLE
                        }
                    }

            }
        }

    }
    private fun checkMailVerification() {
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null && firebaseUser.isEmailVerified) {
            Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, NotesActivity::class.java))
        } else {
            mprogressbarofmainactivity.visibility = View.INVISIBLE
            Toast.makeText(applicationContext, "Verify your mail first", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }
    }


}
