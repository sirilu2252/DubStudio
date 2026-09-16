package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerDialog(
    title: String,
    currentSelection: Language?,
    isSourceMode: Boolean,
    autoDetectEnabled: Boolean,
    onAutoDetectToggle: (Boolean) -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.isBlank()) LanguageCatalog.languages
        else LanguageCatalog.languages.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.code.contains(searchQuery, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("language_picker_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("language_search_input"),
                placeholder = { Text("Pesquisar idioma (ex: Português, Inglês, Japonês...)", color = TextMuted, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = AccentPurple) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar", tint = TextSecondary)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = DarkBorder,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Auto-detect toggle for source language
            if (isSourceMode) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (autoDetectEnabled) AccentPurple.copy(alpha = 0.15f) else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (autoDetectEnabled) AccentPurple else DarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onAutoDetectToggle(!autoDetectEnabled) }
                        .testTag("auto_detect_language_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✨", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Detectar idioma automaticamente",
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "A IA analisa o áudio e identifica a fala",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Switch(
                            checked = autoDetectEnabled,
                            onCheckedChange = onAutoDetectToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentPurple
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "TODOS OS IDIOMAS (${filteredLanguages.size})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
            ) {
                items(filteredLanguages, key = { it.code }) { lang ->
                    val isSelected = !autoDetectEnabled && currentSelection?.code == lang.code
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) AccentPurple.copy(alpha = 0.2f) else Color.Transparent)
                            .clickable {
                                onLanguageSelected(lang)
                                onDismiss()
                            }
                            .padding(horizontal = 12.dp, vertical = 14.dp)
                            .testTag("language_item_${lang.code}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = lang.flag, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = lang.name,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AccentPurpleGlow else TextPrimary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Código ISO: ${lang.code.uppercase()}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Selecionado",
                                tint = AccentPurple
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioMixerSheet(
    stems: AudioStems,
    onStemsChange: (AudioStems) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("audio_mixer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.GraphicEq, contentDescription = null, tint = AccentCyan)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Mixer de Áudio Multitrack",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Text(
                "A IA separou o áudio em 4 stems independentes. Ajuste os volumes para uma dublagem profissional com trilha e efeitos preservados.",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stem 1: Voice
            StemControlItem(
                title = "Voz Traduzida (Voice)",
                subtitle = "Substituição com voz gerada por IA",
                color = StemVoiceColor,
                volume = stems.voiceVolume,
                isMuted = stems.voiceMuted,
                onVolumeChange = { onStemsChange(stems.copy(voiceVolume = it)) },
                onMuteToggle = { onStemsChange(stems.copy(voiceMuted = !stems.voiceMuted)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stem 2: Music
            StemControlItem(
                title = "Música de Fundo (Music)",
                subtitle = "Trilha sonora original preservada",
                color = StemMusicColor,
                volume = stems.musicVolume,
                isMuted = stems.musicMuted,
                onVolumeChange = { onStemsChange(stems.copy(musicVolume = it)) },
                onMuteToggle = { onStemsChange(stems.copy(musicMuted = !stems.musicMuted)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stem 3: SFX
            StemControlItem(
                title = "Efeitos Sonoros (SFX)",
                subtitle = "Foley, impactos, cliques e transições",
                color = StemSfxColor,
                volume = stems.sfxVolume,
                isMuted = stems.sfxMuted,
                onVolumeChange = { onStemsChange(stems.copy(sfxVolume = it)) },
                onMuteToggle = { onStemsChange(stems.copy(sfxMuted = !stems.sfxMuted)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stem 4: Ambient
            StemControlItem(
                title = "Ruído Ambiente (Ambient)",
                subtitle = "Acústica da sala e atmosfera natural",
                color = StemAmbientColor,
                volume = stems.ambientVolume,
                isMuted = stems.ambientMuted,
                onVolumeChange = { onStemsChange(stems.copy(ambientVolume = it)) },
                onMuteToggle = { onStemsChange(stems.copy(ambientMuted = !stems.ambientMuted)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("mixer_apply_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Aplicar Mixagem", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun StemControlItem(
    title: String,
    subtitle: String,
    color: Color,
    volume: Float,
    isMuted: Boolean,
    onVolumeChange: (Float) -> Unit,
    onMuteToggle: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isMuted) TextMuted else color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(title, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 14.sp)
                        Text(subtitle, color = TextMuted, fontSize = 11.sp)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isMuted) "MUDO" else "${(volume * 100).toInt()}%",
                        color = if (isMuted) AccentRose else color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onMuteToggle,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Mudo",
                            tint = if (isMuted) AccentRose else TextSecondary
                        )
                    }
                }
            }

            Slider(
                value = if (isMuted) 0f else volume,
                onValueChange = onVolumeChange,
                enabled = !isMuted,
                colors = SliderDefaults.colors(
                    thumbColor = color,
                    activeTrackColor = color,
                    inactiveTrackColor = DarkSurfaceHighlight
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun VoiceCloningDialog(
    voiceProfile: VoiceProfile,
    onSaveProfile: (VoiceProfile) -> Unit,
    onDismiss: () -> Unit
) {
    var consentChecked by remember { mutableStateOf(voiceProfile.hasConsent) }
    var isRecording by remember { mutableStateOf(false) }
    var recordedSeconds by remember { mutableStateOf(voiceProfile.sampleDurationSec.coerceAtLeast(15)) }
    var voiceName by remember { mutableStateOf(voiceProfile.name) }
    var selectedTimbre by remember { mutableStateOf(voiceProfile.timbre) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, DarkBorder, RoundedCornerShape(20.dp)),
            color = DarkSurfaceElevated
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = AccentPurple)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Minha Voz (Clone Autorizado)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                    }
                }

                Text(
                    "Crie uma representação vocal personalizada para dublar vídeos com sua própria identidade, timbre e expressividade.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mandatory Consent Checkbox
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (consentChecked) AccentPurple.copy(alpha = 0.15f) else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (consentChecked) AccentPurple else DarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { consentChecked = !consentChecked }
                        .testTag("voice_consent_checkbox")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = consentChecked,
                            onCheckedChange = { consentChecked = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AccentPurple,
                                checkmarkColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "Confirmo que tenho autorização para utilizar esta voz.",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                "O uso da voz de terceiros sem autorização expressa é estritamente proibido pelas diretrizes de IA ética.",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Voice Sample Recording / Simulation
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Amostra de Voz (Mínimo recomendado: 15 segundos)",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Waveform Simulation Bars
                            repeat(16) { index ->
                                val height = if (isRecording) {
                                    (10 + (index * 7) % 32).dp
                                } else 14.dp
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(height)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(if (isRecording) AccentCyan else AccentPurple.copy(alpha = 0.6f))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    isRecording = !isRecording
                                    if (!isRecording) {
                                        recordedSeconds = 24
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isRecording) AccentRose else AccentPurple
                                ),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.testTag("record_voice_button")
                            ) {
                                Icon(
                                    if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if (isRecording) "Parar Gravação" else "Gravar Amostra Vocal",
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Text(
                            "Duração gravada: ${recordedSeconds}s • Qualidade biométrica: 98.4%",
                            color = AccentEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Telemetry / Vocal characteristics
                Text("CARACTERÍSTICAS VOCAIS DETECTADAS", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    VocalMetricBadge("Timbre", "Expressivo & Claro", AccentPurple)
                    VocalMetricBadge("Frequência", "128 Hz (Grave)", AccentCyan)
                    VocalMetricBadge("Ritmo", "148 WPM", AccentAmber)
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage!!, color = AccentRose, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (!consentChecked) {
                                errorMessage = "É obrigatório confirmar a autorização do uso da voz."
                                return@Button
                            }
                            onSaveProfile(
                                voiceProfile.copy(
                                    hasConsent = true,
                                    name = voiceName,
                                    sampleDurationSec = recordedSeconds,
                                    timbre = selectedTimbre
                                )
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        modifier = Modifier.testTag("save_voice_profile_button")
                    ) {
                        Text("Salvar Voz Autorizada", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VocalMetricBadge(title: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = Modifier.widthIn(min = 90.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
            Text(title, fontSize = 10.sp, color = TextMuted)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LipSyncConfigDialog(
    currentQuality: LipSyncQuality,
    onQualitySelected: (LipSyncQuality) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("lip_sync_config_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Face, contentDescription = null, tint = AccentCyan)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Sincronização Labial (Lip-Sync IA)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Text(
                "Ajusta os movimentos da boca e lábios para corresponder perfeitamente aos fonemas do idioma traduzido.",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Transparency disclaimer
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AccentCyan.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Garantia de Qualidade: Quando o ângulo facial não permitir recriação visual sem artefatos, o sistema utiliza alinhamento temporal e warping rítmico automático de áudio.",
                        fontSize = 11.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LipSyncQuality.values().forEach { quality ->
                val isSelected = currentQuality == quality
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) AccentCyan.copy(alpha = 0.15f) else DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) AccentCyan else DarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onQualitySelected(quality) }
                        .testTag("lip_sync_option_${quality.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    quality.labelPt,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isSelected) AccentCyanGlow else TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = DarkSurfaceHighlight
                                ) {
                                    Text(
                                        "${quality.accuracyPercent}% precisão",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) AccentCyan else TextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                quality.description,
                                fontSize = 12.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        RadioButton(
                            selected = isSelected,
                            onClick = { onQualitySelected(quality) },
                            colors = RadioButtonDefaults.colors(selectedColor = AccentCyan)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("lip_sync_confirm_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
            ) {
                Text("Confirmar Ajuste Labial", fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakersManagerDialog(
    speakers: List<Speaker>,
    onSpeakerUpdate: (Speaker) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("speakers_manager_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Group, contentDescription = null, tint = AccentPurple)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Locutores Detectados (${speakers.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Text(
                "A IA identificou automaticamente múltiplos locutores por diarização vocal. Você pode atribuir vozes distintas para cada pessoa.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp)
            ) {
                items(speakers, key = { it.id }) { speaker ->
                    SpeakerCard(
                        speaker = speaker,
                        onSpeakerChange = onSpeakerUpdate
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("speakers_done_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Salvar Atribuição de Locutores", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SpeakerCard(
    speaker: Speaker,
    onSpeakerChange: (Speaker) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(speaker.avatarColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            speaker.name.take(2).uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(speaker.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 14.sp)
                        Text(
                            "${speaker.gender} • Voz: ${speaker.assignedVoiceName}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                if (speaker.isCustomVoice) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AccentPurple.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "CLONE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentPurpleGlow,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Voice Presets
            Text("Voz atribuída:", fontSize = 11.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Minha Voz", "Elena Pro", "Marco Deep", "Lucas Dynamic").forEach { voice ->
                    val isSelected = speaker.assignedVoiceName.contains(voice, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) AccentPurple else DarkSurfaceHighlight,
                        modifier = Modifier
                            .clickable {
                                onSpeakerChange(
                                    speaker.copy(
                                        assignedVoiceName = voice,
                                        isCustomVoice = voice == "Minha Voz"
                                    )
                                )
                            }
                    ) {
                        Text(
                            voice,
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinguisticReviewDialog(
    segment: TranscriptSegment,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("linguistic_review_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, contentDescription = null, tint = AccentPurple)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Revisão Linguística IA",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Text(
                "Comparação em 4 etapas: A IA não realiza tradução literal, adaptando gírias, ritmo e referências culturais para o idioma de destino.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Step 1: Original Transcription
            PipelineStageCard(
                stepNumber = "1",
                stepName = "TRANSCRIÇÃO ORIGINAL",
                content = segment.originalText,
                badge = "${segment.startTimeSec}s - ${segment.endTimeSec}s",
                badgeColor = AccentAmber
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step 2: Direct Translation
            PipelineStageCard(
                stepNumber = "2",
                stepName = "TRADUÇÃO DIRETA (LITERAL)",
                content = segment.directTranslation,
                badge = "Não adaptada",
                badgeColor = TextMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step 3: Revised Contextual Translation
            PipelineStageCard(
                stepNumber = "3",
                stepName = "TRADUÇÃO REVISADA CONTEXTUAL",
                content = segment.revisedTranslation,
                badge = "Contextualizada",
                badgeColor = AccentCyan
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Step 4: Final Dubbing Script
            PipelineStageCard(
                stepNumber = "4",
                stepName = "SCRIPT FINAL PARA DUBLAGEM",
                content = segment.dubbingScript,
                badge = "${segment.speedWpm} WPM • Sincronia OK",
                badgeColor = AccentEmerald,
                isHighlight = true
            )

            if (segment.linguisticNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentPurple.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
                        Text("💡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            segment.linguisticNotes,
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Text("Entendido", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PipelineStageCard(
    stepNumber: String,
    stepName: String,
    content: String,
    badge: String,
    badgeColor: Color,
    isHighlight: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isHighlight) AccentPurple.copy(alpha = 0.15f) else DarkSurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isHighlight) AccentPurple else DarkBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stepNumber, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stepName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = badgeColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(content, fontSize = 13.sp, color = TextPrimary, fontWeight = if (isHighlight) FontWeight.Medium else FontWeight.Normal)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportDialog(
    videoTitle: String,
    targetLanguageName: String,
    onDismiss: () -> Unit
) {
    var selectedResolution by remember { mutableStateOf("1080p Full HD") }
    var selectedFormat by remember { mutableStateOf("MP4 (H.264)") }
    var includeSubtitles by remember { mutableStateOf(true) }
    var exportAudioStemsSeparately by remember { mutableStateOf(false) }
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(0f) }
    var isExportCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(isExporting) {
        if (isExporting) {
            exportProgress = 0f
            while (exportProgress < 1f) {
                kotlinx.coroutines.delay(80)
                exportProgress += 0.05f
            }
            isExporting = false
            isExportCompleted = true
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurfaceElevated,
        dragHandle = { BottomSheetDefaults.DragHandle(color = TextMuted) },
        modifier = Modifier.testTag("export_dialog_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, tint = AccentEmerald)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Exportar Vídeo Dublado",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar", tint = TextSecondary)
                }
            }

            Text(
                "Exportação de alta fidelidade com dublagem IA em $targetLanguageName, sincronização labial e mixagem masterizada.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (!isExportCompleted) {
                // Resolution Selector
                Text("RESOLUÇÃO DE EXPORTAÇÃO", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("1080p Full HD", "4K Ultra HD", "720p Social").forEach { res ->
                        val isSelected = selectedResolution == res
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) AccentPurple else DarkSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AccentPurple else DarkBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedResolution = res }
                        ) {
                            Text(
                                res,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Format Selector
                Text("FORMATO DE VÍDEO", fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("MP4 (H.264)", "MOV (ProRes)", "WebM").forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) AccentCyan else DarkSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) AccentCyan else DarkBorder),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedFormat = fmt }
                        ) {
                            Text(
                                fmt,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else TextSecondary,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Checkboxes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { includeSubtitles = !includeSubtitles },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeSubtitles,
                        onCheckedChange = { includeSubtitles = it },
                        colors = CheckboxDefaults.colors(checkedColor = AccentPurple)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Embutir legendas dinâmicas estilo Captions no vídeo", fontSize = 13.sp, color = TextPrimary)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { exportAudioStemsSeparately = !exportAudioStemsSeparately },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = exportAudioStemsSeparately,
                        onCheckedChange = { exportAudioStemsSeparately = it },
                        colors = CheckboxDefaults.colors(checkedColor = AccentPurple)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Exportar também stems de áudio individuais (Voz, Trilha, SFX)", fontSize = 13.sp, color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (isExporting) {
                    LinearProgressIndicator(
                        progress = { exportProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AccentEmerald,
                        trackColor = DarkSurfaceHighlight
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Renderizando áudio e alinhando vídeo: ${(exportProgress * 100).toInt()}%...",
                        fontSize = 12.sp,
                        color = AccentEmerald
                    )
                } else {
                    Button(
                        onClick = { isExporting = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("start_export_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar Exportação de Alta Qualidade", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            } else {
                // Completed View
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AccentEmerald.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentEmerald),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentEmerald, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Vídeo Dublado com Sucesso!", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                        Text(
                            "Arquivo pronto para publicação em redes sociais ou download.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                ) {
                    Text("Concluído", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
