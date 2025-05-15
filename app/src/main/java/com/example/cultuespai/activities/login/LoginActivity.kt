package com.example.cultuespai.activities.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cultuespai.R
import com.example.cultuespai.activities.MainActivity
import com.example.cultuespai.data.UserData
import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.utils.api.ApiRepository.getUsuariById
import com.example.cultuespai.utils.api.ApiRepository.getUsuaris
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<TextView>(R.id.signUpButton)
        val usernameTextbox = findViewById<EditText>(R.id.usernameTextbox)
        val passwordTextbox = findViewById<EditText>(R.id.passwordTextbox)

        var userList = ArrayList<Usuari>()


        lifecycleScope.launch {
            try {
                val users = getUsuaris()
                userList = users?.toMutableList() as ArrayList<Usuari>

                val user = getUsuariById(5)
                println(user)
            }catch (e: Exception)
            {
                println("API Connexion Error")
            }
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, NewUserActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            if (verifyUser(usernameTextbox.text.toString(), passwordTextbox.text.toString(), userList)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                showIncorrectCredentialsMessage(this)
            }
        }

    }

    private fun verifyUser(userName : String, password : String, userList : ArrayList<Usuari>) : Boolean{
        var userFound = false

        for (user in userList) {
//            if (user.correu == userName && BCrypt.checkpw(password, user.contrasenyaHash)) {
//                userFound = true
//                UserData.userId = user.getId()
//                UserData.userType = user.tipusUsuari
//            }
            if (userName == user.Correu && password == user.Contrasenya) {
                userFound = true
                UserData.userId = user.UsuariID
                UserData.userType = user.TipusUsuari
            }
        }
        return userFound
    }

    private fun showIncorrectCredentialsMessage(context: Context) {
        Toast.makeText(context, context.getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show()
    }
}