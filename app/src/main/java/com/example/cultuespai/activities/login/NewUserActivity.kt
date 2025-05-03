package com.example.cultuespai.activities.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cultuespai.R
import com.example.cultuespai.entities.Usuari

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
            val usuari = Usuari(0, "", "", "", "", true, "")
            usuari.nom = nameTextbox.text.toString()
            usuari.correu = emailTextbox.text.toString()
            usuari.contrasenya = passwordTextbox.text.toString()
//            usuari.contrasenyaHash = BCrypt.hashpw(passwordTextbox.text.toString(), BCrypt.gensalt())

            finish()
        }

        loginButton.setOnClickListener {
            finish()
        }
    }
}