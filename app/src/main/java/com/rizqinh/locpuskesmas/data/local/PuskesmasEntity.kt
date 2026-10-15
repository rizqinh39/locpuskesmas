package com.rizqinh.locpuskesmas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_puskesmas")
data class PuskesmasEntity(
    @PrimaryKey
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