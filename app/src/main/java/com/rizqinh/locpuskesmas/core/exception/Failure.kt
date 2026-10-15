package com.rizqinh.locpuskesmas.core.exception

sealed class Failure(message: String) : Exception(message) {
    class NetworkConnection(message: String = "Tidak ada koneksi internet") : Failure(message)
    class ServerError(message: String = "Terjadi kesalahan pada server") : Failure(message)
    class DatabaseError(message: String = "Terjadi kesalahan pada database lokal") : Failure(message)
    class UnknownError(message: String = "Terjadi kesalahan yang tidak diketahui") : Failure(message)
}