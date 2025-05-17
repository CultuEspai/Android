package com.example.cultuespai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cultuespai.R
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment

class TicketsAdapter(
    private val tickets: List<Entrada>,
    private val events: ArrayList<Esdeveniment>,
    private val viewEvent: (Esdeveniment) -> Unit
) : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ticketNameTextView: TextView = itemView.findViewById(R.id.ticketEventName)
        private val ticketQuantityTextView: TextView = itemView.findViewById(R.id.ticketQuantity)

        fun bind(ticket: Entrada, event: Esdeveniment?) {
            if (event == null) {
                ticketNameTextView.text = ""
            }else{
                ticketNameTextView.text = event.Nom
            }
            if (ticket.NumeroButaca == null){
                val quantity = ticket.Quantitat.toString() + " tickets"
                ticketQuantityTextView.text = quantity
            }else{
                ticketQuantityTextView.text = ticket.NumeroButaca
            }

            itemView.setOnClickListener {
                if (event != null) {
                    viewEvent(event)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_item, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        val event = events.find { it.EsdevenimentID == ticket.EsdevenimentID }

        holder.bind(ticket, event)
    }

    override fun getItemCount() = tickets.size
}