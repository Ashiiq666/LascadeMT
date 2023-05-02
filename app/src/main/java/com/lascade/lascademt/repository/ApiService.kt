package com.lascade.lascademt.repository

import com.lascade.lascademt.data.model.GalaxiesResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("test/list")
    fun getGalaxies(): Call<GalaxiesResponse>
}