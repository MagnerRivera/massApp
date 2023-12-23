package com.example.massapp.fragments.loginRegister

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.massapp.R
import com.example.massapp.databinding.FragmentRegisterBinding
import com.example.massapp.room.User
import com.example.massapp.utils.RegisterValidation
import com.example.massapp.utils.Resource
import com.example.massapp.utils.validateAddress
import com.example.massapp.utils.validateDocument
import com.example.massapp.utils.validateName
import com.example.massapp.utils.validatePassword
import com.example.massapp.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    private var isDocumentEditable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    //method for the view to register a new user
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registrerFragment_to_loginFragment)
        }

        binding.apply {

            val maxDigits = 12

            edNameRegister.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val name = edNameRegister.text.toString().trim()
                    val capitalized = name.split(" ").joinToString(" ") { it.capitalize() }
                    edNameRegister.setText(capitalized)
                }
            }

            edDocumentRegister.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxDigits))

            val spinnerValues = arrayOf("Seleccione tipo de documento", "Cédula de ciudadania", "Cédula de extranjeria", "Pasaporte")
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerValues)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerValues.adapter = spinnerAdapter

            binding.spinnerValues.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    isDocumentEditable = position != 0
                    edDocumentRegister.isEnabled = isDocumentEditable
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No se necesita hacer nada aquí
                }
            }

            buttonRegisterRegister.setOnClickListener {
                if (!isDocumentEditable) {
                    Toast.makeText(requireContext(), "Seleccione un tipo de documento", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val nameRegex = edNameRegister.text.toString().trim()
                val emailRegex = edEmailRegister.text.toString().trim()
                val addressRegex = edAddressRegister.text.toString().trim()
                val documentRegex = edDocumentRegister.text.toString().trim()

                if (!Patterns.EMAIL_ADDRESS.matcher(emailRegex)
                        .matches() || !emailRegex.contains("@") || (!emailRegex.endsWith(".com") && !emailRegex.endsWith(
                        ".co"
                    ))
                ) {
                    binding.edEmailRegister.apply {
                        requestFocus()
                        error = "Formato de correo electrónico incorrecto"
                    }
                    return@setOnClickListener
                }

                val passwordRegex = edPasswordRegister.text.toString().trim()

                val nameValidation = validateName(nameRegex)
                if (nameValidation is RegisterValidation.Failed) {
                    edNameRegister.apply {
                        requestFocus()
                        error = nameValidation.message
                    }
                    return@setOnClickListener
                }
                val addressValidation = validateAddress(addressRegex)
                if (addressValidation is RegisterValidation.Failed) {
                    edAddressRegister.apply {
                        requestFocus()
                        error = addressValidation.message
                    }
                    return@setOnClickListener
                }
                val documentValidation = validateDocument(documentRegex)
                if (documentValidation is RegisterValidation.Failed) {
                    edDocumentRegister.apply {
                        requestFocus()
                        error = documentValidation.message
                    }
                    return@setOnClickListener
                }

                val passwordValidation = validatePassword(passwordRegex)
                if (passwordValidation is RegisterValidation.Failed) {
                    binding.edPasswordRegister.apply {
                        requestFocus()
                        error = passwordValidation.message
                    }
                    return@setOnClickListener
                }


                val user = User(
                    username = nameRegex,
                    email = emailRegex,
                    password = passwordRegex,
                    document = documentRegex,
                    address = addressRegex,
                )

                lifecycleScope.launch {
                    viewModel.createAccountWithEmailAndPassword(user, passwordRegex)
                }
            }
        }

        //lifecycleScope is a coroutine, to automatically manage coroutines based on the fragment lifecycle

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegisterRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Log.e("test", it.data.toString())
                        binding.buttonRegisterRegister.revertAnimation()

                        Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.menssage.toString())
                        binding.buttonRegisterRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edEmailRegister.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edPasswordRegister.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}