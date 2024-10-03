package com.example.chatbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide


class PokemonAdapter(
    private var pokemonList: MutableList<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.pokemonName)
        val weightTextView: TextView = itemView.findViewById(R.id.pokemonWeight)
        val typeTextView: TextView = itemView.findViewById(R.id.pokemonType)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)
        val imageView: ImageView = itemView.findViewById(R.id.pokemonImage)

        fun bind(pokemon: Pokemon) {
            nameTextView.text = pokemon.name
            weightTextView.text = "Peso: ${pokemon.weight} kg"
            typeTextView.text = "Tipos: ${pokemon.types.joinToString(", ")}"


            Glide.with(itemView.context)
                .load(pokemon.imageUrl)
                .into(imageView)


            if (pokemon.isFavorite) {
                favoriteIcon.setImageResource(R.drawable.ic_star)
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_star_border)
            }

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

    fun updatePokemonList(newList: List<Pokemon>) {
        pokemonList.clear()
        pokemonList.addAll(newList)
        notifyDataSetChanged()
    }

}


