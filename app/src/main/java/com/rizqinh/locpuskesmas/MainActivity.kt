package com.rizqinh.locpuskesmas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rizqinh.locpuskesmas.presentation.map.MapScreen
import com.rizqinh.locpuskesmas.presentation.theme.LocPuskesmasTheme
import dagger.hilt.android.AndroidEntryPoint

// AndroidEntryPoint menandakan bahwa class ini bisa menggunakan DI Dagger-Hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Memaksimalkan layar ke belakang status bar & navigation bar
        enableEdgeToEdge()
        
        setContent {
            LocPuskesmasTheme {
                // Surface setara background container di XML (seperti CoordinatorLayout)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Memanggil Navigasi awal
                    MapScreen()
                }
            }
        }
    }
}