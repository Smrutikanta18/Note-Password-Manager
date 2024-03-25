package com.example.noteandpasswordmanager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditNoteActivity : AppCompatActivity() {
    private lateinit var medittitleofnote: EditText
    private lateinit var meditcontentofnote: EditText
    private lateinit var msaveeditnote: FloatingActionButton

    private lateinit var data: Intent
    private lateinit var documentReference:DocumentReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        medittitleofnote = findViewById(R.id.edittitleofnote)
        meditcontentofnote = findViewById(R.id.editcontentofnote)
        msaveeditnote = findViewById(R.id.saveeditnote)

        data = intent

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        val toolbar = findViewById<Toolbar>(R.id.toolbarofeditnote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        msaveeditnote.setOnClickListener {
            val newtitle = medittitleofnote.text.toString()
            val newcontent = meditcontentofnote.text.toString()

            if (newtitle.isEmpty() || newcontent.isEmpty()) {
                Toast.makeText(applicationContext, "Something is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                documentReference = data.getStringExtra("noteId")?.let { it1 ->
                    firebaseFirestore.collection("notes")
                        .document(firebaseUser.uid)
                        .collection("myNotes")
                        .document(it1)
                }!!
                val note: MutableMap<String, Any> = HashMap()
                note["title"] = newtitle
                note["content"] = newcontent
                if (documentReference != null) {
                    documentReference.set(note)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "Note is updated", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@EditNoteActivity, NotesActivity::class.java))
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(applicationContext, "Failed To update", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        val notetitle = data.getStringExtra("title")
        val notecontent = data.getStringExtra("content")
        meditcontentofnote.setText(notecontent)
        medittitleofnote.setText(notetitle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
