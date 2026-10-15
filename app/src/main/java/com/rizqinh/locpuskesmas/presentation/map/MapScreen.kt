package com.rizqinh.locpuskesmas.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rizqinh.locpuskesmas.core.constants.AppConstants
import com.rizqinh.locpuskesmas.presentation.components.SearchTopBar
import com.rizqinh.locpuskesmas.presentation.map.components.PuskesmasBottomSheet
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State untuk izin lokasi
    var isLocationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val depokLatLng = LatLng(AppConstants.DEPOK_LAT, AppConstants.DEPOK_LNG)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(depokLatLng, AppConstants.DEFAULT_ZOOM)
    }

    // Launcher izin lokasi
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        isLocationPermissionGranted = granted
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                15f
                            )
                        )
                    }
                }
            }
        } else {
            Toast.makeText(context, "Izin lokasi tidak diberikan", Toast.LENGTH_SHORT).show()
        }
    }

    // Mengumpulkan state dengan aman memperhatikan lifecycle Android
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Tangani tampilan Error Message
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.onErrorDismissed()
        }
    }

    // Scaffold mengatur layout dasar Material Design
    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Tombol 1: Center Default Map Wilayah Depok
                FloatingActionButton(
                    onClick = { 
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(depokLatLng, AppConstants.DEFAULT_ZOOM)
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Icon(
                        imageVector = Icons.Default.CenterFocusStrong,
                        contentDescription = "Peta Default Depok"
                    )
                }

                // Tombol 2: Ke Current Location User
                FloatingActionButton(
                    onClick = { 
                        if (isLocationPermissionGranted) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    coroutineScope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(location.latitude, location.longitude),
                                                15f
                                            )
                                        )
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Gagal mendapatkan lokasi saat ini. Pastikan GPS aktif.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Lokasi Saya"
                    )
                }
            }
        }
    ) { paddingValues ->
        // Box setara FrameLayout, menumpuk layer map dan layer pencarian
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Google Maps
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    compassEnabled = true
                ),
                properties = MapProperties(
                    isMyLocationEnabled = isLocationPermissionGranted
                )
            ) {
                // Iterasi state data dan pasang marker di map
                uiState.filteredList.forEach { puskesmas ->
                    Marker(
                        state = MarkerState(position = LatLng(puskesmas.latitude, puskesmas.longitude)),
                        title = puskesmas.name,
                        snippet = puskesmas.kecamatan,
                        onClick = {
                            viewModel.onPuskesmasSelected(puskesmas)
                            true
                        }
                    )
                }
            }

            // Top Bar melayang di atas Map
            SearchTopBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
                only24Hours = uiState.only24HoursFilter,
                onFilter24HoursChanged = viewModel::onFilter24HoursToggled,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // Loading Indicator di tengah layar
            if (uiState.isLoading && uiState.puskesmasList.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // Modal Bottom Sheet ditampilkan ketika state selectedPuskesmas != null
    if (uiState.selectedPuskesmas != null) {
        PuskesmasBottomSheet(
            puskesmas = uiState.selectedPuskesmas!!,
            onDismiss = { viewModel.onPuskesmasSelected(null) }
        )
    }
}