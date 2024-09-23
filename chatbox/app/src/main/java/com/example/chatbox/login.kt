package com.example.chatbox

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbox.databinding.ActivityMainBinding


class login : AppCompatActivity() { lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inscreverse.setOnClickListener {
            val intent = Intent(this, cadastro::class.java)
            startActivity(intent)
        }
    }
}