package com.example.noteandpasswordmanager

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteandpasswordmanager.model.Note
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.integrity.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Random


class NotesActivity : AppCompatActivity() {
    private lateinit var add:FloatingActionButton
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var mrecyclerview: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var documentreference:DocumentReference

    var noteAdapter: FirestoreRecyclerAdapter<Note, NoteViewHolder>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        add=findViewById(R.id.createnotefab)
        firebaseAuth=FirebaseAuth.getInstance()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()

        supportActionBar?.title = "All Notes"
        add.setOnClickListener{
            startActivity(Intent(this,CreateActivity::class.java))
        }

        val query = firebaseFirestore.collection("notes").document(firebaseUser.uid)
            .collection("myNotes").orderBy("title", Query.Direction.ASCENDING)

        val allusernotes = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query, Note::class.java)
            .build()


        noteAdapter = object : FirestoreRecyclerAdapter<Note, NoteViewHolder>(allusernotes) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view=LayoutInflater.from(parent.context).inflate(R.layout.notes_layout,parent,false)
                return NoteViewHolder(view)
            }

            override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
                val popupbutton:ImageView=holder.itemView.findViewById(R.id.menupopbutton)
                val code = getRandomColor()
                holder.mnote.setBackgroundColor(holder.itemView.resources.getColor(code, null))
                holder.notetitle.text=model.title
                holder.notecontent.text=model.content
                val docId = noteAdapter?.snapshots?.getSnapshot(position)?.id


                holder.itemView.setOnClickListener{
                    val intent = Intent(it.context, NoteDetailActivity::class.java)
                    intent.putExtra("title", model.title)
                    intent.putExtra("content", model.content)
                    intent.putExtra("noteId", docId)
                    startActivity(intent)
                    //Toast.makeText(applicationContext,"clicked",Toast.LENGTH_LONG).show()
                }

                popupbutton.setOnClickListener {
                    val popupMenu = PopupMenu(it.context, it)
                    popupMenu.gravity = Gravity.END
                    popupMenu.menu.add("Edit").setOnMenuItemClickListener { item ->
                        val intent = Intent(it.context, EditNoteActivity::class.java)
                        intent.putExtra("title", model.title)
                        intent.putExtra("content", model.content)
                        intent.putExtra("noteId", docId)
                        it.context.startActivity(intent)
                        false
                    }
                    popupMenu.menu.add("Delete").setOnMenuItemClickListener {
//                        item ->
//                        Toast.makeText(applicationContext,"Note deleted ",Toast.LENGTH_LONG).show()
                        val documentReference = docId?.let { it1 ->
                            firebaseFirestore.collection("notes")
                                .document(firebaseUser.uid)
                                .collection("myNotes")
                                .document(it1)
                        }

                        if (documentReference != null) {
                            documentReference.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "This note is deleted", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(applicationContext, "Failed To Delete", Toast.LENGTH_SHORT).show()
                                }
                        }

                        false
                    }
                    popupMenu.show()
                }


            }


        }
        mrecyclerview = findViewById(R.id.recyclerview)
        mrecyclerview.setHasFixedSize(true)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mrecyclerview.layoutManager = staggeredGridLayoutManager
        mrecyclerview.adapter = noteAdapter



    }

    private fun getRandomColor(): Int {
        val colorcode = mutableListOf(
            R.color.gray,
            R.color.pink,
            R.color.lightgreen,
            R.color.skyblue,
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.green
        )

        val random = Random()
        val number = random.nextInt(colorcode.size)
        return colorcode[number]


    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notetitle: TextView = itemView.findViewById(R.id.notetitle)
        val notecontent: TextView = itemView.findViewById(R.id.notecontent)
        val mnote: LinearLayout = itemView.findViewById(R.id.note)

        init {
            val translate_anim = AnimationUtils.loadAnimation(itemView.context, R.anim.anim)
            mnote.startAnimation(translate_anim)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                firebaseAuth.signOut()
                finish()
                startActivity(Intent(this, NoteActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        noteAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter?.stopListening()
    }


}

