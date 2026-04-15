package com.example.lezione_10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.util.Log

class MainActivity : AppCompatActivity() {

    private var counter = 0 //variabile da incrementere e mostrare

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("TEST","TEST LOG") //log di debug, visibile in Logcat
        //posso referenziare gli elementi grafici in delle variabili
        val tvCount = findViewById<TextView>(R.id.tvCounter)
        val btCount = findViewById<Button>(R.id.btCount)
        val btToast = findViewById<Button>(R.id.btToast)

        tvCount.text = counter.toString() //mostro il valore della variabile counter
        btCount.setOnClickListener {
            counter++ //incremento la variabile counter
            tvCount.text = counter.toString() //aggiorno il testo del TextView
        }

        btToast.setOnClickListener {
            Toast.makeText(this, "Counter: $counter",
                Toast.LENGTH_SHORT).show() //mostro un Toast con il valore di counter
        }

    }
}