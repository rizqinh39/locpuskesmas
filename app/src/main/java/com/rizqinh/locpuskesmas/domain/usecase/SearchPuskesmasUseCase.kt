package com.rizqinh.locpuskesmas.domain.usecase

import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import javax.inject.Inject

class SearchPuskesmasUseCase @Inject constructor() {
    operator fun invoke(
        list: List<Puskesmas>,
        query: String,
        only24Hours: Boolean
    ): List<Puskesmas> {
        return list.filter { puskesmas ->
            val matchesQuery = puskesmas.name.contains(query, ignoreCase = true) ||
                               puskesmas.kecamatan.contains(query, ignoreCase = true)
            val matches24Hours = if (only24Hours) puskesmas.is24Hours else true
            matchesQuery && matches24Hours
        }
    }
}