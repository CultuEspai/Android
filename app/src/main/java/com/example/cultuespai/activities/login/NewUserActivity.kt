package com.example.cultuespai.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cultuespai.R
import com.example.cultuespai.activities.MainActivity
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.utils.api.ApiRepository
import kotlinx.coroutines.launch

class NewUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_user)

        val createButton = findViewById<Button>(R.id.continueButton)
        val loginButton = findViewById<TextView>(R.id.loginButton)

        val nameTextbox = findViewById<EditText>(R.id.nameTextbox)
        val emailTextbox = findViewById<EditText>(R.id.emailTextbox)
        val passwordTextbox = findViewById<EditText>(R.id.newPasswordTextbox)
        val confirmPasswordTextbox = findViewById<EditText>(R.id.confirmPasswordTextbox)

        createButton.setOnClickListener {
            val usuari = Usuari(0, "", "", "", "Normal", true, "")
            usuari.Nom = nameTextbox.text.toString()
            usuari.Correu = emailTextbox.text.toString()
            usuari.Contrasenya = passwordTextbox.text.toString()
            //usuari.contrasenyaHash = BCrypt.hashpw(passwordTextbox.text.toString(), BCrypt.gensalt())

            createUser(usuari)
        }

        loginButton.setOnClickListener {
            finish()
        }
    }

    private fun createUser(usuari: Usuari) {
        // Usar lifecycleScope para llamar a la función suspendida en el contexto adecuado
        lifecycleScope.launch {
            try {
                // Llamar a la función de creación de usuario
                val createdUser = ApiRepository.postUsuari(usuari)
                // Verificar si la respuesta fue exitosa
                if (createdUser != null) {
                    // Aquí puedes manejar la respuesta, como actualizar la UI con el nuevo usuario
                    Toast.makeText(this@NewUserActivity, "Usuario creado exitosamente: ${createdUser.Nom}", Toast.LENGTH_SHORT).show()

                    UserData.userId = createdUser.UsuariID
                    UserData.userType = createdUser.TipusUsuari

                    val intent = Intent(this@NewUserActivity, MainActivity::class.java)
                    startActivity(intent)
                    setResult(RESULT_OK)
                    finish()
                } else {
                    // Manejo de error si la creación del usuario falla
                    Toast.makeText(this@NewUserActivity, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                // Manejo de errores de red o excepciones
                Toast.makeText(this@NewUserActivity, "Error de conexión: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}