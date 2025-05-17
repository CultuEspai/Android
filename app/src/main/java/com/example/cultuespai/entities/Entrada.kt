package com.example.cultuespai.entities

import java.io.Serializable

class Entrada(
    val EntradaID: Int,
    val UsuariID: Int,
    val EsdevenimentID: Int,
    val Quantitat: Int,
    val NumeroButaca: String? = null
) : Serializable {
}