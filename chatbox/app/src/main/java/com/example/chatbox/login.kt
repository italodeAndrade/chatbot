package com.example.chatbox

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chatbox.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first



class login : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)

        binding.inscreverse.setOnClickListener {
            val intent = Intent(this, cadastro::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener { // Alterado para o ID correto do botão
            val email = binding.email.text.toString()
            val senha = binding.senha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                lifecycleScope.launch {
                    val isValid = validateLogin(email, senha)
                    withContext(Dispatchers.Main) {
                        if (isValid) {
                            Toast.makeText(this@login, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@login, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
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
