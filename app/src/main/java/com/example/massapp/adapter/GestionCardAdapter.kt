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
    var cardList: List<CardInfo> = emptyList()

    interface OnItemClickListener {
        fun onItemClick(cardInfo: CardInfo, amount: String, date: String)
    }

    var onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(cardInfo: CardInfo, amount: String, date: String) {
            //
        }
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumberCard: TextView = itemView.findViewById(R.id.tvNumberCard)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val date: TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_back_list_item, parent, false)
        return CardViewHolder(view)
    }

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


    override fun getItemCount(): Int {
        return cardList.size
    }
}