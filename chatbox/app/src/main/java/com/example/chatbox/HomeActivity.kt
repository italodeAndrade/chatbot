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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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
        val retrofit = createRetrofit()
        val pokeApi = retrofit.create(PokeApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pokeApi.getPokemonList(limit = 10)
                if (response.isSuccessful) {
                    response.body()?.let { pokemons ->
                        updatePokemonList(pokemons.results, pokeApi)
                    }
                } else {
                    showErrorToast("Erro ao carregar Pokémon")
                }
            } catch (e: Exception) {
                showErrorToast("Erro: ${e.message}")
            }
        }
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private suspend fun updatePokemonList(results: List<Pokemon>, pokeApi: PokeApiService) {
        pokemonList.clear()
        pokemonList.addAll(results)

        coroutineScope {
            val pokemonDetailsDeferred = results.map { pokemon ->
                async {
                    val detailsResponse = pokeApi.getPokemonDetails(pokemon.name)
                    detailsResponse.body()?.let { details ->
                        val pokemonToUpdate = pokemonList.find { it.name == pokemon.name }
                        pokemonToUpdate?.apply {
                            weight = details.weight
                            types = details.types.map { it.type.name }
                            imageUrl = details.sprites.front_default
                        }
                    }
                }
            }
            pokemonDetailsDeferred.awaitAll()
        }

        withContext(Dispatchers.Main) {
            pokemonAdapter.notifyDataSetChanged()
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
