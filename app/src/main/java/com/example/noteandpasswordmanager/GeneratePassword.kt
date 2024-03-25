package com.example.noteandpasswordmanager

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthMeter
import java.util.Random

class GeneratePassword : AppCompatActivity() {
    private lateinit var PasswordInputMeter : PasswordStrengthMeter
    private lateinit var UpperCase: CheckBox
    private lateinit var LowerCase:CheckBox
    private lateinit var Symble:CheckBox
    private lateinit var NumberChek:CheckBox
    private lateinit var Generate:EditText
    private lateinit var SliderStrength:Slider
    private lateinit var GeneratePass:Button
    private lateinit var GenPass:EditText
    private lateinit var Copy:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_passsword)

        var passwordLength = 13
        PasswordInputMeter = findViewById(R.id.passwordInputMeter)
        UpperCase = findViewById(R.id.cbUppercase)
        LowerCase = findViewById(R.id.cbLowercase)
        Symble = findViewById(R.id.cbSymbols)
        NumberChek = findViewById(R.id.cbNumbers)
        SliderStrength = findViewById(R.id.sliderPasswordStrength)
        GeneratePass = findViewById(R.id.btnGeneratePassword)
        GenPass = findViewById(R.id.etGeneratedPassword)
        Copy = findViewById((R.id.btnCopyPassword))
        Generate=findViewById(R.id.etGeneratedPassword)

        PasswordInputMeter.setPasswordStrengthCalculator(object :
            PasswordStrengthCalculator {
            override fun calculatePasswordSecurityLevel(password: String?): Int {
                if (password != null) {
                    return getPasswordScore(password)
                } else {
                    return 0
                }
            }

            override fun getMinimumLength(): Int {
                return 1
            }

            override fun passwordAccepted(level: Int): Boolean {
                return true
            }

            override fun onPasswordAccepted(password: String?) {

            }
        })

        val generatedPassword = generatePassword(passwordLength,
            includeUpperCaseLetters = UpperCase.isChecked,
            includeLowerCaseLetters = LowerCase.isChecked,
            includeSymbols = Symble.isChecked,
            includeNumbers = NumberChek.isChecked)

        PasswordInputMeter.setEditText(Generate)
        Generate.setText(generatedPassword)

        SliderStrength.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                passwordLength = slider.value.toInt()
            }



        })
        SliderStrength.addOnChangeListener(object: Slider.OnChangeListener{
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                slider.setLabelFormatter(LabelFormatter {
                    return@LabelFormatter  "${value.toInt()} Letters"
                })
            }
        })

        GeneratePass.setOnClickListener {

            val generatedPassword = generatePassword(passwordLength,
                includeUpperCaseLetters = UpperCase.isChecked,
                includeLowerCaseLetters = LowerCase.isChecked,
                includeSymbols = Symble.isChecked,
                includeNumbers = NumberChek.isChecked)

            if(generatedPassword.isBlank()) {
                if(passwordLength==0) {
                    Toast.makeText(this,"Password length cannot be zero",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Please check atleast one item",Toast.LENGTH_LONG).show()
                }
            } else {
                GenPass.setText(generatedPassword)
            }
        }

        Copy.setOnClickListener {
            val clipboard: ClipboardManager? = ContextCompat.getSystemService(
                this,
                ClipboardManager::class.java
            )
            val clip = ClipData.newPlainText("Generated Password", GenPass.text.toString())
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(this,"Password Copied to Clipboard",Toast.LENGTH_LONG).show()
        }


    }

    private fun getPasswordScore(password: String): Int {
        if(password.isEmpty() || password.isBlank()) {
            return 0
        } else {
            if(password.length == 0) {
                return 0
            } else if (password.length in 1..3) {
                return 1
            } else if (password.length in 4..6) {
                return 2
            } else if (password.length in 7..9) {
                return 3
            } else if (password.length in 10..12) {
                return 4
            } else {
                return 5
            }
        }
    }

    fun <E> MutableList<E>.mRandom(): E? = if (size > 0) get(Random().nextInt(size)) else null

    private fun generatePassword(length: Int,includeUpperCaseLetters : Boolean,includeLowerCaseLetters : Boolean,
                                 includeSymbols : Boolean,  includeNumbers: Boolean) : String {
        var password = ""
        val list  = mutableListOf<Int>()
        if(includeUpperCaseLetters)
            list.add(0)
        if(includeLowerCaseLetters)
            list.add(1)
        if(includeNumbers)
            list.add(2)
        if(includeSymbols)
            list.add(3)

        for(i in 1..length){
            when(list.toMutableList().mRandom()){
                0-> password += ('A'..'Z').toMutableList().mRandom().toString()
                1-> password += ('a'..'z').toMutableList().mRandom().toString()
                2-> password += ('0'..'9').toMutableList().mRandom().toString()
                3-> password += listOf('!','@','#','$','%','&','*','+','=','-','~','?','/','_').toMutableList().mRandom().toString()
            }
        }
        return password
    }
}