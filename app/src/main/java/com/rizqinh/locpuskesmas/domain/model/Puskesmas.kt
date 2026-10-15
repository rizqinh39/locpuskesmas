package com.rizqinh.locpuskesmas.domain.model

data class Puskesmas(
    val id: String,
    val name: String,
    val kecamatan: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val operationalHours: String,
    val is24Hours: Boolean
)