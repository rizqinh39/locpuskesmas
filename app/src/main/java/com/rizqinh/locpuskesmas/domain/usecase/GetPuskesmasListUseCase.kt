package com.rizqinh.locpuskesmas.domain.usecase

import com.rizqinh.locpuskesmas.core.network.Resource
import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import com.rizqinh.locpuskesmas.domain.repository.PuskesmasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPuskesmasListUseCase @Inject constructor(
    private val repository: PuskesmasRepository
) {
    operator fun invoke(): Flow<Resource<List<Puskesmas>>> {
        return repository.getPuskesmasList()
    }
}