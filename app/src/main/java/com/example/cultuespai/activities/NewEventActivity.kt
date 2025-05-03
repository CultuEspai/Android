package com.example.cultuespai.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultuespai.R
import com.example.cultuespai.TimePickerFragment
import com.example.cultuespai.DatePickerFragment

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_event)

        val rooms = arrayListOf("Sala 1", "Sala 2", "Sala 3")

        val dateTextbox = findViewById<EditText>(R.id.dateTextbox)
        val startTimeTextbox = findViewById<EditText>(R.id.startTimeTextbox)
        val endTimeTextbox = findViewById<EditText>(R.id.endTimeTextbox)
        val spinner = findViewById<Spinner>(R.id.roomSpinner)

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            rooms
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

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

//        findViewById<Button>(R.id.pickTime).setOnClickListener {
//            TimePickerFragment().show(supportFragmentManager, "timePicker")
//        }

//        findViewById<Button>(R.id.pickTime).setOnClickListener {
//            val newFragment = DatePickerFragment()
//            newFragment.show(supportFragmentManager, "datePicker")
//        }
    }
}