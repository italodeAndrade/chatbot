package com.example.chatbox

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int): Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(@Path("name") name: String): Response<PokemonDetails>

    @GET("pokemon-species/{name}")
    suspend fun getPokemonSpecies(@Path("name") name: String): Response<PokemonSpecies>

}

