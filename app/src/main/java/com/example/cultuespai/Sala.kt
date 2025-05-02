package com.example.cultuespai

class Sala(
    val salaID: Int,
    val nom: String,
    val metresQuadrats: Int = 0,
    val aforament: Int?,
    val cadiresFixes: Boolean = false,
    val descripcio: String?,
    val butacaMax: String?
) {
}