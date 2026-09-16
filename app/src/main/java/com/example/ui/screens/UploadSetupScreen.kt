package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.service.SampleData
import com.example.ui.StudioUiState
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun UploadSetupScreen(
    state: StudioUiState,
    onSelectSample: (VideoMetadata) -> Unit,
    onVideoUploaded: (String, String, Long, Float, String) -> Unit,
    onSourceLanguageSelected: (Language) -> Unit,
    onTargetLanguageSelected: (Language) -> Unit,
    onToggleAutoDetect: (Boolean) -> Unit,
    onLipSyncQualitySelected: (LipSyncQuality) -> Unit,
    onVoiceProfileSaved: (VoiceProfile) -> Unit,
    onStartPipeline: () -> Unit
) {
    var showSourceLanguagePicker by remember { mutableStateOf(false) }
    var showTargetLanguagePicker by remember { mutableStateOf(false) }
    var showVoiceCloneDialog by remember { mutableStateOf(false) }
    var showLipSyncDialog by remember { mutableStateOf(false) }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment ?: "meu_video_upload.mp4"
            onVideoUploaded(fileName, "MP4", 42_500_000L, 24.5f, uri.toString())
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            Surface(
                color = DarkSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "TRADUÇÃO E DUBLAGEM",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (state.autoDetectLanguage) "Auto ✨" else state.sourceLanguage.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AccentPurpleGlow
                                )
                                Text(" → ", color = TextMuted)
                                Text(
                                    state.targetLanguage.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AccentCyanGlow
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentEmerald.copy(alpha = 0.15f)
                        ) {
                            Text(
                                "QA Score: 98%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentEmerald,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onStartPipeline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("translate_video_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Traduzir e Dublar Vídeo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(AccentPurple, AccentCyan))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("DubStudio AI", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = TextPrimary)
                        Text("Localização, Dublagem e Lip-Sync", fontSize = 11.sp, color = TextSecondary)
                    }
                }

                IconButton(
                    onClick = { showVoiceCloneDialog = true },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceElevated)
                        .testTag("open_voice_profile_header")
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Minha Voz", tint = AccentPurpleGlow)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ETAPA 1: Upload Card
            Text(
                "ETAPA 1 — VÍDEO PRINCIPAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AccentCyan,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_drop_zone")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Video Preview Header Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(DarkSurfaceElevated, Color(0xFF0F0E1E))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(AccentPurple.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = AccentPurpleGlow,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                state.videoMetadata.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            Text(
                                "Preview carregado e pronto para análise",
                                fontSize = 11.sp,
                                color = AccentEmerald
                            )
                        }

                        // Duration pill in top right
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                state.videoMetadata.formattedDuration,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Video Technical Specifications Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        VideoSpecBadge("Duração", state.videoMetadata.formattedDuration)
                        VideoSpecBadge("Resolução", state.videoMetadata.resolution.substringBefore(" "))
                        VideoSpecBadge("Formato", state.videoMetadata.format)
                        VideoSpecBadge("Tamanho", state.videoMetadata.formattedSize)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { videoPickerLauncher.launch("video/*") },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("upload_file_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceHighlight),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Fazer Upload", fontSize = 13.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Formatos aceitos: MP4, MOV, WebM, AVI • Separação de áudio automática",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sample Videos Quick Picker
            Text("OU SELECIONE UMA AMOSTRA:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SampleData.sampleVideos) { sample ->
                    val isSelected = state.videoMetadata.sampleId == sample.sampleId
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AccentPurple.copy(alpha = 0.2f) else DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) AccentPurple else DarkBorder
                        ),
                        modifier = Modifier
                            .clickable { onSelectSample(sample) }
                            .testTag("sample_item_${sample.sampleId}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (isSelected) Icons.Default.CheckCircle else Icons.Default.Videocam,
                                contentDescription = null,
                                tint = if (isSelected) AccentPurpleGlow else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    sample.title.take(22) + "...",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AccentPurpleGlow else TextPrimary
                                )
                                Text(
                                    "${sample.formattedDuration} • ${sample.format}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ETAPA 2 & 3: Language Selectors
            Text(
                "ETAPA 2 & 3 — IDIOMAS & LOCALIZAÇÃO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AccentPurpleGlow,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Box 1: Source Language
                    Text("Idioma do vídeo", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSourceLanguagePicker = true }
                            .testTag("source_language_selector")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (state.autoDetectLanguage) "✨" else state.sourceLanguage.flag,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        if (state.autoDetectLanguage) "Detectar idioma automaticamente" else state.sourceLanguage.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        if (state.autoDetectLanguage) "A IA reconhece nativamente no áudio" else "Original do locutor",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Swap / Arrow Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceHighlight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = "Para", tint = AccentCyan, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Box 2: Target Language
                    Text("Traduzir para", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTargetLanguagePicker = true }
                            .testTag("target_language_selector")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(state.targetLanguage.flag, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        state.targetLanguage.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Text("Nova fala dublada no vídeo", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick popular combinations
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        QuickLangChip("🇧🇷 PT → 🇺🇸 EN") {
                            onSourceLanguageSelected(LanguageCatalog.findByCode("pt"))
                            onTargetLanguageSelected(LanguageCatalog.findByCode("en"))
                        }
                        QuickLangChip("🇧🇷 PT → 🇪🇸 ES") {
                            onSourceLanguageSelected(LanguageCatalog.findByCode("pt"))
                            onTargetLanguageSelected(LanguageCatalog.findByCode("es"))
                        }
                        QuickLangChip("🇺🇸 EN → 🇧🇷 PT") {
                            onSourceLanguageSelected(LanguageCatalog.findByCode("en"))
                            onTargetLanguageSelected(LanguageCatalog.findByCode("pt"))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ETAPA 7 & 10: Voice Preservation & Lip Sync Controls
            Text(
                "CONFIGURAÇÕES AVANÇADAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AccentAmber,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Minha Voz Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showVoiceCloneDialog = true }
                    .testTag("open_voice_profile_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AccentPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = AccentPurpleGlow)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Minha Voz", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                if (state.voiceProfile.hasConsent) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = AccentEmerald.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            "AUTORIZADA",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentEmerald,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                "${state.voiceProfile.timbre} • ${state.voiceProfile.sampleDurationSec}s de amostra",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lip-Sync Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLipSyncDialog = true }
                    .testTag("open_lip_sync_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AccentCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Face, contentDescription = null, tint = AccentCyan)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Sincronização Labial", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AccentCyan.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        state.lipSyncQuality.labelPt,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentCyanGlow,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                state.lipSyncQuality.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialogs
    if (showSourceLanguagePicker) {
        LanguagePickerDialog(
            title = "Selecionar Idioma do Vídeo",
            currentSelection = state.sourceLanguage,
            isSourceMode = true,
            autoDetectEnabled = state.autoDetectLanguage,
            onAutoDetectToggle = onToggleAutoDetect,
            onLanguageSelected = onSourceLanguageSelected,
            onDismiss = { showSourceLanguagePicker = false }
        )
    }

    if (showTargetLanguagePicker) {
        LanguagePickerDialog(
            title = "Traduzir para (Idioma de Destino)",
            currentSelection = state.targetLanguage,
            isSourceMode = false,
            autoDetectEnabled = false,
            onAutoDetectToggle = {},
            onLanguageSelected = onTargetLanguageSelected,
            onDismiss = { showTargetLanguagePicker = false }
        )
    }

    if (showVoiceCloneDialog) {
        VoiceCloningDialog(
            voiceProfile = state.voiceProfile,
            onSaveProfile = onVoiceProfileSaved,
            onDismiss = { showVoiceCloneDialog = false }
        )
    }

    if (showLipSyncDialog) {
        LipSyncConfigDialog(
            currentQuality = state.lipSyncQuality,
            onQualitySelected = onLipSyncQualitySelected,
            onDismiss = { showLipSyncDialog = false }
        )
    }
}

@Composable
fun VideoSpecBadge(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceHighlight
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(label, fontSize = 10.sp, color = TextMuted)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun QuickLangChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}
