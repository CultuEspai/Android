package com.example.cultuespai.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cultuespai.R
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import com.example.cultuespai.fragments.TimePickerFragment
import com.example.cultuespai.fragments.DatePickerFragment
import com.example.cultuespai.utils.api.ApiRepository.getEntradesByUser
import com.example.cultuespai.utils.api.ApiRepository.getSalaById
import com.example.cultuespai.utils.api.ApiRepository.getSales
import com.example.cultuespai.utils.api.ApiRepository.postEsdeveniment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_event)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            try {
                val salas = getSales()
                val rooms = salas?.toMutableList() as ArrayList<Sala>
                loadComponents(rooms)
                createEventButtonListener(rooms)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun loadComponents(rooms: ArrayList<Sala>){
        val dateTextbox = findViewById<EditText>(R.id.dateTextbox)
        val startTimeTextbox = findViewById<EditText>(R.id.startTimeTextbox)
        val endTimeTextbox = findViewById<EditText>(R.id.endTimeTextbox)
        val roomSpinner = findViewById<Spinner>(R.id.roomSpinner)
        val capacityTextbox = findViewById<EditText>(R.id.capacityTextbox)

        val roomNames = rooms.map { it.Nom }

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            roomNames
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        roomSpinner.adapter = adapter

        roomSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRoom = rooms[position]
                capacityTextbox.hint = "Max. ${selectedRoom.Aforament}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                capacityTextbox.hint = ""
            }
        }

        startTimeTextbox.setOnClickListener {
            val startTimePicker = TimePickerFragment(startTimeTextbox)
            startTimePicker.show(supportFragmentManager, "timePicker")
        }

        endTimeTextbox.setOnClickListener {
            val endTimePicker = TimePickerFragment(endTimeTextbox)
            endTimePicker.show(supportFragmentManager, "timePicker")
        }

        dateTextbox.setOnClickListener {
            val datePicker = DatePickerFragment(dateTextbox)
            datePicker.show(supportFragmentManager, "datePicker")
        }
    }

    private fun createEventButtonListener(rooms: ArrayList<Sala>) {
        val nameTextbox = findViewById<EditText>(R.id.nameTextbox)
        val descriptionTextbox = findViewById<EditText>(R.id.descriptionTextbox)
        val priceTextbox = findViewById<EditText>(R.id.priceTextbox)
        val capacityTextbox = findViewById<EditText>(R.id.capacityTextbox)
        val dateTextbox = findViewById<EditText>(R.id.dateTextbox)
        val startTimeTextbox = findViewById<EditText>(R.id.startTimeTextbox)
        val endTimeTextbox = findViewById<EditText>(R.id.endTimeTextbox)
        val roomSpinner = findViewById<Spinner>(R.id.roomSpinner)

        findViewById<Button>(R.id.create).setOnClickListener {
            val room = rooms.find { it.Nom == roomSpinner.selectedItem.toString() }
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateStr = dateTextbox.text.toString()
            val startTimeStr = startTimeTextbox.text.toString()
            val endTimeStr = endTimeTextbox.text.toString()

            if (verifyData(nameTextbox, descriptionTextbox,
                    dateStr, startTimeStr, endTimeStr, capacityTextbox, room)){
                val startDateStr = "$dateStr $startTimeStr"
                val endDateStr = "$dateStr $endTimeStr"

                val startDate: Date = format.parse(startDateStr)!!
                val endDate: Date = format.parse(endDateStr)!!

                val ofPayment = priceTextbox.text.toString().toInt() > 0
                val price = if (ofPayment) priceTextbox.text.toString().toInt() else 0
                val capacity = if (capacityTextbox.text.toString().isNotBlank())
                    capacityTextbox.text.toString().toInt() else room?.Aforament ?: 0

                val event = Esdeveniment(
                    0,
                    nameTextbox.text.toString(),
                    descriptionTextbox.text.toString(),
                    room?.SalaID ?: roomSpinner.selectedItem.toString().toInt(),
                    startDate,
                    endDate,
                    capacity,
                    UserData.userId,
                    "Pendent",
                    ofPayment,
                    price,
                    capacity
                )

                lifecycleScope.launch {
                    try {
                        val createdEvent = postEsdeveniment(event)
                        if (createdEvent != null) {
                            val intent = intent.putExtra("event", event)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }catch (e: Exception)
                    {
                        println("API Connexion Error")
                    }
                }
            }
        }
    }

    private fun verifyData(nameTextbox: EditText, descriptionTextbox: EditText,
                           dateStr: String, startTimeStr: String, endTimeStr: String,
                           capacityTextbox: EditText, room: Sala?): Boolean {
        if (nameTextbox.text.isBlank()) {
            Toast.makeText(this, R.string.name_obligatory,
                Toast.LENGTH_SHORT).show()
            return false
        }

        if (descriptionTextbox.text.isBlank()) {
            Toast.makeText(this, R.string.description_obligatory,
                Toast.LENGTH_SHORT).show()
            return false
        }

        if (dateStr.isBlank() || startTimeStr.isBlank() || endTimeStr.isBlank()) {
            Toast.makeText(this,
                R.string.date_obligatory,
                Toast.LENGTH_SHORT).show()
            return false
        }

        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (format.parse(startTimeStr)!! >= format.parse(endTimeStr)!!) {
            Toast.makeText(this, R.string.valid_date,
                Toast.LENGTH_SHORT).show()
            return false
        }

        if (capacityTextbox.text.toString().isNotBlank() &&
            capacityTextbox.text.toString().toInt() > room?.Aforament!!) {
            Toast.makeText(this, R.string.valid_capacity,
                Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}