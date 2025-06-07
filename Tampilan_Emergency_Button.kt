package com.example.sosaleart

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EmergencyActivity : AppCompatActivity() {

    private lateinit var btnSOS1: SwitchCompat
    private lateinit var btnSOS2: SwitchCompat
    private lateinit var textViewBuzzer: TextView
    private lateinit var textViewSpeaker: TextView
    private lateinit var backButton: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampilan_emergency_button)

        btnSOS1 = findViewById(R.id.btnSOS1)
        btnSOS2 = findViewById(R.id.btnSOS2)
        textViewBuzzer = findViewById(R.id.textViewBuzzer)
        textViewSpeaker = findViewById(R.id.textViewSpeaker)
        backButton = findViewById(R.id.backButton)

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().reference

        backButton.setOnClickListener { finish() }

        // Switch untuk Buzzer
        btnSOS1.setOnCheckedChangeListener { _, isChecked ->
            textViewBuzzer.setTextColor(ContextCompat.getColor(this, R.color.white))
            val buzzerValue = if (isChecked) "1" else "0" // "1" = ON, "0" = OFF
            database.child("buzzer").setValue(buzzerValue)
            Toast.makeText(this, "Buzzer Status: $buzzerValue", Toast.LENGTH_SHORT).show()
        }

        // Switch untuk Speaker
        btnSOS2.setOnCheckedChangeListener { _, isChecked ->
            textViewSpeaker.setTextColor(ContextCompat.getColor(this, R.color.white))
            val speakerValue = if (isChecked) "1" else "0" // "1" = ON, "0" = OFF
            database.child("speaker").setValue(speakerValue)
            Toast.makeText(this, "Speaker Status: $speakerValue", Toast.LENGTH_SHORT).show()
        }

        // (Opsional) Sinkronisasi status awal switch dengan Firebase
        database.child("buzzer").get().addOnSuccessListener {
            val value = it.getValue(String::class.java)
            btnSOS1.isChecked = value == "1"
        }

        database.child("speaker").get().addOnSuccessListener {
            val value = it.getValue(String::class.java)
            btnSOS2.isChecked = value == "1"
        }
    }
}
