package com.example.noteandpasswordmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var login:TextView
    private lateinit var forgot:Button
    private lateinit var recoverymail:EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        login=findViewById(R.id.gobacktologin)
        forgot=findViewById(R.id.passwordrecoverbutton)
        recoverymail=findViewById(R.id.forgotpassword)
        firebaseAuth=FirebaseAuth.getInstance()
        supportActionBar?.hide()

        login.setOnClickListener {
                    val intent = Intent(this, NoteActivity::class.java)
                    startActivity(intent)
        }
        forgot.setOnClickListener {
                    val mail = recoverymail.text.toString().trim()
                    if (mail.isEmpty()) {
                        Toast.makeText(applicationContext, "Enter your mail first", Toast.LENGTH_SHORT).show()
                    } else {
                        // Send password recovery email
                        firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener{
                            if(it.isSuccessful){
                                Toast.makeText(this,"Mail send, you can now change your password ",Toast.LENGTH_LONG).show()
                                finish()
                                startActivity(Intent(this,NoteActivity::class.java))
                            }
                            else{
                                Toast.makeText(this,"Email is wrong or not exists ",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }
}



