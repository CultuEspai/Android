package com.example.cultuespai.entities

import java.io.Serializable
import java.util.Date

class Esdeveniment(
    val esdevenimentID: Int,
    val nom: String,
    val descripcio: String?,
    val salaID: Int,
    val dataInici: Date,
    val dataFi: Date,
    val aforament: Int,
    val organitzadorID: Int,
    val estat: String,
    val dePagament: Boolean,
    val preu: Int?,
    val entradesDisp: Int
) : Serializable {
}