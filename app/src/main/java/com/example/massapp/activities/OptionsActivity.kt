package com.example.massapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.massapp.R
import com.example.massapp.databinding.ActivityOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityOptionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.optionsHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

}