package com.example.chatbox

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbox.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var pokemonAdapter: PokemonAdapter
    private val pokemonList = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pokemonAdapter = PokemonAdapter(pokemonList) { pokemon ->
            showPokemonDetails(pokemon)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = pokemonAdapter
        }

        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pokeApi = retrofit.create(PokeApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pokeApi.getPokemonList(limit = 5)
                if (response.isSuccessful) {
                    response.body()?.let { pokemons ->
                        pokemonList.clear()
                        pokemonList.addAll(pokemons.results)


                        pokemons.results.forEach { pokemon ->
                            val detailsResponse = pokeApi.getPokemonDetails(pokemon.name)
                            if (detailsResponse.isSuccessful) {
                                detailsResponse.body()?.let { details ->

                                    val pokemonToUpdate = pokemonList.find { it.name == pokemon.name }
                                    pokemonToUpdate?.weight = details.weight
                                    pokemonToUpdate?.types = details.types.map { it.type.name }
                                }
                            }
                        }

                        withContext(Dispatchers.Main) {
                            pokemonAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeActivity, "Erro ao carregar Pokémon", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showPokemonDetails(pokemon: Pokemon) {
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            putExtra("pokemon_name", pokemon.name)
            putExtra("pokemon_weight", pokemon.weight)
            putExtra("pokemon_types", pokemon.types.toTypedArray())
        }
        startActivity(intent)
    }
}
