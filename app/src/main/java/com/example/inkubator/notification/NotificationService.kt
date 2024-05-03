package com.example.inkubator.notification

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.inkubator.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationService:Service() {
    private lateinit var database : FirebaseDatabase
    private lateinit var notificationSetup: NotificationSetup

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
        notificationSetup = NotificationSetup(this)

        // Menambahkan ValueEventListener untuk mengetahui prubshsn pada node REPTIL
        val reference = database.getReference("WATER_LEVEL")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val level = snapshot.child("water_level").value.toString()
                notificationSetup.sendWaterLevelNotification(level)
                //Log.d(TAG,"Water Level Notification")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })
    }

     fun detectionNotify(){
        database = FirebaseDatabase.getInstance()
        notificationSetup = NotificationSetup(this)

        // Menambahkan ValueEventListener untuk mengetahui perubahan pada node detection
        val reference = database.getReference("detection")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val detection = snapshot.child("object_name").value.toString()
                val confidence = snapshot.child("confidence").value.toString().toFloat()

                notificationSetup.sendDetectionNotification(detection,confidence)

                //Log.d(TAG,"Object Detection Notification")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })

    }
}
