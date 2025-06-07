package com.example.sosaleart

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.database.*

class DoorControlActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var doorStatusTextViews: List<TextView>
    private lateinit var doorSwitches: List<SwitchCompat>
    private lateinit var backButton: Button

    // List untuk menyimpan referensi ValueEventListener agar bisa dihapus saat Activity dihancurkan
    private val firebaseListeners = mutableListOf<ValueEventListener>()

    // Flag untuk mencegah loop tak terbatas saat switch diupdate secara programatis dari Firebase
    private var isSwitchUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door_control)

        database = FirebaseDatabase.getInstance().reference

        // Inisialisasi TextViews dan Switches
        doorStatusTextViews = listOf(
            findViewById(R.id.door1StatusTextView),
            findViewById(R.id.door2StatusTextView),
            findViewById(R.id.door3StatusTextView),
            findViewById(R.id.door4StatusTextView),
            findViewById(R.id.door5StatusTextView),
            findViewById(R.id.door6StatusTextView),
            findViewById(R.id.door7StatusTextView),
            findViewById(R.id.door8StatusTextView),
            findViewById(R.id.door9StatusTextView)
        )

        doorSwitches = listOf(
            findViewById(R.id.door1ControlSwitch),
            findViewById(R.id.door2ControlSwitch),
            findViewById(R.id.door3ControlSwitch),
            findViewById(R.id.door4ControlSwitch),
            findViewById(R.id.door5ControlSwitch),
            findViewById(R.id.door6ControlSwitch),
            findViewById(R.id.door7ControlSwitch),
            findViewById(R.id.door8ControlSwitch),
            findViewById(R.id.door9ControlSwitch)
        )

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        setupFirebaseListeners()
        setupSwitchListeners()
    }

    // Fungsi pembantu untuk mendapatkan lantai dan ruang dari indeks 0-8
    private fun getLantaiRuang(index: Int): Pair<String, String> {
        val lantai = (index / 3) + 1 // Lantai 1, 2, atau 3
        val ruang = (index % 3) + 1  // Ruang 1, 2, atau 3
        return Pair("lantai$lantai", "ruang$ruang")
    }

    private fun setupFirebaseListeners() {
        for (i in doorStatusTextViews.indices) {
            val (lantai, ruang) = getLantaiRuang(i)
            val doorPath = "pintu/$lantai/$ruang/status" // Jalur Firebase yang benar

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isSwitchUpdating = true // Set flag agar switch tidak memicu update Firebase

                    val statusValue = snapshot.getValue(String::class.java)

                    if (statusValue != null) {
                        // Sesuaikan dengan status "BUKA" atau "TUTUP" dari ESP32
                        val displayStatus = if (statusValue == "BUKA") "BUKA" else "TUTUP"
                        doorStatusTextViews[i].text = displayStatus
                        doorSwitches[i].isChecked = (statusValue == "BUKA")
                    } else {
                        doorStatusTextViews[i].text = "N/A"
                        doorSwitches[i].isChecked = false
                    }
                    isSwitchUpdating = false // Reset flag
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DoorControlActivity", "Gagal membaca data pintu $lantai/$ruang: ", error.toException())
                    isSwitchUpdating = false // Pastikan flag direset bahkan saat error
                }
            }
            // Tambahkan listener ke jalur spesifik setiap pintu
            database.child(doorPath).addValueEventListener(valueEventListener)
            firebaseListeners.add(valueEventListener) // Simpan listener untuk dihapus nanti
        }
    }

    private fun setupSwitchListeners() {
        for (i in doorSwitches.indices) {
            val (lantai, ruang) = getLantaiRuang(i)
            val doorPath = "pintu/$lantai/$ruang/status" // Jalur Firebase yang benar
            val doorRef = database.child(doorPath)

            doorSwitches[i].setOnCheckedChangeListener { _, isChecked ->
                if (!isSwitchUpdating) { // Pastikan perubahan bukan dari update Firebase
                    // Kirim status "BUKA" atau "TUTUP" ke Firebase
                    val firebaseStatus = if (isChecked) "BUKA" else "TUTUP"

                    // Langsung update TextView untuk respons instan (opsional, Firebase akan update lagi)
                    doorStatusTextViews[i].text = firebaseStatus

                    doorRef.setValue(firebaseStatus)
                        .addOnSuccessListener {
                            Log.d("DoorControlActivity", "Status pintu $lantai/$ruang berhasil diupdate ke $firebaseStatus")
                        }
                        .addOnFailureListener { e ->
                            Log.e("DoorControlActivity", "Gagal mengupdate status pintu $lantai/$ruang: ${e.message}")
                            // Jika gagal, kembalikan switch ke status sebelumnya (opsional)
                            // doorSwitches[i].isChecked = !isChecked
                        }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hapus semua listener Firebase saat Activity dihancurkan untuk mencegah memory leak
        for (i in firebaseListeners.indices) {
            val (lantai, ruang) = getLantaiRuang(i)
            val doorPath = "pintu/$lantai/$ruang/status"
            database.child(doorPath).removeEventListener(firebaseListeners[i])
        }
    }
}
