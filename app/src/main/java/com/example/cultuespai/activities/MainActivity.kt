package com.example.cultuespai.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuespai.R
import com.example.cultuespai.adapters.EventsAdapter
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.utils.activateNavBar
import com.example.cultuespai.utils.api.ApiRepository.getEsdeveniments
import com.example.cultuespai.utils.api.ApiRepository.getUsuariById
import com.example.cultuespai.utils.api.ApiRepository.getUsuaris
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        activateNavBar(this, this, 1)

        var eventsList = ArrayList<Esdeveniment>()

        lifecycleScope.launch {
            try {
                val events = getEsdeveniments()
                eventsList = events?.toMutableList() as ArrayList<Esdeveniment>
                showEvents(eventsList)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun showEvents(events: ArrayList<Esdeveniment>) {
        val eventsRecyclerView = findViewById<RecyclerView>(R.id.eventsRecyclerview)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsRecyclerView.adapter = EventsAdapter(events) { event ->
            val intent = Intent(this, EventActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }
}