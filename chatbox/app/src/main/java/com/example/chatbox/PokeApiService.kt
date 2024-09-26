package com.example.chatbox

import retrofit2.Response
import retrofit2.http.GET

interface PokeApiService {
    @GET("pokemon?limit=100")
    suspend fun getPokemonList(): Response<PokemonResponse>
}
