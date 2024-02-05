package com.example.inkubator.main

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.inkubator.R
import com.example.inkubator.about.AboutActivity
import com.example.inkubator.databinding.ActivityMainBinding
import com.example.inkubator.notification.NotificationService
import com.example.inkubator.notification.NotificationSet
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var notifikationSet: NotificationSet
    private lateinit var database: FirebaseDatabase

    var buttonActive = false

    var buttonActiveFemaleGecko: Boolean = false
    var buttonActiveMaleBallPython: Boolean = false
    var buttonActiveFemaleBallPython: Boolean = false
    var buttonActiveMaleBeardedDragon: Boolean = false
    var buttonActiveFemaleBeardedDragon: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Inisialisasi database
        database = FirebaseDatabase.getInstance()

        // Inisisalisasi notificationSet
        notifikationSet = NotificationSet(this)

        startService(Intent(this, NotificationService::class.java))

        checkNotificationPermission()

        firebaseConnection()
        dht()
        waterLevel()
        maleGecko()
        femaleGecko()
        maleBallPython()
        femaleBallPython()
        maleBeardedDragon()
        femaleBeardedDragon()



    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission diijinkan maka notifikasi dapat diterima
        } else {
            // Permission ditolak maka akan mengajukan permintaan
            requestPermission()
        }
    }

    private fun firebaseConnection() {
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = com.google.firebase.ktx.Firebase.analytics

        // Check if Firebase is initialized
        if (FirebaseApp.getInstance() != null) {
            // Menampilkan pesan pada toast ketika firebase berhasil terhubung
            Toast.makeText(this, "Firebase connected", Toast.LENGTH_SHORT).show()

            // Menampilkan pesan pada logcat
            firebaseAnalytics.logEvent("firebaseConnected", null)
        }
    }

    private fun maleGecko() {
        val buttonRef = database.getReference("REPTIL/mGecko")
        binding.btMaleGecko.setOnClickListener {
            if (!buttonActive) {
                buttonRef.setValue("1")
                buttonActive = true
            } else {
                buttonRef.setValue("0")
                buttonActive = false
            }
        }
        buttonRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? String ?: "0"
                if (value == "1") {
                    binding.btMaleGecko.setBackgroundColor(Color.GREEN)
                    buttonActive = true

                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                } else {
                    binding.btMaleGecko.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActive = false

                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "mGecko: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_fetcing.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

        })
    }

    private fun femaleGecko(){
        val buttonRef = database.getReference("REPTIL/fGecko")
        binding.btFemaleGecko.setOnClickListener {
            if (!buttonActiveFemaleGecko) {
                buttonRef.setValue("1")
                buttonActiveFemaleGecko = true
            }else{
                buttonRef.setValue("0")
                buttonActiveFemaleGecko = false
            }
        }
        buttonRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? String ?: "0"
                if (value == "1"){
                    binding.btFemaleGecko.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleGecko = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                }else{
                    binding.btFemaleGecko.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleGecko = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "fGecko: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }

        })
    }

    private fun maleBallPython(){
        val buttonRef = database.getReference("REPTIL/mPython")
        binding.btMaleBallPython.setOnClickListener {
            if (!buttonActiveMaleBallPython) {
                buttonRef.setValue("1")
                buttonActiveMaleBallPython = true
            }else{
                buttonRef.setValue("0")
                buttonActiveMaleBallPython = false
            }
        }
        buttonRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? String ?: "0"
                if (value == "1"){
                    binding.btMaleBallPython.setBackgroundColor(Color.GREEN)
                    buttonActiveMaleBallPython = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                }else{
                    binding.btMaleBallPython.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveMaleBallPython = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "mPython: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }

        })
    }

    private fun femaleBallPython(){
        val buttonRef = database.getReference("REPTIL/fPython")
        binding.btFemaleBallPython.setOnClickListener {
            if (!buttonActiveFemaleBallPython) {
                buttonRef.setValue("1")
                buttonActiveFemaleBallPython = true
            }else{
                buttonRef.setValue("0")
                buttonActiveFemaleBallPython = false
            }
        }
        buttonRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? String ?: "0"
                if (value == "1"){
                    binding.btFemaleBallPython.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleBallPython = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                }
                else{
                    binding.btFemaleBallPython.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleBallPython = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "fPython: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }

        })
    }

    private fun maleBeardedDragon(){
        val buttonRef = database.getReference("REPTIL/mDragon")
        binding.btMaleBeardedDragon.setOnClickListener {
            if (!buttonActiveMaleBeardedDragon) {
                buttonRef.setValue("1")
                buttonActiveMaleBeardedDragon = true
            }else{
                buttonRef.setValue("0")
                buttonActiveMaleBeardedDragon = false
            }
        }
        buttonRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.value as? String ?: "0"
                if (value == "1"){
                    binding.btMaleBeardedDragon.setBackgroundColor(Color.GREEN)
                    buttonActiveMaleBeardedDragon = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                }else{
                    binding.btMaleBeardedDragon.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveMaleBeardedDragon = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "mDragon: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }
        })
    }

    private fun femaleBeardedDragon(){
        val buttonRef = database.getReference("REPTIL/fDragon")
        binding.btFemaleBeardedDragon.setOnClickListener {
            if (!buttonActiveFemaleBeardedDragon) {
                buttonRef.setValue("1")
                buttonActiveFemaleBeardedDragon = true
            }else{
                buttonRef.setValue("0")
                buttonActiveFemaleBeardedDragon = false
            }
        }
        buttonRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //mengambil nilai dari snapshot Firebase dan mengubahnya menjadi string, dengan nilai default "0" jika terjadi kesalahan
                val value = snapshot.value as? String ?: "0"
                if (value == "1"){
                    binding.btFemaleBeardedDragon.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleBeardedDragon = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                }else{
                   binding.btFemaleBeardedDragon.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleBeardedDragon = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                }
                Log.d(TAG, "fDragon: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }

        })
    }


        private fun dht() {
            val ref = database.getReference("TEST")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val suhu = snapshot.child("suhu").value.toString().toFloat()
                    val kelembaban = snapshot.child("kelembaban").value.toString().toFloat()
                    binding.tvSuhu.text = "$suhu \u00b0C"
                    binding.tvKelembaban.text = "$kelembaban %"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.error_fetcing.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "Failed to read value.", error.toException())
                }

            })
        }

        private fun waterLevel() {
            val ref = database.getReference("TEST/message")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Baca data dari database
                    val level = snapshot.value.toString()

                    //Menampilkan data pada textview
                    binding.tvTinggiAir.text = "$level cm"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.error_fetcing.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, R.string.load_post_onCancelled.toString(), error.toException())
                }

            })
        }

        //Meminta ijin notifikasi
        private fun requestPermission() {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                )
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 0) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.notification_granted, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.notification_ungranted, Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.about_action -> {
                    val intent = Intent(this@MainActivity, AboutActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        }

    }
