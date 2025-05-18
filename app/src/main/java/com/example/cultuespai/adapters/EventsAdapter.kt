package com.example.cultuespai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuespai.R
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import java.text.SimpleDateFormat

class EventsAdapter(
    private val events: ArrayList<Esdeveniment>,
    private val viewEvent: (Esdeveniment) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventImage: ImageView = view.findViewById(R.id.eventImage)
        private val eventName: TextView = view.findViewById(R.id.eventName)
        private val eventDate: TextView = view.findViewById(R.id.eventDate)
        private val eventCapacity: TextView = view.findViewById(R.id.eventCapacity)

        fun bind(event: Esdeveniment) {
            when (event.SalaID) {
                1 -> eventImage.setImageResource(R.drawable.sala1)
                2 -> eventImage.setImageResource(R.drawable.sala2)
                3 -> eventImage.setImageResource(R.drawable.sala3)
            }

            eventName.text = event.Nom

            val currentLocale = itemView.context.resources.configuration.locales[0]
            val dateFormat = itemView.context.getString(R.string.date_format)
            val formatter = SimpleDateFormat(dateFormat, currentLocale)
            eventDate.text = formatter.format(event.DataInici)

            eventCapacity.text = event.Aforament.toString()

            itemView.setOnClickListener {
                viewEvent(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount() = events.size

    fun updateData(newEvents: List<Esdeveniment>) {
        this.events.clear()
        this.events.addAll(newEvents)
        notifyDataSetChanged()
    }
}