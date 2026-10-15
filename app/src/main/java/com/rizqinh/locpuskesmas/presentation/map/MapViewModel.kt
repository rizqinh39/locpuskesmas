package com.rizqinh.locpuskesmas.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizqinh.locpuskesmas.core.network.Resource
import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import com.rizqinh.locpuskesmas.domain.usecase.GetPuskesmasListUseCase
import com.rizqinh.locpuskesmas.domain.usecase.SearchPuskesmasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getPuskesmasListUseCase: GetPuskesmasListUseCase,
    private val searchPuskesmasUseCase: SearchPuskesmasUseCase
) : ViewModel() {

    // StateFlow menggantikan LiveData. Ini lebih modern, aman untuk Kotlin Coroutines, dan selalu memiliki state awal.
    // _uiState adalah mutable (bisa diubah dari dalam ViewModel).
    private val _uiState = MutableStateFlow(MapUiState())
    // uiState adalah immutable (hanya bisa dibaca dari UI / Activity / Compose). State Hoisting concept.
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        fetchPuskesmas()
    }

    private fun fetchPuskesmas() {
        getPuskesmasListUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Resource.Success -> {
                    val list = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            puskesmasList = list,
                            // Langsung filter data setiap kali data baru tiba
                            filteredList = searchPuskesmasUseCase(
                                list = list,
                                query = currentState.searchQuery,
                                only24Hours = currentState.only24HoursFilter
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { 
                        it.copy(isLoading = false, errorMessage = result.message) 
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    // Dipanggil saat teks di SearchBar berubah
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    // Dipanggil saat chip "24 Jam / UGD" ditekan
    fun onFilter24HoursToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(only24HoursFilter = isEnabled) }
        applyFilters()
    }

    // Dipanggil saat marker map ditekan
    fun onPuskesmasSelected(puskesmas: Puskesmas?) {
        _uiState.update { it.copy(selectedPuskesmas = puskesmas) }
    }
    
    // Membersihkan error message setelah ditampilkan
    fun onErrorDismissed() {
         _uiState.update { it.copy(errorMessage = null) }
    }

    // Menerapkan logika search dan filter, dan meng-update filteredList
    private fun applyFilters() {
        val state = _uiState.value
        val filtered = searchPuskesmasUseCase(
            list = state.puskesmasList,
            query = state.searchQuery,
            only24Hours = state.only24HoursFilter
        )
        _uiState.update { it.copy(filteredList = filtered) }
    }
}