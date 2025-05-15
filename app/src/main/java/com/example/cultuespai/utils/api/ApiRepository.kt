package com.example.cultuespai.utils.api

import com.example.cultuespai.entities.Usuari
import com.example.cultuespai.entities.Entrada
import com.example.cultuespai.entities.Esdeveniment
import com.example.cultuespai.entities.Sala
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiRepository {
    private val api = ApiClient.apiService

    // ---------- USUARIS ----------

    suspend fun getUsuaris(): List<Usuari>? = withContext(Dispatchers.IO) {
        api.getUsuaris().body()
    }

    suspend fun getUsuariById(id: Int): Usuari? = withContext(Dispatchers.IO) {
        api.getUsuariById(id).body()
    }

    suspend fun postUsuari(usuari: Usuari): Usuari? = withContext(Dispatchers.IO) {
        api.postUsuari(usuari).body()
    }

    suspend fun putUsuari(id: Int, usuari: Usuari): Boolean = withContext(Dispatchers.IO) {
        api.putUsuari(id, usuari).isSuccessful
    }

    suspend fun deleteUsuari(id: Int): Usuari? = withContext(Dispatchers.IO) {
        api.deleteUsuari(id).body()
    }

    // ---------- ENTRADES ----------

    suspend fun getEntradesByUser(userId: Int): List<Entrada>? = withContext(Dispatchers.IO) {
        api.getEntradesByUser(userId).body()
    }

    suspend fun postEntrada(entrada: Entrada): Entrada? = withContext(Dispatchers.IO) {
        api.postEntrada(entrada).body()
    }

    suspend fun deleteEntrada(id: Int): Entrada? = withContext(Dispatchers.IO) {
        api.deleteEntrada(id).body()
    }

    // ---------- ESDEVENIMENTS ----------

    suspend fun getEsdeveniments(): List<Esdeveniment>? = withContext(Dispatchers.IO) {
        api.getEsdeveniments().body()
    }

    suspend fun postEsdeveniment(esdeveniment: Esdeveniment): Esdeveniment? = withContext(Dispatchers.IO) {
        api.postEsdeveniment(esdeveniment).body()
    }

    suspend fun putEsdeveniment(id: Int, esdeveniment: Esdeveniment): Boolean = withContext(Dispatchers.IO) {
        api.putEsdeveniment(id, esdeveniment).isSuccessful
    }

    suspend fun deleteEsdeveniment(id: Int): Esdeveniment? = withContext(Dispatchers.IO) {
        api.deleteEsdeveniment(id).body()
    }

    // ---------- SALES ----------

    suspend fun getSales(): List<Sala>? = withContext(Dispatchers.IO) {
        api.getSales().body()
    }

    suspend fun getSalaById(id: Int): Sala? = withContext(Dispatchers.IO) {
        api.getSalaById(id).body()
    }
}