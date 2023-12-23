package com.example.massapp.fragments.options

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.massapp.R
import com.example.massapp.adapter.GestionCardAdapter
import com.example.massapp.animationUtils.AnimationUtils
import com.example.massapp.databinding.FragmentGestionCardBinding
import com.example.massapp.room.CardInfo
import com.example.massapp.room.CardInfoDao
import com.example.massapp.room.MassAppDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@AndroidEntryPoint
class GestionCardFragment : Fragment() {
    private lateinit var binding: FragmentGestionCardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: GestionCardAdapter
    private lateinit var cardInfoDao: CardInfoDao
    private lateinit var originalCardList: List<CardInfo>
    private lateinit var filteredCardList: MutableList<CardInfo>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardInfoDao = MassAppDatabase.getInstance(context).cardInfoDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGestionCardBinding.inflate(inflater)
        recyclerView = binding.root.findViewById(R.id.recyclerView)

        val imageSearch = binding.imageSearch
        val editTextSearch = binding.editTextSearch

        imageSearch.setOnClickListener {
            if (editTextSearch.visibility == View.VISIBLE) {
                AnimationUtils.slideViewUp(editTextSearch)
                editTextSearch.visibility = View.INVISIBLE
            } else {
                AnimationUtils.slideViewDown(editTextSearch)
                editTextSearch.visibility = View.VISIBLE
            }
        }

        cardAdapter = GestionCardAdapter()
        recyclerView.adapter = cardAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.backManagement.setOnClickListener {
            findNavController().popBackStack()
        }

        cardAdapter.onItemClickListener = object : GestionCardAdapter.OnItemClickListener {
            override fun onItemClick(cardInfo: CardInfo, amount: String, date: String) {
                val action = GestionCardFragmentDirections
                    .actionGestionCardFragmentToDetailCardFragment(
                        cardNumber = cardInfo.card,
                        amount = amount,
                        date = date
                    )
                findNavController().navigate(action)
            }
        }

        lifecycleScope.launch {
            originalCardList = withContext(Dispatchers.IO) {
                cardInfoDao.getAllCardInfo()
            }

            filteredCardList = originalCardList.toMutableList()

            cardAdapter.cardList = filteredCardList
            cardAdapter.notifyDataSetChanged()

            if (filteredCardList.isEmpty()) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.INVISIBLE
            }
        }

        // Agrego un TextChangedListener al EditText para realizar la búsqueda en tiempo real
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Filtro la lista original con el texto ingresado
                filteredCardList.clear()
                if (charSequence.isNullOrEmpty()) {
                    filteredCardList.addAll(originalCardList)
                } else {
                    val searchText = charSequence.toString().toLowerCase(Locale.getDefault())
                    for (card in originalCardList) {
                        if (card.card.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredCardList.add(card)
                        }
                    }
                }

                // Notifico al adaptador sobre los cambios en la lista filtrada
                cardAdapter.notifyDataSetChanged()

                // Actualizo la visibilidad del RecyclerView y el TextView de vacío
                if (filteredCardList.isEmpty()) {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        return binding.root
    }
}