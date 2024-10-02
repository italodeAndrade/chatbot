package com.example.chatbox

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var pokemonDetailName: TextView
    private lateinit var pokemonDetailWeight: TextView
    private lateinit var pokemonDetailTypes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        pokemonDetailName = findViewById(R.id.pokemonDetailName)
        pokemonDetailWeight = findViewById(R.id.pokemonDetailWeight)
        pokemonDetailTypes = findViewById(R.id.pokemonDetailTypes)

        val name = intent.getStringExtra("pokemon_name") ?: "Nome não disponível"
        val weight = intent.getStringExtra("pokemon_weight") ?: "Peso não disponível"
        val types = intent.getStringArrayExtra("pokemon_types")?.joinToString(", ") ?: "Tipos não disponíveis"

        pokemonDetailName.text = name
        pokemonDetailWeight.text = "Peso: $weight kg"
        pokemonDetailTypes.text = "Tipos: $types"
    }
}

