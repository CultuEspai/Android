package com.example.cultuespai.entities

import java.io.Serializable
import java.util.Date

class CaracteristicaSala(
    val caracteristicaID: Int,
    val salaID: Int,
    val caracteristicaNom: String,
    val caracteristicaValor: String,
    val dataModificacio: Date
) : Serializable {
}