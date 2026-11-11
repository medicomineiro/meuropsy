package com.example.ui.components

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PresetCasesRepository
import com.example.model.PresetClinicalCase
import com.example.ui.AnalysisUiState
import com.example.ui.NeuroPsychViewModel
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.RedAlert
import com.example.ui.theme.TealSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ClinicalInputScreen(
    viewModel: NeuroPsychViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clinicalNotes by viewModel.clinicalNotes.collectAsState()
    val neuroimagingFindings by viewModel.neuroimagingFindings.collectAsState()
    val therapeuticScheme by viewModel.currentTherapeuticScheme.collectAsState()
    val attachedImage by viewModel.attachedImage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val history by viewModel.savedConsensusHistory.collectAsState()

    var showHistoryDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                viewModel.setAttachedImage(bitmap)
            } catch (e: Exception) {
                // Keep safe
            }
        }
    }

    if (uiState is AnalysisUiState.Success) {
        val successResult = (uiState as AnalysisUiState.Success).result
        AnalysisReportView(
            result = successResult,
            onClose = { viewModel.resetToInput() },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // App Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0x2200E5FF), CircleShape)
                        .border(1.5.dp, CyanPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Logo",
                        tint = CyanPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "NEUROPSYCH CONSENSUS",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Clinical Decision Support Engine • Brasil (SUS)",
                        color = CyanPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row {
                if (history.isNotEmpty()) {
                    IconButton(
                        onClick = { showHistoryDialog = true },
                        modifier = Modifier.testTag("history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Histórico",
                            tint = CyanPrimary
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.clearInputs() },
                    modifier = Modifier.testTag("clear_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpar formulário",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Scrollable Input Body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Preset Cases Quick Selector
            Text(
                text = "CASOS CLÍNICOS MODELO (SELEÇÃO RÁPIDA)",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetCasesRepository.presetCases.forEach { preset ->
                    PresetCaseChip(
                        preset = preset,
                        onClick = { viewModel.loadPresetCase(preset) }
                    )
                }
            }

            // Input 1: Clinical notes / Anamnesis
            InputCard(
                title = "1. Fragmentos de Evolução / Anamnese",
                subtitle = "Sintomas afetivos, psicóticos, cognitivos ou motores com marco temporal.",
                icon = Icons.Default.MedicalInformation,
                tint = CyanPrimary
            ) {
                OutlinedTextField(
                    value = clinicalNotes,
                    onValueChange = { viewModel.updateClinicalNotes(it) },
                    placeholder = {
                        Text(
                            text = "Ex: Paciente 68 anos, com anedonia profunda, abulia e lentificação psicomotora há 10 meses. Refratário a dois ISRS em doses máximas...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("input_clinical_notes"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = Color(0xFF23354D),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = CyanPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Input 2: Neuroimaging / Complementary Exams
            InputCard(
                title = "2. Neuroimagem & Exames Complementares",
                subtitle = "Achados de TC / RM (ex: Fazekas, atrofia mesial, núcleos da base) ou biomarcadores.",
                icon = Icons.Default.Biotech,
                tint = TealSecondary
            ) {
                OutlinedTextField(
                    value = neuroimagingFindings,
                    onValueChange = { viewModel.updateNeuroimagingFindings(it) },
                    placeholder = {
                        Text(
                            text = "Ex: RM crânio com leucoencefalopatia confluente em T2/FLAIR (Fazekas grau 3) com infartos lacunares nos núcleos da base...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("input_neuroimaging"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealSecondary,
                        unfocusedBorderColor = Color(0xFF23354D),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = TealSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Attach Neuroimaging Image/Cut
                if (attachedImage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, CyanPrimary, RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            bitmap = attachedImage!!.asImageBitmap(),
                            contentDescription = "Corte de neuroimagem anexado",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { viewModel.setAttachedImage(null) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color(0xAA000000), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remover imagem",
                                tint = RedAlert
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("attach_image_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CyanPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Anexar Imagem / Corte Tomográfico ou RM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Input 3: Current Therapeutic Scheme & Refratariedade
            InputCard(
                title = "3. Hipóteses & Esquema Terapêutico Atual",
                subtitle = "Fármacos em uso, doses, interações a revisar e refratariedade prévia.",
                icon = Icons.Default.Medication,
                tint = AmberTertiary
            ) {
                OutlinedTextField(
                    value = therapeuticScheme,
                    onValueChange = { viewModel.updateTherapeuticScheme(it) },
                    placeholder = {
                        Text(
                            text = "Ex: Em uso de Haloperidol 2.5 mg/dia + Quetiapina 50 mg/noite. Investigar segurança e compatibilidade com SUS.",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("input_therapeutic_scheme"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberTertiary,
                        unfocusedBorderColor = Color(0xFF23354D),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AmberTertiary
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Error display
            if (uiState is AnalysisUiState.Error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x33FF5252), RoundedCornerShape(8.dp))
                        .border(1.dp, RedAlert, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = (uiState as AnalysisUiState.Error).message,
                        color = Color(0xFFFF8A80),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Run Analysis Action Button
            Button(
                onClick = { viewModel.runAnalysis() },
                enabled = uiState !is AnalysisUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("run_analysis_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanPrimary,
                    contentColor = Color(0xFF00363D)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (uiState is AnalysisUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color(0xFF00363D),
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "PROCESSANDO CONSENSO BIFOCAL...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GERAR CONSENSO NEUROPSIQUIÁTRICO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // History Sheet / Dialog
    if (showHistoryDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = "Histórico de Análises da Sessão",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    history.forEachIndexed { idx, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0D1726), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF23354D), RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.showAnalysis(item)
                                    showHistoryDialog = false
                                }
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Parecer #${history.size - idx}",
                                    color = CyanPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.caseSummary.take(80) + "...",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showHistoryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary)
                ) {
                    Text("Fechar", color = Color(0xFF00363D))
                }
            }
        )
    }
}

@Composable
fun InputCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E2E44), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun PresetCaseChip(
    preset: PresetClinicalCase,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(DarkSurface, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF263A52), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = preset.category,
                color = CyanPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = preset.title,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
