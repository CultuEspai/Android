package com.example.cultuespai.entities

import java.io.Serializable

class Entrada(
    val entradaID: Int,
    val usuariID: Int,
    val esdevenimentID: Int,
    val quantitat: Int,
    val numeroButaca: String? = null
) : Serializable {
}