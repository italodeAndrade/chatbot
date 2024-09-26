package com.example.chatbox

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chatbox.databinding.ActivityCadastroBinding
import kotlinx.coroutines.launch
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext




@Entity(tableName = "login_table")
data class LoginEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val senha: String,
    val dataNascimento: String
)

@Dao
interface LoginDao {
    @Insert
    fun insertLogin(login: LoginEntity)

    @Query("SELECT * FROM login_table")
    fun getAllLogins(): Flow<List<LoginEntity>>
}

@Database(entities = [LoginEntity::class], version = 1, exportSchema = false)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var INSTANCE: LoginDatabase? = null

        fun getDatabase(context: Context): LoginDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoginDatabase::class.java,
                    "login_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class cadastro : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)
        binding.editTextDate.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val mask = "##/##/####"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                var text = s.toString().filter { it.isDigit() }

                if (text.length > 8) {
                    text = text.substring(0, 8)
                }

                val newText = StringBuilder()
                var i = 0

                for (m in mask.toCharArray()) {
                    if (m != '#' && text.length > i) {
                        newText.append(m)
                    } else {
                        if (i >= text.length) break
                        newText.append(text[i])
                        i++
                    }
                }

                isUpdating = true
                s?.replace(0, s.length, newText)
                isUpdating = false
            }
        })

        binding.btReg.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.editTextText2.text.toString()
            val dataNascimento = binding.editTextDate.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty() && dataNascimento.isNotEmpty()) {
                if (!isEmailValid(email)) {
                    Toast.makeText(this@cadastro, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val loginEntity = LoginEntity(email = email, senha = senha, dataNascimento = dataNascimento)
                // Insira o registro usando coroutines
                lifecycleScope.launch {
                    insertLogin(loginEntity)
                }
            } else {
                Toast.makeText(this@cadastro, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.lgRed.setOnClickListener {
            val intent = Intent(this@cadastro, login::class.java)
            startActivity(intent)
        }
    }

    private suspend fun insertLogin(loginEntity: LoginEntity) {
        withContext(Dispatchers.IO) {
            try {
                db.loginDao().insertLogin(loginEntity)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@cadastro, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    binding.email.text.clear()
                    binding.editTextText2.text.clear()
                    binding.editTextDate.text.clear()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@cadastro, "Erro ao registrar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Função para validar o formato do e-mail usando regex
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}










