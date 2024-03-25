package com.example.noteandpasswordmanager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var mtitleofnotedetail: TextView
    private lateinit var mcontentofnotedetail: TextView
    private lateinit var mgotoeditnote: FloatingActionButton
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        mtitleofnotedetail = findViewById(R.id.titleofnotedetail)
        mcontentofnotedetail = findViewById(R.id.contentofnotedetail)
        mgotoeditnote = findViewById(R.id.gotoeditnote)
        val toolbar = findViewById<Toolbar>(R.id.toolbarofnotedetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageView = findViewById(R.id.backGround)
        imageView.setImageResource(getRandomImageBackground())
        val data = intent

        mgotoeditnote.setOnClickListener {
            val intent = Intent(it.context, EditNoteActivity::class.java)
            intent.putExtra("title", data.getStringExtra("title"))
            intent.putExtra("content", data.getStringExtra("content"))
            intent.putExtra("noteId", data.getStringExtra("noteId"))
            it.context.startActivity(intent)
        }

        mcontentofnotedetail.text = data.getStringExtra("content")
        mtitleofnotedetail.text = data.getStringExtra("title")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getRandomImageBackground(): Int {
        val imageViews = intArrayOf(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4)
        val random = Random()
        val number = random.nextInt(imageViews.size)
        return imageViews[number]
    }
}
