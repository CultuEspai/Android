package com.example.cultuespai.utils.api


import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import com.example.cultuespai.entities.Usuari

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ---------------- USUARIS ----------------

    @GET("Usuaris")
    suspend fun getUsuaris(): Response<List<Usuari>>

    @GET("Usuaris/{id}")
    suspend fun getUsuariById(@Path("id") id: Int): Response<Usuari>

    @POST("Usuaris")
    suspend fun postUsuari(@Body usuari: Usuari): Response<Usuari>

    @PUT("Usuaris/{id}")
    suspend fun putUsuari(@Path("id") id: Int, @Body usuari: Usuari): Response<Void>

    @DELETE("Usuaris/{id}")
    suspend fun deleteUsuari(@Path("id") id: Int): Response<Usuari>

    // ---------------- ENTRADES ----------------

    @GET("Entrades/user/{id}")
    suspend fun getEntradesByUser(@Path("id") userId: Int): Response<List<Entrada>>

    @GET("Entrades/event/{id}")
    suspend fun getEntradesByEvent(@Path("id") eventId: Int): Response<List<Entrada>>

    @POST("Entrades")
    suspend fun postEntrada(@Body entrada: Entrada): Response<Entrada>

    @DELETE("Entrades/{id}")
    suspend fun deleteEntrada(@Path("id") id: Int): Response<Entrada>

    // ---------------- ESDEVENIMENTS ----------------

    @GET("Esdeveniments")
    suspend fun getEsdeveniments(): Response<List<Esdeveniment>>

    @POST("Esdeveniments")
    suspend fun postEsdeveniment(@Body esdeveniment: Esdeveniment): Response<Esdeveniment>

    @PUT("Esdeveniments/{id}")
    suspend fun putEsdeveniment(@Path("id") id: Int, @Body esdeveniment: Esdeveniment): Response<Void>

    @DELETE("Esdeveniments/{id}")
    suspend fun deleteEsdeveniment(@Path("id") id: Int): Response<Esdeveniment>

    // ---------------- SALES ----------------

    @GET("Sales")
    suspend fun getSales(): Response<List<Sala>>

    @GET("Sales/{id}")
    suspend fun getSalaById(@Path("id") id: Int): Response<Sala>
}