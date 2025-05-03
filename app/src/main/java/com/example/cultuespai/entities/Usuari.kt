package com.example.cultuespai.entities

class Usuari(
    val usuariID: Int,
    val nom: String,
    val correu: String,
    val contrasenya: String,
    val tipusUsuari: String,
    val actiu: Boolean,
    val contrasenyaHash: String
) {
}