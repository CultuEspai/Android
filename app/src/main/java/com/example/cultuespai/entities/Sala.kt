package com.example.cultuespai.entities

import java.io.Serializable

class Sala(
    val salaID: Int,
    val nom: String,
    val metresQuadrats: Int = 0,
    val aforament: Int?,
    val cadiresFixes: Boolean = false,
    val descripcio: String?,
    val butacaMax: String?
) : Serializable {
}