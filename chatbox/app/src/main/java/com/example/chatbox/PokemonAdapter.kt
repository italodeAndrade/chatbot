package com.example.chatbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(
    private var pokemonList: MutableList<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.pokemonName)
        private val weightTextView: TextView = itemView.findViewById(R.id.pokemonWeight)
        private val typeTextView: TextView = itemView.findViewById(R.id.pokemonType)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)
        private val imageView: ImageView = itemView.findViewById(R.id.pokemonImage)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.pokemonDescription)

        fun bind(pokemon: Pokemon) {
            nameTextView.text = pokemon.name
            weightTextView.text = "Peso: ${pokemon.weight} kg"
            typeTextView.text = "Tipos: ${pokemon.types.joinToString(", ")}"
            descriptionTextView.text = pokemon.description


            Glide.with(itemView.context)
                .load(pokemon.imageUrl)
                .into(imageView)

            favoriteIcon.setImageResource(
                if (pokemon.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            )

            favoriteIcon.setOnClickListener {
                pokemon.isFavorite = !pokemon.isFavorite
                notifyItemChanged(adapterPosition)
            }

            itemView.setOnClickListener {
                onItemClick(pokemon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.bind(pokemon)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}
