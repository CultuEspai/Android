package com.example.cultuespai.entities

import java.io.Serializable
import java.util.Date

class Esdeveniment(
    val EsdevenimentID: Int,
    val Nom: String,
    val Descripcio: String?,
    val SalaID: Int,
    val DataInici: Date,
    val DataFi: Date,
    val Aforament: Int,
    val OrganitzadorID: Int,
    val Estat: String,
    val DePagament: Boolean,
    val Preu: Int?,
    val EntradesDisp: Int
) : Serializable {
}