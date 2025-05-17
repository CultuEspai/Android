package com.example.cultuespai.entities

import java.io.Serializable
import java.util.Date

class CaracteristicaSala(
    val CaracteristicaID: Int,
    val SalaID: Int,
    val CaracteristicaNom: String,
    val CaracteristicaValor: String,
    val DataModificacio: Date
) : Serializable {
}