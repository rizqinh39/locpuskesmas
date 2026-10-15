package com.rizqinh.locpuskesmas.data.remote

import com.rizqinh.locpuskesmas.data.remote.dto.PuskesmasDto
import retrofit2.http.GET

interface PuskesmasApiService {
    @GET("puskesmas_depok.json")
    suspend fun getPuskesmasList(): List<PuskesmasDto>
}