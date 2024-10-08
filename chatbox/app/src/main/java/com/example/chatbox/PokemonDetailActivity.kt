package com.example.chatbox

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import android.transition.Fade
import androidx.appcompat.app.AppCompatActivity

class PokemonDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        window.enterTransition = Fade()
        window.exitTransition = Fade()



        val imageView: ImageView = findViewById(R.id.pokemonDetailImage)
        val nameTextView: TextView = findViewById(R.id.pokemonDetailName)
        val weightTextView: TextView = findViewById(R.id.pokemonDetailWeight)
        val typesTextView: TextView = findViewById(R.id.pokemonDetailTypes)
        val descriptionTextView: TextView = findViewById(R.id.pokemonDetailDescription)


        nameTextView.text = intent.getStringExtra("pokemon_name")
        weightTextView.text = "Peso: ${intent.getIntExtra("pokemon_weight", 0)} kg"
        typesTextView.text = "Tipos: ${intent.getStringArrayExtra("pokemon_types")?.joinToString(", ")}"
        descriptionTextView.text = intent.getStringExtra("pokemon_description")

        Glide.with(this)
            .load(intent.getStringExtra("pokemon_image_url"))
            .into(imageView)

    }

}

