package com.example.cultuespai.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuespai.R
import com.example.cultuespai.adapters.TicketsAdapter
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.utils.activateNavBar
import com.example.cultuespai.utils.api.ApiRepository.getEntradesByUser
import com.example.cultuespai.utils.api.ApiRepository.getEsdeveniments
import kotlinx.coroutines.launch

class TicketsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tickets)

        activateNavBar(this, this, 2)

        var ticketsList = ArrayList<Entrada>()
        var eventsList = ArrayList<Esdeveniment>()

        lifecycleScope.launch {
            try {
                val tickets = getEntradesByUser(UserData.userId)
                val events = getEsdeveniments()
                ticketsList = tickets?.toMutableList() as ArrayList<Entrada>
                eventsList = events?.toMutableList() as ArrayList<Esdeveniment>
                showTickets(ticketsList, eventsList)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun showTickets(tickets: ArrayList<Entrada>, events: ArrayList<Esdeveniment>) {
        val ticketsRecyclerView = findViewById<RecyclerView>(R.id.ticketsRecyclerview)
        ticketsRecyclerView.layoutManager = LinearLayoutManager(this)
        ticketsRecyclerView.adapter = TicketsAdapter(tickets, events) { event ->
            val intent = Intent(this, EventActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }
}