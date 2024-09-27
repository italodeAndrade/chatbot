package com.example.chatbox

data class Pokemon(
    val name: String,
    var weight: Int = 0,
    var types: List<String> = emptyList()
)


data class PokemonDetails(
    val weight: Int,
    val types: List<PokemonType>
)

data class PokemonType(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)
