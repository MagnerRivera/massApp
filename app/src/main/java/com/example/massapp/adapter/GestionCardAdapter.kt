package com.example.massapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.massapp.R
import com.example.massapp.fragments.options.GestionCardFragmentDirections
import com.example.massapp.room.CardInfo

class GestionCardAdapter : RecyclerView.Adapter<GestionCardAdapter.CardViewHolder>() {

    // Lista de tarjetas a mostrar en el RecyclerView
    var cardList: List<CardInfo> = emptyList()

    // Interfaz para manejar los clicks al pulsar un item del adaptador
    interface OnItemClickListener {
        fun onItemClick(cardInfo: CardInfo, amount: String, date: String)
    }

    // Inicialización predeterminada del OnItemClickListener
    var onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(cardInfo: CardInfo, amount: String, date: String) {
            //
        }
    }

    // ViewHolder para mantener las vistas de cada elemento de tarjeta

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumberCard: TextView = itemView.findViewById(R.id.tvNumberCard)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
    }
    // Creo un nuevo ViewHolder cuando agrego una nueva tarjeta
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_back_list_item, parent, false)
        return CardViewHolder(view)
    }

    // Lleno el ViewHolder con datos de la tarjeta obtenidos desde la base de datos
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = cardList[position]

        holder.tvNumberCard.text = currentCard.card
        holder.amount.text = currentCard.balance.toString()
        holder.date.text = currentCard.balanceDate

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(
                currentCard,
                currentCard.balance.toString(),
                currentCard.balanceDate
            )
        }
    }


    // Devuelvo la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return cardList.size
    }
}