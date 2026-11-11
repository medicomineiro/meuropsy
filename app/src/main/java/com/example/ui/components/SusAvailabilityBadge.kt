package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrivateBlue
import com.example.ui.theme.SusGreen
import com.example.ui.theme.WarningOrange

@Composable
fun SusAvailabilityBadge(
    availabilityText: String,
    modifier: Modifier = Modifier
) {
    val (badgeBg, badgeBorder, badgeText, badgeIcon) = when {
        availabilityText.contains("Atenção Básica", ignoreCase = true) || availabilityText.contains("RENAME", ignoreCase = true) -> {
            Quad(
                Color(0x2200C853),
                SusGreen,
                "SUS: RENAME (Atenção Básica)",
                Icons.Default.Public
            )
        }
        availabilityText.contains("Especializado", ignoreCase = true) || availabilityText.contains("Alto Custo", ignoreCase = true) -> {
            Quad(
                Color(0x22FFB300),
                WarningOrange,
                "SUS: Alto Custo (PCDT)",
                Icons.Default.Bookmark
            )
        }
        else -> {
            Quad(
                Color(0x222979FF),
                PrivateBlue,
                "Saúde Suplementar / Desembolso",
                Icons.Default.LocalPharmacy
            )
        }
    }

    Box(
        modifier = modifier
            .background(badgeBg, RoundedCornerShape(6.dp))
            .border(1.dp, badgeBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = badgeIcon,
                contentDescription = null,
                tint = badgeBorder,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = badgeText,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
