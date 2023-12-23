package com.example.massapp.fragments.options

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.massapp.animationUtils.AnimationUtils
import com.example.massapp.databinding.FragmentRegisterCardBinding
import com.example.massapp.retrofit.BalanceResponse
import com.example.massapp.retrofit.CardResponse
import com.example.massapp.retrofit.RetrofitClient
import com.example.massapp.room.CardInfo
import com.example.massapp.room.MassAppDatabase
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

@AndroidEntryPoint
class RegisterCardFragment : Fragment() {
    private lateinit var binding: FragmentRegisterCardBinding
    private lateinit var progressBarCircular: ProgressBar
    private lateinit var activity: AppCompatActivity
    private val handler = Handler()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterCardBinding.inflate(inflater)
        progressBarCircular = binding.progressBarCircular

        binding.backManagement.setOnClickListener {
            findNavController().popBackStack()
        }

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

        binding.buttonSearchCard.setOnClickListener {

            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.buttonSearchCard.windowToken, 0)

            progressBarCircular.visibility = View.VISIBLE
            binding.consCard.visibility = View.INVISIBLE
            binding.tvEmpty.visibility = View.INVISIBLE
            binding.linearOptions.visibility = View.INVISIBLE

            val cardNumber = binding.editTextSearch.text.toString().trim()
            Log.d("TAG", "Enviando solicitud con número de tarjeta: $cardNumber")

            if (cardNumber.isNotEmpty()) {
                val authToken =
                    "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJtYWFzIiwiaXNzIjoicmJzYXMuY28iLCJjb21wYW55IjoiMTAwMiIsImV4cCI6MTcwMzQxOTI3NiwidXNlciI6InBydWViYS5hbmRyb2lkIiwiaWF0IjoxNzAzMTk2MDc2LCJHcnVwb3MiOiJbXCJVbml2ZXJzYWxSZWNoYXJnZXJcIl0ifQ.JZ6dnujyohmUYl4J4d-8tvvit4FwFxPFcPKYuoSONaEzHAsfPZw-yIV9baUXpeNd6IeJMD1AoQO7Z6Co524xldt3-2bTBIoXFxuZ-8jc1AUaVf4qJ9CxtQruOBYUdHSLMsOAF4EuNpVKDcSyRYdddXm6ZjYncSWcfl1q8MsOZDlbqM__y1CfOtJY-6DROzJzz2eNblL4bHrUNJbuPe3MOKXM9d9vsUSd3zEnRT5847_UPDJxNotIcHgHO_UhArNydq4DFG11NdUa_Wav63jjPrGEUWEVlLkxxWQ-6EDzfLFLiFP0dVfOQrWGvYnBZrhwKb6niFXqXR5tGkhidQ0Epg"

                val cardService = RetrofitClient.create().getBalance(authToken, cardNumber)

                val requestUrl = cardService.request().url.toString()
                Log.d("TAG", "URL de la solicitud: $requestUrl")

                cardService.enqueue(object : Callback<CardResponse> {
                    override fun onResponse(
                        call: Call<CardResponse>, response: Response<CardResponse>
                    ) {
                        if (response.isSuccessful) {
                            val cardResponse = response.body()
                            Log.d("TAG", "Card Status: ${cardResponse?.status}")

                            if (cardResponse?.isValid == false) {
                                handler.post {
                                    showToast(requireContext(), "Tarjeta no válida")
                                    editTextSearch.setText("")
                                    progressBarCircular.visibility = View.INVISIBLE
                                }
                                return
                            }

                            val balanceService =
                                RetrofitClient.create().getBalanceDetails(authToken, cardNumber)

                            Log.d("TAG", "Enviando solicitud2 con número de tarjeta: $cardNumber")

                            val requestUrlBalance = balanceService.request().url.toString()
                            Log.d("TAG", "URL de la solicitud2: $requestUrlBalance")

                            balanceService.enqueue(object : Callback<BalanceResponse> {
                                override fun onResponse(
                                    call: Call<BalanceResponse>,
                                    balanceResponse: Response<BalanceResponse>
                                ) {
                                    progressBarCircular.visibility = View.INVISIBLE
                                    binding.linearOptions.visibility = View.VISIBLE


                                    if (balanceResponse.isSuccessful) {
                                        val detailsResponse = balanceResponse.body()
                                        Log.d("TAG", "Balance: ${detailsResponse?.balance}")

                                        handler.post {
                                            binding.btnCheck.setOnClickListener {
                                                val cardInfo = CardInfo(
                                                    card = cardResponse?.card ?: "",
                                                    balance = detailsResponse?.balance ?: 0,
                                                    balanceDate = detailsResponse?.balanceDate ?: ""
                                                )

                                                binding.consCard.visibility = View.INVISIBLE
                                                binding.linearOptions.visibility = View.INVISIBLE
                                                editTextSearch.setText("")

                                                val executor = Executors.newSingleThreadExecutor()

                                                executor.execute {
                                                    val database =
                                                        MassAppDatabase.getInstance(requireContext())

                                                    val existingCard = database.cardInfoDao()
                                                        .getCardInfoByCardNumber(
                                                            cardResponse?.card ?: ""
                                                        )

                                                    if (existingCard == null) {
                                                        database.cardInfoDao()
                                                            .insertCardInfo(cardInfo)
                                                        showToast(
                                                            requireContext(),
                                                            "Tarjeta almacenada con éxito"
                                                        )
                                                    } else {
                                                        showToast(
                                                            requireContext(),
                                                            "Tarjeta ya registrada"
                                                        )
                                                    }
                                                }
                                            }


                                            binding.btnCancel.setOnClickListener {
                                                binding.consCard.visibility = View.INVISIBLE
                                                binding.linearOptions.visibility = View.INVISIBLE
                                                editTextSearch.setText("")

                                            }

                                            binding.tvNumberCard.text = cardResponse?.card
                                            binding.amount.text =
                                                "$" + detailsResponse?.balance.toString()
                                            binding.date.text = detailsResponse?.balanceDate ?: ""


                                            binding.consCard.visibility = View.VISIBLE
                                            binding.linearOptions.visibility = View.VISIBLE
                                            binding.tvEmpty.visibility = View.INVISIBLE
                                        }

                                    } else {
                                        Log.e(
                                            "TAG",
                                            "Error en la segunda respuesta: ${balanceResponse.message()}"
                                        )
                                    }

                                }


                                override fun onFailure(
                                    call: Call<BalanceResponse>, t: Throwable
                                ) {
                                    progressBarCircular.visibility = View.INVISIBLE
                                    Log.e("TAG", "Error en la segunda llamada: ${t.message}")
                                }
                            })

                        } else {
                            Log.e("TAG", "Error en la primera respuesta: ${response.message()}")
                            progressBarCircular.visibility = View.INVISIBLE
                            binding.consCard.visibility = View.INVISIBLE
                            binding.linearOptions.visibility = View.INVISIBLE
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<CardResponse>, t: Throwable) {
                        Log.e("TAG", "Error en la primera llamada: ${t.message}")
                    }
                })
            } else {
                Log.d("TAG", "El número de tarjeta está vacío")
                progressBarCircular.visibility = View.INVISIBLE
                binding.consCard.visibility = View.INVISIBLE
                binding.linearOptions.visibility = View.INVISIBLE
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    fun showToast(context: Context, message: String) {
        handler.post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}