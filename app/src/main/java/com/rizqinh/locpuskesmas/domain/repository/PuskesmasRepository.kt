package com.rizqinh.locpuskesmas.domain.repository

import com.rizqinh.locpuskesmas.core.network.Resource
import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import kotlinx.coroutines.flow.Flow

interface PuskesmasRepository {
    fun getPuskesmasList(): Flow<Resource<List<Puskesmas>>>
}