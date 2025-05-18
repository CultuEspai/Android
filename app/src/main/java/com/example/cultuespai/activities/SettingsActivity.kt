package com.example.cultuespai.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cultuespai.R
import com.example.cultuespai.activities.login.LoginActivity
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.utils.activateNavBar
import com.example.cultuespai.utils.api.ApiRepository.getEntradesByUser
import com.example.cultuespai.utils.api.ApiRepository.getSalaById
import com.example.cultuespai.utils.api.ApiRepository.getUsuariById
import com.example.cultuespai.utils.api.ApiRepository.putUsuari
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        activateNavBar(this, this, 4)

        logOutButtonListener()
        languageButtonsListener()

        val nameTextbox = findViewById<EditText>(R.id.nameTextbox)
        val emailTextbox = findViewById<EditText>(R.id.emailTextbox)

        lifecycleScope.launch {
            try {
                val user = getUsuariById(UserData.userId)
                nameTextbox.setHint(user?.Nom)
                emailTextbox.setHint(user?.Correu)
                updateButtonListener(user, nameTextbox, emailTextbox)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }
    }

    private fun languageButtonsListener() {
        val caButton = findViewById<Button>(R.id.ca)
        val esButton = findViewById<Button>(R.id.es)
        val enButton = findViewById<Button>(R.id.en)

        val currentLocale = resources.configuration.locales.get(0)
        val currentLanguage = currentLocale.language

        when (currentLanguage) {
            "ca" -> {
                languageButtonOn(caButton)
                languageButtonOff(esButton)
                languageButtonOff(enButton)
            }
            "es" -> {
                languageButtonOn(esButton)
                languageButtonOff(caButton)
                languageButtonOff(enButton)
            }
            "en" -> {
                languageButtonOn(enButton)
                languageButtonOff(caButton)
                languageButtonOff(esButton)
            }
        }

        caButton.setOnClickListener {
            languageButtonOn(caButton)
            languageButtonOff(esButton)
            languageButtonOff(enButton)

            changeLenguage("ca")
        }

        esButton.setOnClickListener {
            languageButtonOn(esButton)
            languageButtonOff(caButton)
            languageButtonOff(enButton)

            changeLenguage("es")
        }

        enButton.setOnClickListener {
            languageButtonOn(enButton)
            languageButtonOff(caButton)
            languageButtonOff(esButton)

            changeLenguage("en")
        }
    }

    private fun changeLenguage(lenguage: String){
        val locale = Locale(lenguage)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        recreate()
    }

    private fun languageButtonOn(button: Button){
        button.setTextColor(this.resources.getColor(R.color.white))
        button.setBackgroundColor(this.resources.getColor(R.color.turquoise))
        button.isEnabled = false
        }

    private fun languageButtonOff(button: Button){
        button.setTextColor(this.resources.getColor(R.color.black))
        button.setBackgroundColor(this.resources.getColor(R.color.white))
        button.isEnabled = true
    }

    private fun updateButtonListener(user: Usuari?, nameTextbox: EditText, emailTextbox: EditText) {
        val updateButton = findViewById<Button>(R.id.update)

        updateButton.setOnClickListener {
            val userUpdated = validateData(user, nameTextbox, emailTextbox)

            lifecycleScope.launch {
                try {
                    if (userUpdated != null){
                        val updatedUser = putUsuari(userUpdated.UsuariID, userUpdated)
                        if (updatedUser) {
                            Toast.makeText(this@SettingsActivity,
                                this@SettingsActivity.getString(R.string.user_data_updated),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e: Exception)
                {
                    println("API Connexion Error")
                }
            }
        }
    }

    private fun validateData(user: Usuari?, nameTextbox: EditText, emailTextbox: EditText): Usuari? {
        val passwordTextbox = findViewById<EditText>(R.id.passwordTextbox)
        val confirmPasswordTextbox = findViewById<EditText>(R.id.confirmPasswordTextbox)
        val userUpdated = user
        var change = false

        if (nameTextbox.text.toString().isNotBlank() && nameTextbox.text.toString() != user?.Nom) {
            userUpdated?.Nom = nameTextbox.text.toString()
            change = true
        }
        if (emailTextbox.text.toString().isNotBlank() && emailTextbox.text.toString() != user?.Correu) {
            userUpdated?.Correu = emailTextbox.text.toString()
            change = true
        }
        if (passwordTextbox.text.toString().isNotBlank() &&
            confirmPasswordTextbox.text.toString() == passwordTextbox.text.toString() &&
            passwordTextbox.text.toString() != user?.Contrasenya) {
            userUpdated?.Contrasenya = passwordTextbox.text.toString()
            change = true
        }

        if (!change) {
            Toast.makeText(this, this.getString(R.string.no_changes), Toast.LENGTH_SHORT).show()
            return null
        }else{
            return userUpdated
        }
    }

    private fun logOutButtonListener() {
        val logOutButton = findViewById<Button>(R.id.logOut)
        logOutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}