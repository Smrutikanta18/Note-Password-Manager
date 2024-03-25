package com.example.noteandpasswordmanager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class CreateActivity : AppCompatActivity() {

    private lateinit var mCreateTitleOfNote: EditText
    private lateinit var mCreateContentOfNote: EditText
    private lateinit var mSaveNote: FloatingActionButton
    private lateinit var mProgressBarOfCreateNote: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        mSaveNote = findViewById(R.id.savenote)
        mCreateContentOfNote = findViewById(R.id.createcontentofnote)
        mCreateTitleOfNote = findViewById(R.id.createtitleofnote)
        mProgressBarOfCreateNote = findViewById(R.id.progressbarofcreatenote)
        val toolbar = findViewById<Toolbar>(R.id.toolbarofcreatenote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        mSaveNote.setOnClickListener {
            val title = mCreateTitleOfNote.text.toString().trim()
            val content = mCreateContentOfNote.text.toString().trim()
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill all the field", Toast.LENGTH_SHORT).show()
            } else {
                mProgressBarOfCreateNote.visibility = View.VISIBLE

                val documentReference: DocumentReference =
                    firebaseFirestore.collection("notes").document(firebaseUser.uid).collection("myNotes").document()
                val note: MutableMap<String, Any> = HashMap()
                note["title"] = title
                note["content"] = content

                documentReference.set(note)
                    .addOnSuccessListener {
                        Toast.makeText(applicationContext, "Note Created Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, NotesActivity::class.java))
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "Failed To Create Note ${e.message}", Toast.LENGTH_SHORT).show()
                        mProgressBarOfCreateNote.visibility = View.INVISIBLE
                    }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
