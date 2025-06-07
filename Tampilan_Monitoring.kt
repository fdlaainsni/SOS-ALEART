package com.example.sosaleart

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MonitoringActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var suhuRef: DatabaseReference

    // TextView untuk menampilkan suhu tiap ruangan
    private lateinit var lantai1ruang1Suhu: TextView
    private lateinit var lantai1ruang2Suhu: TextView
    private lateinit var lantai1ruang3Suhu: TextView
    private lateinit var lantai2ruang1Suhu: TextView
    private lateinit var lantai2ruang2Suhu: TextView
    private lateinit var lantai2ruang3Suhu: TextView
    private lateinit var lantai3ruang1Suhu: TextView
    private lateinit var lantai3ruang2Suhu: TextView
    private lateinit var lantai3ruang3Suhu: TextView

    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampilan_monitoring)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance()
        suhuRef = database.getReference("monitoring")

        // Inisialisasi TextView dari layout
        lantai1ruang1Suhu = findViewById(R.id.lantai1ruang1Suhu)
        lantai1ruang2Suhu = findViewById(R.id.lantai1ruang2Suhu)
        lantai1ruang3Suhu = findViewById(R.id.lantai1ruang3Suhu)
        lantai2ruang1Suhu = findViewById(R.id.lantai2ruang1Suhu)
        lantai2ruang2Suhu = findViewById(R.id.lantai2ruang2Suhu)
        lantai2ruang3Suhu = findViewById(R.id.lantai2ruang3Suhu)
        lantai3ruang1Suhu = findViewById(R.id.lantai3ruang1Suhu)
        lantai3ruang2Suhu = findViewById(R.id.lantai3ruang2Suhu)
        lantai3ruang3Suhu = findViewById(R.id.lantai3ruang3Suhu)

        // Inisialisasi tombol kembali
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Menutup activity ini dan kembali ke sebelumnya
        }

        // Mulai membaca data suhu dari Firebase
        readSuhuData()
    }

    private fun readSuhuData() {
        suhuRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Update setiap ruangan
                updateTextView(lantai1ruang1Suhu, snapshot, "lantai1", "ruang1")
                updateTextView(lantai1ruang2Suhu, snapshot, "lantai1", "ruang2")
                updateTextView(lantai1ruang3Suhu, snapshot, "lantai1", "ruang3")

                updateTextView(lantai2ruang1Suhu, snapshot, "lantai2", "ruang1")
                updateTextView(lantai2ruang2Suhu, snapshot, "lantai2", "ruang2")
                updateTextView(lantai2ruang3Suhu, snapshot, "lantai2", "ruang3")

                updateTextView(lantai3ruang1Suhu, snapshot, "lantai3", "ruang1")
                updateTextView(lantai3ruang2Suhu, snapshot, "lantai3", "ruang2")
                updateTextView(lantai3ruang3Suhu, snapshot, "lantai3", "ruang3")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Gagal membaca data: ", error.toException())
            }
        })
    }

    private fun updateTextView(textView: TextView, snapshot: DataSnapshot, lantai: String, ruangan: String) {
        val suhu = snapshot.child(lantai).child(ruangan).child("suhu").getValue(Float::class.java)
        textView.text = if (suhu != null) "$suhu °C" else "N/A"
    }

    companion object {
        private const val TAG = "MonitoringActivity"
    }
}
