package com.rizqinh.locpuskesmas.data.mapper

import com.rizqinh.locpuskesmas.data.local.PuskesmasEntity
import com.rizqinh.locpuskesmas.data.remote.dto.PuskesmasDto
import com.rizqinh.locpuskesmas.domain.model.Puskesmas

fun PuskesmasDto.toEntity(): PuskesmasEntity {
    return PuskesmasEntity(
        id = id,
        name = name,
        kecamatan = kecamatan,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone,
        operationalHours = operationalHours,
        is24Hours = is24Hours
    )
}

fun PuskesmasEntity.toDomain(): Puskesmas {
    return Puskesmas(
        id = id,
        name = name,
        kecamatan = kecamatan,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone,
        operationalHours = operationalHours,
        is24Hours = is24Hours
    )
}