package com.example.massapp.fragments.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.massapp.R
import com.example.massapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // Creo la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        // Navego al fragmento de registro de tarjeta al hacer click en el primer cardView
        binding.cardViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerCardFragment)
        }

        // Navego al fragmento de gestión de tarjeta al hacer click en el segundo cardView
        binding.secondCardView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gestionCardFragment)
        }

        // Agrego un callback para gestionar el botón de retroceso de la actividad
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //
                }
            })

        return binding.root
    }
}