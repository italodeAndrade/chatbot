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

class Cadastro : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var db: LoginDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = LoginDatabase.getDatabase(applicationContext)

        binding.btReg.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.editTextText2.text.toString()
            val dataNascimento = binding.editTextDate.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty() && dataNascimento.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val loginEntity = LoginEntity(email = email, senha = senha, dataNascimento = dataNascimento)
                        db.loginDao().insertLogin(loginEntity)
                        Toast.makeText(this@Cadastro, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        binding.email.text.clear()
                        binding.editTextText2.text.clear()
                        binding.editTextDate.text.clear()
                    } catch (e: Exception) {
                        Toast.makeText(this@Cadastro, "Erro ao registrar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.lgRed.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}




