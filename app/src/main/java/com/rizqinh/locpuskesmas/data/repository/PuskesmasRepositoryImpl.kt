package com.rizqinh.locpuskesmas.data.repository

import com.rizqinh.locpuskesmas.core.network.Resource
import com.rizqinh.locpuskesmas.data.local.PuskesmasDao
import com.rizqinh.locpuskesmas.data.mapper.toDomain
import com.rizqinh.locpuskesmas.data.mapper.toEntity
import com.rizqinh.locpuskesmas.data.remote.PuskesmasApiService
import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import com.rizqinh.locpuskesmas.domain.repository.PuskesmasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PuskesmasRepositoryImpl @Inject constructor(
    private val apiService: PuskesmasApiService,
    private val dao: PuskesmasDao
) : PuskesmasRepository {

    override fun getPuskesmasList(): Flow<Resource<List<Puskesmas>>> = flow {
        emit(Resource.Loading)

        // 1. Ambil data dari cache lokal (Room) terlebih dahulu
        val cachedList = dao.getAllPuskesmas().first().map { it.toDomain() }
        
        // 2. Tampilkan data dari cache lokal (jika ada) sambil mengambil data baru dari API
        if (cachedList.isNotEmpty()) {
            emit(Resource.Success(cachedList))
        }

        try {
            // 3. Ambil data terbaru dari API
            val remotePuskesmas = apiService.getPuskesmasList()
            
            // 4. Update data ke Room (Simpan sebagai single source of truth)
            dao.clearAll()
            dao.insertPuskesmasList(remotePuskesmas.map { it.toEntity() })
            
            // 5. Emit data terbaru yang diambil langsung dari database
            val updatedList = dao.getAllPuskesmas().first().map { it.toDomain() }
            emit(Resource.Success(updatedList))
            
        } catch (e: IOException) {
            // 6. Jika gagal (misal tidak ada internet), kembalikan error, 
            // tetapi UI tetap menggunakan cached data yang dikirim sebelumnya.
            emit(Resource.Error("Terjadi kesalahan jaringan atau API", e))
        } catch (e: Exception) {
            emit(Resource.Error("Terjadi kesalahan sistem", e))
        }
    }
}