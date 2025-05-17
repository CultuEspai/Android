package com.example.cultuespai.entities

import java.io.Serializable

class Sala(
    val SalaID: Int,
    val Nom: String,
    val MetresQuadrats: Int = 0,
    val Aforament: Int?,
    val CadiresFixes: Boolean = false,
    val Descripcio: String?,
    val ButacaMax: String?
) : Serializable {
}