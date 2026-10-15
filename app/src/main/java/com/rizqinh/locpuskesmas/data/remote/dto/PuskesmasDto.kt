package com.rizqinh.locpuskesmas.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PuskesmasDto(
    val id: String,
    val name: String,
    val kecamatan: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    @SerializedName("operational_hours")
    val operationalHours: String,
    @SerializedName("is_24_hours")
    val is24Hours: Boolean
)