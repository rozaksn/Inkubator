package com.example.inkubator.notification

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.inkubator.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationService:Service() {
    private lateinit var database : FirebaseDatabase
    private lateinit var notificationSet: NotificationSet

    override fun onCreate() {
        super.onCreate()

        waterLevelNotify()
        detectionNotify()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun waterLevelNotify(){
        database = FirebaseDatabase.getInstance()
        notificationSet = NotificationSet(this)

        // Menambahkan ValueEventListener untuk mengetahui prubshsn pada node REPTIL
        val reference = database.getReference("WATER_LEVEL")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val level = snapshot.child("water_level").value.toString()
                notificationSet.sendWaterLevelNotification(level)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })
    }

     fun detectionNotify(){
        database = FirebaseDatabase.getInstance()
        notificationSet = NotificationSet(this)

        // Menambahkan ValueEventListener untuk mengetahui perubahan pada node detection
        val reference = database.getReference("detection")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val detection = snapshot.child("object_name").value.toString()
                val confidence = snapshot.child("confidence").value.toString().toFloat()

                notificationSet.sendDetectionNotification(detection,confidence)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })

    }




    /*
    private fun showPopup(detection: String, confidence: Float) {
        Handler(Looper.getMainLooper()).post {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Deteksi Objek")
            builder.setMessage("Objek: $detection\nConfidence: $confidence")

            // Tombol OK pada pop-up
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: android.app.AlertDialog = builder.create()

            // Menampilkan pop-up
            dialog.show()
        }





    }

     */
}
