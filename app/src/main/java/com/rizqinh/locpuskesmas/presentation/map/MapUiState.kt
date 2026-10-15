package com.rizqinh.locpuskesmas.presentation.map

import com.rizqinh.locpuskesmas.domain.model.Puskesmas

data class MapUiState(
    val isLoading: Boolean = false,
    val puskesmasList: List<Puskesmas> = emptyList(), // Original data (Single Source of Truth)
    val filteredList: List<Puskesmas> = emptyList(),  // Data that is actually shown on the map
    val searchQuery: String = "",
    val only24HoursFilter: Boolean = false,
    val selectedPuskesmas: Puskesmas? = null,
    val errorMessage: String? = null
)