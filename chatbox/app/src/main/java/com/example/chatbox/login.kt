package com.example.chatbox

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chatbox.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class login : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)

        // Verificar se o usuário já está logado
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Se estiver logado, redireciona diretamente para a HomeActivity
            val intent = Intent(this@login, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.inscreverse.setOnClickListener {
            val intent = Intent(this, cadastro::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.senha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                lifecycleScope.launch {
                    val isValid = validateLogin(email, senha)
                    withContext(Dispatchers.Main) {
                        if (isValid) {
                            // Login bem-sucedido: salvar estado de login em SharedPreferences
                            with(sharedPref.edit()) {
                                putBoolean("isLoggedIn", true)
                                apply()
                            }

                            Toast.makeText(this@login, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@login, HomeActivity::class.java)
                            startActivity(intent)
                            finish() // Evita que o usuário volte para a tela de login
                        } else {
                            Toast.makeText(this@login, "E-mail ou senha inválidos!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this@login, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun validateLogin(email: String, senha: String): Boolean {
        return withContext(Dispatchers.IO) {
            val logins = db.loginDao().getAllLogins().first()
            logins.any { it.email == email && it.senha == senha }
        }
    }
}



