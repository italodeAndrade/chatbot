package com.example.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatbox.ui.theme.ChatboxTheme

class PokemonDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar os dados enviados pela HomeActivity
        val pokemonName = intent.getStringExtra("pokemon_name") ?: "Nome não disponível"
        val pokemonWeight = intent.getIntExtra("pokemon_weight", 0)
        val pokemonTypes = intent.getStringArrayExtra("pokemon_types")?.joinToString(", ") ?: "Tipos não disponíveis"

        setContent {
            ChatboxTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonDetailScreen(
                        name = pokemonName,
                        weight = pokemonWeight,
                        types = pokemonTypes,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailScreen(name: String, weight: Int, types: String, modifier: Modifier = Modifier) {
    Text(
        text = "Nome: $name\nPeso: $weight kg\nTipos: $types",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailPreview() {
    ChatboxTheme {
        PokemonDetailScreen(name = "Pikachu", weight = 60, types = "Elétrico")
    }
}
