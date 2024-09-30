package com.example.chatbox

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

        setupRecyclerView()
        fetchPokemonList()
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonAdapter(pokemonList) { pokemon ->
            showPokemonDetails(pokemon)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = pokemonAdapter
    }


    private fun fetchPokemonList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pokeApi = retrofit.create(PokeApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = pokeApi.getPokemonList(limit = 9)
                if (response.isSuccessful) {
                    response.body()?.let { pokemons ->
                        pokemonList.addAll(pokemons.results)


                        pokemons.results.forEach { pokemon ->
                            val detailsResponse = pokeApi.getPokemonDetails(pokemon.name)
                            if (detailsResponse.isSuccessful) {
                                detailsResponse.body()?.let { details ->
                                    // Atualiza o Pokémon com os detalhes
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

    }
}
