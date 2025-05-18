package com.example.cultuespai.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuespai.R
import com.example.cultuespai.adapters.EventsAdapter
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.utils.activateNavBar
import com.example.cultuespai.utils.api.ApiRepository.getEsdeveniments
import com.example.cultuespai.utils.api.ApiRepository.getSales
import com.example.cultuespai.utils.api.ApiRepository.getUsuariById
import com.example.cultuespai.utils.api.ApiRepository.getUsuaris
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var eventsList: ArrayList<Esdeveniment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        activateNavBar(this, this, 1)

        eventsRecyclerView = findViewById<RecyclerView>(R.id.eventsRecyclerview)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = EventsAdapter(arrayListOf()) { event ->
            val intent = Intent(this, EventActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
        eventsRecyclerView.adapter = eventsAdapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val newEvent = intent?.getSerializableExtra("event") as Esdeveniment

                eventsList.add(newEvent)
                eventsAdapter.updateData(eventsList)
            }
        }

        lifecycleScope.launch {
            try {
                val events = getEsdeveniments()
                eventsList = events?.toMutableList() as ArrayList<Esdeveniment>
                eventsAdapter.updateData(eventsList)
                if (UserData.userType == "Organitzador") {
                    newEventButtonListener()
                }
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun newEventButtonListener() {
        val newEventButton = findViewById<Button>(R.id.newEventButton)
        newEventButton.visibility = Button.VISIBLE

        newEventButton.setOnClickListener {
            val intent = Intent(this, NewEventActivity::class.java)
            launcher.launch(intent)
        }
    }
}