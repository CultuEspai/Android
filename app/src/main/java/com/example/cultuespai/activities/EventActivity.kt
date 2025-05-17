package com.example.cultuespai.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultuespai.R
import com.example.cultuespai.entities.Esdeveniment

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event)

        val event = intent.getSerializableExtra("event") as Esdeveniment

        val eventImage = findViewById<ImageView>(R.id.eventImage)
        val eventName = findViewById<TextView>(R.id.eventName)
        val eventRoom = findViewById<TextView>(R.id.eventRoom)
        val eventDate = findViewById<TextView>(R.id.eventDate)
        val eventCapacity = findViewById<TextView>(R.id.eventCapacity)
        val eventDescription = findViewById<TextView>(R.id.eventDescription)

        eventImage.setImageResource(R.drawable.tickets)
        eventName.text = event.Nom
        eventRoom.text = event.SalaID.toString()
        eventDate.text = event.DataInici.toString()
        eventCapacity.text = event.Aforament.toString()
        eventDescription.text = event.Descripcio
    }
}