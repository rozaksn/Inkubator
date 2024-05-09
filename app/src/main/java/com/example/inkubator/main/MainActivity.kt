package com.example.inkubator.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
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
import com.example.inkubator.notification.NotificationSetup
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var notificationSetup: NotificationSetup
    private lateinit var database: FirebaseDatabase

    var buttonActive = false
    var buttonActiveFemaleGecko = false
    var buttonActiveMaleBallPython = false
    var buttonActiveFemaleBallPython = false
    var buttonActiveMaleBeardedDragon = false
    var buttonActiveFemaleBeardedDragon = false
    var buttonActiveReset =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Inisialisasi database
        database = FirebaseDatabase.getInstance()

        // Inisisalisasi notificationSet
        notificationSetup = NotificationSetup(this)

        // Menjalankan service dari notifiaksi
        startService(Intent(this, NotificationService::class.java))

        checkNotificationPermission()
        checkBatteryOptimizationPermission()

        firebaseConnection()
        dht()
        waterLevel()
        detection()

        maleGecko()
        femaleGecko()
        maleBallPython()
        femaleBallPython()
        maleBeardedDragon()
        femaleBeardedDragon()
        reset()
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

    private fun checkBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Mengecek versi android
            val packageName = packageName // Dapatkan nama paket aplikasi saat ini.
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager // Dapatkan layanan PowerManager.

            // Periksa apakah aplikasi saat ini mengabaikan optimasi baterai.
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                requestBatteryOptimization() // Meminta ijin pengecualian optimasi baterai
            }
        }
    }

    private fun requestBatteryOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Buat Intent untuk membuka halaman pengaturan optimasi baterai.
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)

            // Tetapkan URI data untuk menyertakan nama paket aplikasi.
            //untuk memeuat sumber daya dari string yang mencakup paket nama aplikasi (packageName).
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }


    private fun firebaseConnection() {
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = com.google.firebase.ktx.Firebase.analytics

        // Menampilkan pesan pada toast ketika firebase berhasil terhubung
        Toast.makeText(this, "Firebase connected", Toast.LENGTH_SHORT).show()

        // Menampilkan pesan pada logcat
        firebaseAnalytics.logEvent("firebaseConnected", null)
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1") {
                    binding.btMaleGecko.setBackgroundColor(Color.GREEN)
                    buttonActive = true

                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                } else {
                    binding.btMaleGecko.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActive = false

                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d("mGecko", "mGecko: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_fetcing.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.w("mGecko", "loadPost:onCancelled", error.toException())
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btFemaleGecko.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleGecko = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                }else{
                    binding.btFemaleGecko.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleGecko = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d("fGecko", "fGecko: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("fGecko", "loadPost:onCancelled",error.toException())
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btMaleBallPython.setBackgroundColor(Color.GREEN)
                    buttonActiveMaleBallPython = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                }else{
                    binding.btMaleBallPython.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveMaleBallPython = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d("mPython", "mPython: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("mPython", "loadPost:onCancelled",error.toException())
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btFemaleBallPython.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleBallPython = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                }
                else{
                    binding.btFemaleBallPython.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleBallPython = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d(TAG, "fPython: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("fPython", "loadPost:onCancelled",error.toException())
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btMaleBeardedDragon.setBackgroundColor(Color.GREEN)
                    buttonActiveMaleBeardedDragon = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                }else{
                    binding.btMaleBeardedDragon.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveMaleBeardedDragon = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d("mDragon", "mDragon: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("mDragon", "loadPost:onCancelled",error.toException())
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
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btFemaleBeardedDragon.setBackgroundColor(Color.GREEN)
                    buttonActiveFemaleBeardedDragon = true

                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btReset.isEnabled = false
                }else{
                   binding.btFemaleBeardedDragon.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveFemaleBeardedDragon = false

                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btReset.isEnabled = true
                }
                Log.d("fDragon", "fDragon: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("fDragon", "loadPost:onCancelled",error.toException())
            }

        })
    }

    private fun reset(){
        val buttonRef= database.getReference("REPTIL/reset")
        binding.btReset.setOnClickListener {
            if (!buttonActiveReset){
                buttonRef.setValue("1")
                buttonActiveReset = true
            }else{
                buttonRef.setValue("0")
                buttonActiveReset = false
            }
        }

        buttonRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //mengambil nilai dari snapshot Firebase dan mengubahnya menjadi string, dengan nilai default "0" jika terjadi kesalahan
                val value = snapshot.value as? String ?: "0"  // Niali default dari value adalah 0
                if (value == "1"){
                    binding.btReset.setBackgroundColor(Color.GREEN)
                    buttonActiveReset = true

                    // Menunda pengubahan nilai kembali ke "0" selama 5 detik
                    Handler(Looper.getMainLooper()).postDelayed({
                        buttonRef.setValue("0")
                        buttonActiveReset = false
                    },10000) // delay selama 5 detik


                    binding.btMaleGecko.isEnabled = false
                    binding.btFemaleGecko.isEnabled = false
                    binding.btMaleBallPython.isEnabled = false
                    binding.btFemaleBallPython.isEnabled = false
                    binding.btMaleBeardedDragon.isEnabled = false
                    binding.btFemaleBeardedDragon.isEnabled = false


                }else{
                    binding.btReset.setBackgroundColor(resources.getColor(R.color.red))
                    buttonActiveReset = false


                    binding.btMaleGecko.isEnabled = true
                    binding.btFemaleGecko.isEnabled = true
                    binding.btMaleBallPython.isEnabled = true
                    binding.btFemaleBallPython.isEnabled = true
                    binding.btMaleBeardedDragon.isEnabled = true
                    binding.btFemaleBeardedDragon.isEnabled = true


                }
                Log.d("Reset Button","reset: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w(TAG, "loadPost:onCancelled",error.toException())
            }

        })


    }


        private fun dht() {
            val ref = database.getReference("DHT")
            var startTime: Long = 0
            var totalBytesReceived: Long = 0

            ref.addValueEventListener(object : ValueEventListener {

                @SuppressLint("SetTextI18n") // Digunakan untuk menghilangkan peringatan karena string ditulis secara hardcode
                override fun onDataChange(snapshot: DataSnapshot) {
                    val suhu = snapshot.child("suhu").value.toString().toFloat()
                    val kelembaban = snapshot.child("kelembaban").value.toString().toFloat()

                    binding.tvSuhu.text = "$suhu \u00b0C"
                    binding.tvKelembaban.text = "$kelembaban %"

                    // Hitung throughput
                    // Update the totalBytesReceived
                    totalBytesReceived += 2 * Float.SIZE_BYTES // 2 float values: suhu and kelembaban

                    // Calculate throughput if startTime has been set
                    if (startTime > 0) {
                        val duration = System.currentTimeMillis() - startTime
                        val throughput = totalBytesReceived.toDouble() / duration.toDouble()
                        // Convert the throughput to KB/S
                        //val throughputInKBs = throughput / 1024.0
                        // Log or display the throughput
                        Log.d("Throughput", "Throughput: $throughput Byte/S")

                        // Reset the variables for the next calculation
                        startTime = 0
                        totalBytesReceived = 0
                    } else {
                        // Set the startTime for the first update
                        startTime = System.currentTimeMillis()
                    }

                   /* if (snapshot.exists()) {
                        val endTime = System.currentTimeMillis()
                        if (startTime == 0L) {
                            startTime = endTime
                        } else {
                            val durationInMillis = endTime - startTime
                            val snapshotBytes = snapshot.getValue(Float::class.java)?.toString()?.toByteArray()?.size?.toLong() ?: 0L // Handle null snapshot values
                            totalBytesReceived += snapshotBytes
                            val throughputBps = totalBytesReceived / (durationInMillis / 1000.0)
                            val throughputKBps = throughputBps / 1024.0
                            Log.d(TAG, "Throughput: $throughputKBps KB/s")
                        }
                        startTime = endTime // Reset start time for next measurement
                        totalBytesReceived = 0  // Reset total bytes for next measurement
                    }

                    */

                   /* val snapshotBytes = snapshot.toString().toByteArray().size.toLong() // Hitung jumlah byte dari snapshot
                    totalBytesReceived += snapshotBytes

                    if (startTime == 0L) {
                        startTime = System.currentTimeMillis()
                    } else {
                        val endTime = System.currentTimeMillis()
                        val durationInMillis = endTime - startTime
                        totalBytesReceived += snapshot.toString().length.toLong() // Hitung panjang string snapshot sebagai jumlah byte
                        val throughputBps = totalBytesReceived / (durationInMillis / 1000.0) // Throughput dalam Byte per detik
                        val throughputKBps = throughputBps / 1024.0 // Konversi ke Kilobyte per detik

                        Log.d(TAG, "Throughput: $throughputKBps KB/s")
                    }

                    */

                    //Log.d("Suhu","Suhu: $suhu°C")
                    //Log.d("Kelembaban", "Kelembaban: $kelembaban%")
                    //Log.d(TAG,"===========================")
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
            val ref = database.getReference("WATER_LEVEL")
            ref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //Baca data dari database
                    val level = snapshot.child("water_level").value.toString().toInt()

                    //Menampilkan data pada textview
                    binding.tvTinggiAir.text = "$level cm"

                    //Log.d("Water Level","Water level: $level cm")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.error_fetcing.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w("Water Level", R.string.load_post_onCancelled.toString(), error.toException())
                }

            })
        }

    private fun detection(){
        var isPopupShown = false
        val ref = database.getReference("detection")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val objectDetection = snapshot.child("object_name").value.toString()
                val confidenceScore = snapshot.child("confidence").value.toString().toFloat()

                binding.tvDeteksi.text = objectDetection
                // Menampilkan pop-up hanya jika belum ditampilkan sebelumnya
                if (!isPopupShown && objectDetection == "Telur menetas" && confidenceScore > 0.5) {
                    showPopup(objectDetection, confidenceScore)
                    isPopupShown = true
                }
                Log.d("Deteksi Objek","Objek: $objectDetection")

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_fetcing.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.w("Deteksi Objek", R.string.load_post_onCancelled.toString(), error.toException())
            }

        })
    }

        //Meminta ijin notifikasi
        private fun requestPermission() {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0
                )
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS),1
            )
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 0) { // Mengecek apakah kode permintyaan sama dengan 0
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.notification_granted, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.notification_ungranted, Toast.LENGTH_SHORT).show()
                }
            }
            if (requestCode == 1){
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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

    private fun showPopup(detection:String, confidence:Float) {
        if (detection == "Telur Menetas" && confidence > 0.5){
            val builder = AlertDialog.Builder(this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
            builder.setTitle(R.string.object_detection)
            builder.setIcon(R.drawable.logo_polinema_no_bg_png)
            builder.setMessage("Objek: $detection")

            // Tombol OK pada pop-up
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()

            // Menampilkan pop-up
            dialog.show()
        }

    }
}
