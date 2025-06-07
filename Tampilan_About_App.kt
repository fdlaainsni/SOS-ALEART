package com.example.sosaleart

import android.os.Bundle
import android.widget.Button // Import Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutAppActivity : AppCompatActivity() {

    private lateinit var textViewAboutTitle: TextView
    private lateinit var textViewAboutDescription: TextView
    private lateinit var backButton: Button // Deklarasikan backButton di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        textViewAboutTitle = findViewById(R.id.textViewAboutTitle)
        textViewAboutDescription = findViewById(R.id.textViewAboutDescription)

        // Inisialisasi Button Back dan atur OnClickListener
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
