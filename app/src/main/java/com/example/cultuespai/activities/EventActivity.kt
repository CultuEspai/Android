package com.example.cultuespai.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cultuespai.R
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import com.example.cultuespai.utils.api.ApiRepository.getEntradesByEvent
import com.example.cultuespai.utils.api.ApiRepository.getEntradesByUser
import com.example.cultuespai.utils.api.ApiRepository.getEsdeveniments
import com.example.cultuespai.utils.api.ApiRepository.getSalaById
import com.example.cultuespai.utils.api.ApiRepository.postEntrada
import com.example.cultuespai.utils.api.ApiRepository.putEsdeveniment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event)

        backButtonListener()

        val event = intent.getSerializableExtra("event") as Esdeveniment

        val eventImage = findViewById<ImageView>(R.id.eventImage)
        val eventName = findViewById<TextView>(R.id.eventName)
        val eventRoom = findViewById<TextView>(R.id.eventRoom)
        val eventDate = findViewById<TextView>(R.id.eventDate)
        val eventCapacity = findViewById<TextView>(R.id.eventCapacity)
        val eventDescription = findViewById<TextView>(R.id.eventDescription)
        val reserveButton = findViewById<Button>(R.id.reserveButton)

        //eventImage.setImageResource(R.drawable.tickets)
        eventName.text = event.Nom

        val currentLocale = this.resources.configuration.locales[0]
        val dateFormat = this.getString(R.string.date_format)
        val formatter = SimpleDateFormat(dateFormat, currentLocale)
        eventDate.text = formatter.format(event.DataInici)

        eventCapacity.text = event.Aforament.toString()
        eventDescription.text = event.Descripcio

        lifecycleScope.launch {
            try {
                val room = getSalaById(event.SalaID)
                val userTickets = getEntradesByUser(UserData.userId)
                val tickets = userTickets?.toMutableList() as ArrayList<Entrada>
                eventRoom.text = room?.Nom ?: event.SalaID.toString()
                loadReserveButton(reserveButton, event, tickets, room)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun backButtonListener() {
        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadReserveButton(button: Button, event: Esdeveniment,
                                  tickets: ArrayList<Entrada>, room: Sala?){
        val reserved = tickets.any { it.EsdevenimentID == event.EsdevenimentID }

        if (reserved){
            button.text = this.getString(R.string.reserved)
            button.isEnabled = false
        }else if(event.EntradesDisp < 1){
            button.text = this.getString(R.string.full)
            button.isEnabled = false
        }else{
            lifecycleScope.launch {
                try {
                    val eventTicketsList = getEntradesByEvent(event.EsdevenimentID)
                    val eventTickets = eventTicketsList as ArrayList<Entrada>
                    if (room != null && room.CadiresFixes) {
                        loadSeatNumberLayout(eventTickets, room)
                        seatsButtonListener(button, event)
                    }else{
                        loadNumberTicketsLayout(event)
                        numTicketsButtonListener(button, event)
                    }
                }catch (e: Exception)
                {
                    println("API Connexion Error")
                }
            }
        }
    }

    private fun numTicketsButtonListener(button: Button, event: Esdeveniment){
        val numberTicketsLayout = findViewById<LinearLayout>(R.id.numberTicketsLayout)
        val numberTicketsSpinner = findViewById<Spinner>(R.id.numberTicketsSpinner)
        button.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val entrada = Entrada(
                        0,
                        UserData.userId,
                        event.EsdevenimentID,
                        numberTicketsSpinner.selectedItem.toString().toInt(),
                        null)
                    val createdEntrada = postEntrada(entrada)
                    if (createdEntrada != null) {
                        event.EntradesDisp -= numberTicketsSpinner.selectedItem.toString().toInt()
                        putEsdeveniment(event.EsdevenimentID, event)
                        numberTicketsLayout.visibility = View.GONE
                        button.text = this@EventActivity.getString(R.string.reserved)
                        button.isEnabled = false
                    }
                }catch (e: Exception)
                {
                    println("API Connexion Error")
                }
            }
        }
    }

    private fun seatsButtonListener(button: Button, event: Esdeveniment){
        val seatNumberLayout = findViewById<LinearLayout>(R.id.seatNumberLayout)

        button.setOnClickListener {
            val seatLetterSpinner = findViewById<Spinner>(R.id.seatLetterSpinner)
            val seatNumberSpinner = findViewById<Spinner>(R.id.seatNumberSpinner)

            val seatLetter = seatLetterSpinner.selectedItem.toString()
            val seatNumber = seatNumberSpinner.selectedItem.toString()
            val selectedSeat = "$seatLetter$seatNumber"
            lifecycleScope.launch {
                try {
                    val entrada = Entrada(
                        0,
                        UserData.userId,
                        event.EsdevenimentID,
                        1,
                        selectedSeat)
                    val createdEntrada = postEntrada(entrada)
                    if (createdEntrada != null) {
                        event.EntradesDisp -= 1
                        putEsdeveniment(event.EsdevenimentID, event)
                        button.text = this@EventActivity.getString(R.string.reserved)
                        button.isEnabled = false
                        seatNumberLayout.visibility = View.GONE
                    }

                }catch (e: Exception)
                {
                    println("API Connexion Error")
                }
            }
        }
    }

    private fun loadNumberTicketsLayout(event: Esdeveniment){
        val numberTicketsLayout = findViewById<LinearLayout>(R.id.numberTicketsLayout)
        val numberTicketsSpinner = findViewById<Spinner>(R.id.numberTicketsSpinner)
        val tickets = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        numberTicketsLayout.visibility = LinearLayout.VISIBLE

        val limitedTickets = if (event.EntradesDisp < 10)
            tickets.subList(0, event.EntradesDisp)
        else
            tickets

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_centred,
            limitedTickets
        )
        adapter.setDropDownViewResource(R.layout.spinner_item_centred)
        numberTicketsSpinner.adapter = adapter
    }

    private fun loadSeatNumberLayout(eventTickets: ArrayList<Entrada>, room: Sala){
        val seatNumberLayout = findViewById<LinearLayout>(R.id.seatNumberLayout)
        val seatLetterSpinner = findViewById<Spinner>(R.id.seatLetterSpinner)
        val seatNumberSpinner = findViewById<Spinner>(R.id.seatNumberSpinner)

        seatNumberLayout.visibility = LinearLayout.VISIBLE

        // Extraer letra máxima y número máximo
        val maxLetter = room.ButacaMax?.substring(0, 1) ?: "A"
        val maxNumber = room.ButacaMax?.substring(1)?.toIntOrNull() ?: 1

        // Generar lista completa de asientos posibles
        val allSeats = mutableListOf<String>()
        for (letter in 'A'..maxLetter[0]) {
            for (number in 1..maxNumber) {
                allSeats.add("$letter$number")
            }
        }

        // Quitar los asientos ocupados
        val occupiedSeats = eventTickets.mapNotNull { it.NumeroButaca }
        val availableSeats = allSeats.filterNot { it in occupiedSeats }

        // Mapear letras a números disponibles
        val seatMap = mutableMapOf<Char, MutableList<Int>>()
        for (seat in availableSeats) {
            val letter = seat[0]
            val number = seat.substring(1).toInt()
            seatMap.getOrPut(letter) { mutableListOf() }.add(number)
        }

        // Letras disponibles
        val availableLetters = seatMap.keys.sorted()

        // Adaptador del spinner de letras
        val letterAdapter = ArrayAdapter(this, R.layout.spinner_item_centred, availableLetters)
        letterAdapter.setDropDownViewResource(R.layout.spinner_item_centred)
        seatLetterSpinner.adapter = letterAdapter

        // Al seleccionar una letra, cargar sus números disponibles
        seatLetterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLetter = availableLetters[position]
                val numbers = seatMap[selectedLetter]?.sorted() ?: emptyList()
                val numberAdapter = ArrayAdapter(this@EventActivity, R.layout.spinner_item_centred, numbers)
                numberAdapter.setDropDownViewResource(R.layout.spinner_item_centred)
                seatNumberSpinner.adapter = numberAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nada
            }
        }
    }
}