package com.example.chatbox

import androidx.room.PrimaryKey


data class Pokemon(
    @PrimaryKey val name: String,
    var weight: Int = 0,
    var types: List<String> = emptyList(),
    var isFavorite: Boolean = false,
    var imageUrl: String = "",
    var description: String = ""
)


data class PokemonDetails(
    val weight: Int,
    val types: List<PokemonType>,
    val sprites: Sprites,
    val flavor_text_entries: List<FlavorTextEntry>
)


data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language
)


data class Language(
    val name: String,
    val url: String
)


data class PokemonType(
    val type: TypeInfo
)


data class TypeInfo(
    val name: String
)

data class Sprites(
    val front_default: String
)
