package com.example.massapp.fragments.options

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.massapp.R
import com.example.massapp.databinding.FragmentDetailCardBinding
import com.example.massapp.room.CardInfoDao
import com.example.massapp.room.MassAppDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCardFragment : Fragment() {
    private lateinit var binding: FragmentDetailCardBinding
    private var isCardFront = true
    lateinit var cardInfoDao: CardInfoDao
    private val handler = Handler(Looper.getMainLooper())
    private val flipCardDelayMillis = 3000L
    private lateinit var cardFaceFrontView: View
    private lateinit var cardFaceBackView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardInfoDao = MassAppDatabase.getInstance(context).cardInfoDao()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailCardBinding.inflate(inflater)

        // Inflo las vistas cardFaceFront y cardFaceBack
        cardFaceFrontView = inflater.inflate(R.layout.card_list_ittem, null)
        cardFaceBackView = inflater.inflate(R.layout.card_back_detail_list_item, null)


        val cardNumber = arguments?.getString("cardNumber")
        val amount = arguments?.getString("amount")
        val date = arguments?.getString("date")

        Log.e("TAG", "onCreateView: cardNumber=$cardNumber, amount=$amount, date=$date")

        binding.cardNumber.text = cardNumber

        binding.backManagement.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDelete.setOnClickListener {
            Log.e("TAG", "aqui entra")

            binding.btnDelete.setOnClickListener {

                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Eliminación de la tarjeta")
                    setMessage("¿Seguro quieres eliminar la tarjeta?")
                    setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Sí") { dialog, _ ->
                        // Obtén el número de tarjeta desde los argumentos
                        val cardNumber = arguments?.getString("cardNumber")

                        // Si el número de tarjeta no es nulo, realiza la eliminación
                        cardNumber?.let {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val cardInfo = cardInfoDao.getCardInfoByCardNumber(it)

                                // Si la tarjeta existe, elimínala de la base de datos
                                cardInfo?.let {
                                    cardInfoDao.deleteCardInfo(it)
                                }
                            }
                        }
                        findNavController().popBackStack()
                        dialog.dismiss()
                    }
                }

                alertDialog.create()
                alertDialog.show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicia la ejecución automática de flipCard cuando se crea la vista
        handler.postDelayed(flipCardRunnable, flipCardDelayMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Detén la ejecución automática de flipCard cuando se destruye la vista
        handler.removeCallbacks(flipCardRunnable)
    }

    private val flipCardRunnable = object : Runnable {
        override fun run() {
            flipCardAutomatically()
            handler.postDelayed(this, flipCardDelayMillis)
        }
    }

    private fun flipCardAutomatically() {
        // Lógica para cambiar automáticamente el estilo de la tarjeta
        val currentVisibleView = if (isCardFront) binding.cardFaceFront else binding.cardFaceBack
        val currentInVisibleView = if (isCardFront) binding.cardFaceBack else binding.cardFaceFront

        isCardFront = !isCardFront

        flipCard(requireContext(), currentVisibleView, currentInVisibleView)
    }

    private fun flipCard(context: Context, visibleView: View, inVisibleView: View) {
        try {
            visibleView.visibility = View.VISIBLE
            val scale = context.resources.displayMetrics.density
            val cameraDist = 8000F * scale
            visibleView.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist

            // Infla la vista card_back_detail_list_item.xml dentro del método flipCard
            val inflater = LayoutInflater.from(context)
            val cardBackListItemView = inflater.inflate(R.layout.card_back_detail_list_item, null)

            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(context, R.anim.flip_out) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)

            val flipInAnimatorSet =
                AnimatorInflater.loadAnimator(context, R.anim.flip_in) as AnimatorSet
            flipInAnimatorSet.setTarget(visibleView)

            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()
            flipInAnimatorSet.doOnEnd {
                binding.cardFlip.isEnabled = true
                cardBackListItemView.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            Log.e("TAG", "flipCard: $e")
        }
    }

}