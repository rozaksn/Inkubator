package com.example.inkubator.notification

import android.app.Service
import android.content.ContentValues
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
        val reference = database.getReference("TEST")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val level = snapshot.child("message").value.toString()
                if (level != null){
                    notificationSet.sendWaterLevelNotification(level)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })
    }

    private fun detectionNotify(){
        database = FirebaseDatabase.getInstance()
        notificationSet = NotificationSet(this)

        // Menambahkan ValueEventListener untuk mengetahui perubahan pada node detection
        val reference = database.getReference("detection")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val detection = snapshot.child("object_name").value.toString()
                val confidence = snapshot.child("confidence").value.toString().toFloat()

            if (detection != null && confidence != null){
                    notificationSet.sendDetectionNotification(detection,confidence)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, R.string.load_post_onCancelled.toString(),error.toException())
            }

        })

    }
}
