package com.example.inkubator

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.inkubator.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy (LazyThreadSafetyMode.NONE){
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database:DatabaseReference
    val sensorDatabase = Firebase.database
    var buttonActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         //Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().reference

        dht()
        waterLevel()
        buttonMode()

    }

    private fun buttonMode(){
        val btnMaleGecko = binding.btMaleGecko
        val btnFemaleGecko = binding.btFemaleGecko
        val btnMaleBP = binding.btMaleBallPython
        val btnFemaleBP = binding.btFemaleBallPython
        val btnMaleBD = binding.btMaleBeardedDragon
        val btnFemaleBD = binding.btFemaleBeardedDragon

        //Gecko Jantan
        btnMaleGecko.setOnClickListener {
            if (!buttonActive){
                btnMaleGecko.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnFemaleGecko.isEnabled =false
                btnMaleBP.isEnabled = false
                btnFemaleBP.isEnabled = false
                btnMaleBD.isEnabled = false
                btnFemaleBD.isEnabled = false
            }
            else{
                btnMaleGecko.setBackgroundColor(Color.RED)
                buttonActive = false

                btnFemaleGecko.isEnabled = true
                btnMaleBP.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnMaleBD.isEnabled = true
                btnFemaleBD.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.male_gecko.toString()).setValue(status)
        }
        //Gecko Betina
        btnFemaleGecko.setOnClickListener {
            if (!buttonActive){
                btnFemaleGecko.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnMaleGecko.isEnabled =false
                btnMaleBP.isEnabled = false
                btnFemaleBP.isEnabled = false
                btnMaleBD.isEnabled = false
                btnFemaleBD.isEnabled = false
            }
            else{
                btnFemaleGecko.setBackgroundColor(Color.RED)
                buttonActive = false

                btnMaleGecko.isEnabled = true
                btnMaleBP.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnMaleBD.isEnabled = true
                btnFemaleBD.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.female_gecko.toString()).setValue(status)
        }
        //Ball Python Jantan
        btnFemaleGecko.setOnClickListener {
            if (!buttonActive){
                btnMaleBP.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnMaleGecko.isEnabled =false
                btnFemaleGecko.isEnabled = false
                btnFemaleBP.isEnabled = false
                btnMaleBD.isEnabled = false
                btnFemaleBD.isEnabled = false
            }
            else{
                btnMaleBP.setBackgroundColor(Color.RED)
                buttonActive = false

                btnMaleGecko.isEnabled = true
                btnFemaleGecko.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnMaleBD.isEnabled = true
                btnFemaleBD.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.male_BP.toString()).setValue(status)
        }
        //Ball Python Betina
        btnFemaleGecko.setOnClickListener {
            if (!buttonActive){
                btnFemaleBP.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnMaleGecko.isEnabled =false
                btnFemaleGecko.isEnabled = false
                btnMaleBP.isEnabled = false
                btnMaleBD.isEnabled = false
                btnFemaleBD.isEnabled = false
            }
            else{
                btnFemaleBP.setBackgroundColor(Color.RED)
                buttonActive = false

                btnMaleGecko.isEnabled = true
                btnFemaleGecko.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnMaleBD.isEnabled = true
                btnFemaleBD.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.female_BP.toString()).setValue(status)
        }
        //Bearded Dragon Jantan
        btnFemaleGecko.setOnClickListener {
            if (!buttonActive){
                btnMaleBD.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnMaleGecko.isEnabled =false
                btnFemaleGecko.isEnabled = false
                btnMaleBP.isEnabled = false
                btnFemaleBP.isEnabled = false
                btnFemaleBD.isEnabled = false
            }
            else{
                btnMaleBD.setBackgroundColor(Color.RED)
                buttonActive = false

                btnMaleGecko.isEnabled = true
                btnFemaleGecko.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnFemaleBD.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.male_BD.toString()).setValue(status)
        }
        //Bearded Dragon Betina
        btnFemaleGecko.setOnClickListener {
            if (!buttonActive){
                btnFemaleBD.setBackgroundColor(Color.GREEN)
                buttonActive = true

                btnMaleGecko.isEnabled =false
                btnFemaleGecko.isEnabled = false
                btnMaleBP.isEnabled = false
                btnMaleBD.isEnabled = false
                btnFemaleBP.isEnabled = false
            }
            else{
                btnFemaleBD.setBackgroundColor(Color.RED)
                buttonActive = false

                btnMaleGecko.isEnabled = true
                btnFemaleGecko.isEnabled = true
                btnFemaleBP.isEnabled = true
                btnMaleBD.isEnabled = true
                btnFemaleBP.isEnabled = true
            }
            val status = if (buttonActive) 1 else 0
            database.child(R.string.female_BD.toString()).setValue(status)
        }
    }

    private fun dht(){
        val dhtRef = sensorDatabase.getReference(R.string.dht.toString())
        dhtRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Baca nilai dari database
                val suhu = snapshot.child(R.string.suhu.toString()).value as? Float
                val kelembaban = snapshot.child(R.string.kelembaban.toString()).value as? Float

                //Menampilkan data pada textview
                binding.tvSuhu.text = suhu.toString()
                binding.tvKelembaban.text = kelembaban.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("Fetch Data",R.string.load_post_onCancelled.toString(),error.toException())
            }
        })
    }

    private fun waterLevel(){
        val waterLevelRef = sensorDatabase.getReference(R.string.level.toString())
        waterLevelRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Baca data dari database
                val level = snapshot.child(R.string.water_level.toString()).value

                //Menampilkan data pada textview
                binding.tvTinggiAir.text = level.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,R.string.error_fetcing.toString(),Toast.LENGTH_SHORT).show()
                Log.w("Fetch Data",R.string.load_post_onCancelled.toString(),error.toException())
            }

        })
    }

}