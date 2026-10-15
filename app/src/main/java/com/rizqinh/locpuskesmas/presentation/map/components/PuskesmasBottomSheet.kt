package com.rizqinh.locpuskesmas.presentation.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rizqinh.locpuskesmas.core.util.dialPhone
import com.rizqinh.locpuskesmas.core.util.openGoogleMapsDirection
import com.rizqinh.locpuskesmas.domain.model.Puskesmas
import com.rizqinh.locpuskesmas.presentation.theme.StatusClosed
import com.rizqinh.locpuskesmas.presentation.theme.StatusClosedBg
import com.rizqinh.locpuskesmas.presentation.theme.StatusOpen
import com.rizqinh.locpuskesmas.presentation.theme.StatusOpenBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuskesmasBottomSheet(
    puskesmas: Puskesmas,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    // State management untuk bottom sheet, di XML ini dikontrol manual via BottomSheetBehavior
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Badge Kecamatan (Box setara FrameLayout)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Kec. ${puskesmas.kecamatan}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = puskesmas.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row setara LinearLayout(horizontal)
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = puskesmas.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Operasional Badge
            val statusColor = if (puskesmas.is24Hours) StatusOpen else StatusClosed
            val statusBgColor = if (puskesmas.is24Hours) StatusOpenBg else StatusClosedBg
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusBgColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = puskesmas.operationalHours,
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // CTA Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { context.dialPhone(puskesmas.phone) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Hubungi")
                    Spacer(Modifier.width(8.dp))
                    Text("Hubungi")
                }

                Button(
                    onClick = { context.openGoogleMapsDirection(puskesmas.latitude, puskesmas.longitude) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Rute")
                    Spacer(Modifier.width(8.dp))
                    Text("Rute")
                }
            }
        }
    }
}