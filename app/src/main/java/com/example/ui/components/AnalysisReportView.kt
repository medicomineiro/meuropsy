package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlgorithmStep
import com.example.model.ConsensusAnalysisResult
import com.example.model.DrugOption
import com.example.model.NeuroPerspective
import com.example.model.PsychPerspective
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
fun AnalysisReportView(
    result: ConsensusAnalysisResult,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableIntStateOf(0) }
    val sectionTitles = listOf(
        "Síntese Bifocal",
        "1. Neuroanatomia",
        "2. Debate Neuro vs Psi",
        "3. Farmacologia Brasil",
        "4. Algoritmo Conduta",
        "5. Clinical Pearls"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBg)
    ) {
        // Report Header
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
                        .size(36.dp)
                        .background(Color(0x2200E5FF), CircleShape)
                        .border(1.dp, CyanPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Biotech,
                        contentDescription = "Consensus Icon",
                        tint = CyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "PARECER BIFOCAL DO CONSENSO",
                        color = CyanPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Neurologia & Psiquiatria Integradas",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_report_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fechar relatório",
                    tint = TextSecondary
                )
            }
        }

        // Section Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedSection,
            containerColor = DarkSurfaceVariant,
            contentColor = CyanPrimary,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                    color = CyanPrimary,
                    height = 3.dp
                )
            }
        ) {
            sectionTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSection == index,
                    onClick = { selectedSection = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedSection == index) CyanPrimary else TextSecondary
                        )
                    }
                )
            }
        }

        // Content Body
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedSection) {
                0 -> {
                    // Summary View
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF23354D), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SectionHeader(
                                    title = "SÍNTESE DO CASO CLÍNICO",
                                    icon = Icons.Default.Description,
                                    tint = CyanPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = result.caseSummary,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    item {
                        NeuroanatomyCard(result.neuroanatomicalCorrelation)
                    }

                    item {
                        DebatePreviewCard(result.neuroPerspective, result.psychPerspective)
                    }

                    item {
                        ClinicalPearlsCard(result.clinicalPearls)
                    }
                }

                1 -> {
                    // 1. Correlação Neuroanatômica & Fisiopatológica
                    item {
                        NeuroanatomyCard(result.neuroanatomicalCorrelation, isExpanded = true)
                    }
                }

                2 -> {
                    // 2. Painel de Debate / Consenso (Neurologia vs Psiquiatria)
                    item {
                        NeuroPerspectiveCard(result.neuroPerspective)
                    }
                    item {
                        PsychPerspectiveCard(result.psychPerspective)
                    }
                }

                3 -> {
                    // 3. Análise Terapêutica Comparativa
                    item {
                        Text(
                            text = "TABELA FARMACOLÓGICA COMPARATIVA (BRASIL)",
                            color = CyanPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Perfil metabólico (CYP450), receptorologia e disponibilidade no SUS (RENAME) / Saúde Suplementar.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    items(result.therapeuticComparison) { drug ->
                        DrugCard(drug)
                    }
                }

                4 -> {
                    // 4. Algoritmo de Conduta Prática
                    item {
                        Text(
                            text = "ALGORITMO DE CONDUTA PRÁTICA SEQUENCIAL",
                            color = CyanPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    items(result.practicalAlgorithm) { step ->
                        AlgorithmStepCard(step)
                    }
                }

                5 -> {
                    // 5. Clinical Pearls
                    item {
                        ClinicalPearlsCard(result.clinicalPearls, isExpanded = true)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: ImageVector,
    tint: Color
) {
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
            color = tint,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun NeuroanatomyCard(
    correlationText: String,
    isExpanded: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF23354D), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(
                title = "1. CORRELAÇÃO NEUROANATÔMICA & FISIOPATOLÓGICA",
                icon = Icons.Default.Psychology,
                tint = CyanPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D1726), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = correlationText,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 21.sp
                )
            }
        }
    }
}

@Composable
fun DebatePreviewCard(
    neuro: NeuroPerspective,
    psych: PsychPerspective
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF23354D), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(
                title = "2. PAINEL DE DEBATE / CONSENSO",
                icon = Icons.Default.AltRoute,
                tint = TealSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Neuro summary mini block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0E1F29), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Visão Neuroclínica:",
                        color = TealSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = neuro.summary,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Psych summary mini block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1729), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Visão Psiquiátrica:",
                        color = AmberTertiary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = psych.summary,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NeuroPerspectiveCard(neuro: NeuroPerspective) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF163E48), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(TealSecondary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PERSPECTIVA NEUROCLÍNICA",
                    color = TealSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Causas Orgânicas & Mimetizadores:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            neuro.organicEtiologies.forEach { etiology ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "• ", color = TealSecondary, fontSize = 13.sp)
                    Text(text = etiology, color = TextPrimary, fontSize = 13.sp, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Padrão de Lesão / Neuroimagem:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = neuro.neuroimagingCorrelations,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            // Reversible causes exclusion warning block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x2200E5FF), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0x5500E5FF), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = CyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Screening de Causas Reversíveis (B12, TSH, Sífilis) & Biomarcadores:",
                            color = CyanPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    neuro.biomarkersAndScreening.forEach { item ->
                        Text(
                            text = "→ $item",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Síntese da Conduta Neurológica: ${neuro.summary}",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun PsychPerspectiveCard(psych: PsychPerspective) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF453055), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(AmberTertiary, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PERSPECTIVA PSIQUIÁTRICA",
                    color = AmberTertiary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Psicopatologia Pura:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = psych.psychopathology,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Curso Longitudinal & Estadiamento:",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${psych.longitudinalCourse} — ${psych.stagingAndFunctionalImpact}",
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Síntese Psiquiátrica: ${psych.summary}",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun DrugCard(drug: DrugOption) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF22364E), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = drug.drugName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = drug.drugClass,
                        color = CyanPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                SusAvailabilityBadge(availabilityText = drug.availabilityBrazil)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Profile info box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0B1422), RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = "Perfil Farmacocinético & Receptores:",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = drug.pharmacokineticProfile,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pros and Cons
            Row(modifier = Modifier.fillMaxWidth()) {
                // Pros column
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00C853),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Eficácia / Vantagens:",
                            color = Color(0xFF00C853),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    drug.pros.forEach { pro ->
                        Text(
                            text = "• $pro",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Cons column
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = RedAlert,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Riscos / Iatrogenias:",
                            color = RedAlert,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    drug.cons.forEach { con ->
                        Text(
                            text = "• $con",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            if (drug.susDetails.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Regulatório / PCDT: ${drug.susDetails}",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun AlgorithmStepCard(step: AlgorithmStep) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF22364E), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0x2200E5FF), CircleShape)
                    .border(1.dp, CyanPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    color = CyanPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.stepTitle,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step.actionPlan,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                if (step.monitoringMetrics.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF0F1A29), RoundedCornerShape(6.dp))
                            .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Métrica / Escala (LaTeX): ",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = step.monitoringMetrics,
                                color = CyanPrimary,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClinicalPearlsCard(
    pearls: List<String>,
    isExpanded: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF14241B)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF00C853), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(
                title = "5. CLINICAL PEARLS (CONDUTA PRÁTICA)",
                icon = Icons.Default.Lightbulb,
                tint = Color(0xFF00E676)
            )
            Spacer(modifier = Modifier.height(10.dp))
            pearls.forEachIndexed { idx, pearl ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color(0x3300E676), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${idx + 1}",
                            color = Color(0xFF00E676),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pearl,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
