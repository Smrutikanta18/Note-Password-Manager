package com.example.noteandpasswordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.noteandpasswordmanager.R.*


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( layout.activity_main)
        button=findViewById(id.btnGetStarted)


        button.setOnClickListener{
            val intent1=Intent(this, MainActivity2::class.java)
            startActivity(intent1)
        }
    }
}