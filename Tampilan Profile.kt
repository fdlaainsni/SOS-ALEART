package com.example.sosaleart

import android.os.Bundle
import android.widget.Button // Import Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileTitle: TextView
    private lateinit var nameFadila: TextView
    private lateinit var nimFadila: TextView
    private lateinit var classFadila: TextView
    private lateinit var imageFadila: CircleImageView
    private lateinit var nameGeosevi: TextView
    private lateinit var nimGeosevi: TextView
    private lateinit var classGeosevi: TextView
    private lateinit var imageGeosevi: CircleImageView
    private lateinit var backButton: Button // Deklarasikan backButton di sini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi View untuk Fadila
        profileTitle = findViewById(R.id.profileTitle)
        nameFadila = findViewById(R.id.nameFadila)
        nimFadila = findViewById(R.id.nimFadila)
        classFadila = findViewById(R.id.classFadila)
        imageFadila = findViewById(R.id.imageFadila)

        // Inisialisasi View untuk Geosevi
        nameGeosevi = findViewById(R.id.nameGeosevi)
        nimGeosevi = findViewById(R.id.nimGeosevi)
        classGeosevi = findViewById(R.id.classGeosevi)
        imageGeosevi = findViewById(R.id.imageGeosevi)

        // Inisialisasi Button Back dan atur OnClickListener
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Metode ini akan menutup Activity saat ini dan kembali ke Activity sebelumnya di stack
        }
    }
}
