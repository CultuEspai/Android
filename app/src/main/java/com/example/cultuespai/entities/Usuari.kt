package com.example.cultuespai.entities

class Usuari(
    var usuariID: Int,
    var nom: String,
    var correu: String,
    var contrasenya: String,
    var tipusUsuari: String,
    var actiu: Boolean,
    var contrasenyaHash: String
) {
}