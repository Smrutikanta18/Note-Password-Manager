package com.example.noteandpasswordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PasswordActivity : AppCompatActivity() {
    private lateinit var GetStarted:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        GetStarted =findViewById(R.id.btnGetStarted)

        GetStarted.setOnClickListener {
            startActivity(Intent(this, GeneratePassword::class.java))
        }
    }
}