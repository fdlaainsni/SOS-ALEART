package com.example.sosaleart

import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

class DashboardActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 1001
    }

    private lateinit var logoImage: ImageView
    private lateinit var titleDashboard: TextView
    private lateinit var logoLayout: LinearLayout
    private lateinit var logoIcon: ImageView
    private lateinit var logoText: TextView
    private lateinit var titleEmergencyButton: Button
    private lateinit var titleMonitoring: Button
    private lateinit var titleDoorControl: Button
    private lateinit var titleAboutApp: Button
    private lateinit var titleProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampilan_dashboard) // Pastikan nama layout Anda benar

        // Inisialisasi elemen UI berdasarkan ID
        logoImage = findViewById(R.id.logoImage)
        titleDashboard = findViewById(R.id.titleDashboard)
        logoLayout = findViewById(R.id.logoLayout)
        logoIcon = findViewById(R.id.logoIcon)
        logoText = findViewById(R.id.logoText)
        titleEmergencyButton = findViewById(R.id.titleEmergency)
        titleMonitoring = findViewById(R.id.titleMonitoring)
        titleDoorControl = findViewById(R.id.titleDoorControl)
        titleAboutApp = findViewById(R.id.titleAboutApp)
        titleProfile = findViewById(R.id.titleProfile)

        titleEmergencyButton.setOnClickListener {
            val intent = Intent(this, EmergencyActivity::class.java)
            startActivity(intent)
        }

        titleMonitoring.setOnClickListener {
            val intent = Intent(this, MonitoringActivity::class.java)
            startActivity(intent)
        }

        titleDoorControl.setOnClickListener {
            val intent = Intent(this, DoorControlActivity::class.java)
            startActivity(intent)
        }

        titleAboutApp.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }

        titleProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Minta permission POST_NOTIFICATIONS untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }

        // Subscribe ke topic FCM "fire_alert"
        FirebaseMessaging.getInstance().subscribeToTopic("fire_alert")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to fire_alert topic")
                } else {
                    Log.w("FCM", "Failed to subscribe to fire_alert topic", task.exception)
                }
            }

        // Ambil token FCM dan log
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "FCM Registration Token: $token")
                    // Kirim token ke server jika perlu
                } else {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                }
            }
    }

    // Jika ingin, override onRequestPermissionsResult untuk handle hasil permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "POST_NOTIFICATIONS granted")
            } else {
                Log.w("Permission", "POST_NOTIFICATIONS denied")
            }
        }
    }
}
