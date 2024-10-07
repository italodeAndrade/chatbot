package com.example.chatbox

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide

class PokemonDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        val pokemonName = intent.getStringExtra("pokemon_name") ?: "Nome não disponível"
        val pokemonWeight = intent.getIntExtra("pokemon_weight", 0)
        val pokemonTypes = intent.getStringArrayExtra("pokemon_types")?.joinToString(", ") ?: "Tipos não disponíveis"
        val pokemonImageUrl = intent.getStringExtra("pokemon_image_url") ?: ""
        val pokemonDescription = intent.getStringExtra("pokemon_description") ?: "Descrição não disponível"

        val pokemonImageView: ImageView = findViewById(R.id.pokemon_image)
        val pokemonNameView: TextView = findViewById(R.id.pokemon_name)
        val pokemonTypesView: TextView = findViewById(R.id.pokemon_types)
        val pokemonWeightView: TextView = findViewById(R.id.pokemon_weight)
        val pokemonDescriptionView: TextView = findViewById(R.id.pokemon_description)

        Glide.with(this)
            .load(pokemonImageUrl)
            .into(pokemonImageView)

        pokemonNameView.text = pokemonName
        pokemonTypesView.text = "Tipos: $pokemonTypes"
        pokemonWeightView.text = "Peso: $pokemonWeight kg"
        pokemonDescriptionView.text = pokemonDescription
    }

}

