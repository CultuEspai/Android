package com.example.cultuespai.entities

import java.io.Serializable

class Usuari(
    var UsuariID: Int,
    var Nom: String,
    var Correu: String,
    var Contrasenya: String,
    var TipusUsuari: String,
    var Actiu: Boolean,
    var ContrasenyaHash: String
) : Serializable {
}