package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.StudioUiState
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun StudioEditorScreen(
    state: StudioUiState,
    onPlayPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onPlaySegmentAudio: (TranscriptSegment) -> Unit,
    onUpdateScript: (String, String) -> Unit,
    onUpdateEmotion: (String, EmotionType) -> Unit,
    onUpdateStems: (AudioStems) -> Unit,
    onUpdateSpeaker: (Speaker) -> Unit,
    onUpdateVoiceProfile: (VoiceProfile) -> Unit,
    onSetLipSyncQuality: (LipSyncQuality) -> Unit,
    onReturnToSetup: () -> Unit
) {
    var showMixerDialog by remember { mutableStateOf(false) }
    var showLipSyncDialog by remember { mutableStateOf(false) }
    var showSpeakersDialog by remember { mutableStateOf(false) }
    var showVoiceCloneDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var selectedSegmentForReview by remember { mutableStateOf<TranscriptSegment?>(null) }
    var editingSegmentId by remember { mutableStateOf<String?>(null) }
    var editingText by remember { mutableStateOf("") }

    val activeSegment = state.segments.firstOrNull { it.id == state.activeSegmentId }
        ?: state.segments.firstOrNull()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Surface(
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onReturnToSetup) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    state.videoMetadata.title.take(18) + "...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AccentEmerald.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        "QA ${state.qaScore}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentEmerald,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                "Dublado para ${state.targetLanguage.name} ${state.targetLanguage.flag}",
                                fontSize = 11.sp,
                                color = AccentCyanGlow
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Stems Quick Action
                        IconButton(
                            onClick = { showMixerDialog = true },
                            modifier = Modifier.testTag("open_mixer_topbar_button")
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = "Mixer", tint = AccentCyan)
                        }

                        // Export Button
                        Button(
                            onClick = { showExportDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_export_modal_button")
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Exportar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Video Player Card & Lip-Sync Canvas
            VideoPlayerCanvas(
                state = state,
                activeSegment = activeSegment,
                onPlayPause = onPlayPause,
                onSeekTo = onSeekTo,
                onOpenLipSync = { showLipSyncDialog = true }
            )

            // Audio Stems Quick Fader Ribbon
            AudioStemsRibbon(
                stems = state.audioStems,
                onOpenMixer = { showMixerDialog = true }
            )

            // Studio Quick Bar: Locutores, Minha Voz, LipSync
            StudioToolbar(
                state = state,
                onOpenSpeakers = { showSpeakersDialog = true },
                onOpenVoice = { showVoiceCloneDialog = true },
                onOpenLipSync = { showLipSyncDialog = true }
            )

            // Timeline Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ViewTimeline, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "TIMELINE DE DUBLAGEM & TRANSCRIÇÃO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    "${state.segments.size} blocos sincronizados",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            // Interactive Segments Timeline List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.segments, key = { it.id }) { segment ->
                    val isActive = segment.id == state.activeSegmentId
                    val speaker = state.speakers.firstOrNull { it.id == segment.speakerId }
                    val isEditing = editingSegmentId == segment.id

                    TimelineSegmentCard(
                        segment = segment,
                        speaker = speaker,
                        isActive = isActive,
                        isEditing = isEditing,
                        editingText = editingText,
                        onEditingTextChange = { editingText = it },
                        onStartEditing = {
                            editingSegmentId = segment.id
                            editingText = segment.dubbingScript
                        },
                        onSaveEdit = {
                            onUpdateScript(segment.id, editingText)
                            editingSegmentId = null
                        },
                        onCancelEdit = { editingSegmentId = null },
                        onPlayAudio = { onPlaySegmentAudio(segment) },
                        onOpenReview = { selectedSegmentForReview = segment },
                        onEmotionChange = { newEmotion -> onUpdateEmotion(segment.id, newEmotion) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

    // Dialogs & Sheets
    if (showMixerDialog) {
        AudioMixerSheet(
            stems = state.audioStems,
            onStemsChange = onUpdateStems,
            onDismiss = { showMixerDialog = false }
        )
    }

    if (showLipSyncDialog) {
        LipSyncConfigDialog(
            currentQuality = state.lipSyncQuality,
            onQualitySelected = onSetLipSyncQuality,
            onDismiss = { showLipSyncDialog = false }
        )
    }

    if (showSpeakersDialog) {
        SpeakersManagerDialog(
            speakers = state.speakers,
            onSpeakerUpdate = onUpdateSpeaker,
            onDismiss = { showSpeakersDialog = false }
        )
    }

    if (showVoiceCloneDialog) {
        VoiceCloningDialog(
            voiceProfile = state.voiceProfile,
            onSaveProfile = onUpdateVoiceProfile,
            onDismiss = { showVoiceCloneDialog = false }
        )
    }

    if (showExportDialog) {
        ExportDialog(
            videoTitle = state.videoMetadata.title,
            targetLanguageName = state.targetLanguage.name,
            onDismiss = { showExportDialog = false }
        )
    }

    if (selectedSegmentForReview != null) {
        LinguisticReviewDialog(
            segment = selectedSegmentForReview!!,
            onDismiss = { selectedSegmentForReview = null }
        )
    }
}

@Composable
fun VideoPlayerCanvas(
    state: StudioUiState,
    activeSegment: TranscriptSegment?,
    onPlayPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onOpenLipSync: () -> Unit
) {
    Surface(
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .testTag("video_player_container")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Simulated video scene with speaker & subtle ambient lighting
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E1B4B), Color(0xFF07070E))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Speaker visual representation & Lip-Sync Animation
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(AccentPurple, AccentCyan)
                                )
                            )
                            .border(2.dp, if (state.isPlaying) AccentCyan else Color.Transparent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            activeSegment?.emotion?.icon ?: "🎙️",
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Simulated Lip-Sync Wireframe Overlay
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.65f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable { onOpenLipSync() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (state.isPlaying) AccentEmerald else TextMuted)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Lip-Sync: ${state.lipSyncQuality.labelPt} (${state.lipSyncQuality.accuracyPercent}%)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentCyanGlow
                            )
                        }
                    }
                }
            }

            // Dubbed Captions Overlay (Styled like Captions App)
            if (activeSegment != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.80f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentPurple.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 36.dp, start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = activeSegment.dubbingScript,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Bottom Transport Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("video_play_pause_button")
                ) {
                    Icon(
                        if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pausar" else "Reproduzir",
                        tint = Color.White
                    )
                }

                // Scrubber Slider
                Slider(
                    value = state.currentTimeSec,
                    onValueChange = onSeekTo,
                    valueRange = 0f..state.videoMetadata.durationSec,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentPurpleGlow,
                        activeTrackColor = AccentPurple,
                        inactiveTrackColor = Color.DarkGray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                Text(
                    "${formatSeconds(state.currentTimeSec)} / ${state.videoMetadata.formattedDuration}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AudioStemsRibbon(
    stems: AudioStems,
    onOpenMixer: () -> Unit
) {
    Surface(
        color = DarkSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenMixer() }
            .testTag("open_audio_mixer_ribbon")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("STEMS DE ÁUDIO:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.width(10.dp))

                StemIndicatorPill("Voz", (stems.voiceVolume * 100).toInt(), StemVoiceColor, stems.voiceMuted)
                Spacer(modifier = Modifier.width(6.dp))
                StemIndicatorPill("Trilha", (stems.musicVolume * 100).toInt(), StemMusicColor, stems.musicMuted)
                Spacer(modifier = Modifier.width(6.dp))
                StemIndicatorPill("SFX", (stems.sfxVolume * 100).toInt(), StemSfxColor, stems.sfxMuted)
                Spacer(modifier = Modifier.width(6.dp))
                StemIndicatorPill("Ambiente", (stems.ambientVolume * 100).toInt(), StemAmbientColor, stems.ambientMuted)
            }

            Icon(Icons.Default.Tune, contentDescription = "Ajustar Stems", tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun StemIndicatorPill(name: String, percent: Int, color: Color, isMuted: Boolean) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (isMuted) Color.DarkGray else color.copy(alpha = 0.2f)
    ) {
        Text(
            text = if (isMuted) "$name [M]" else "$name $percent%",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isMuted) TextMuted else color,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun StudioToolbar(
    state: StudioUiState,
    onOpenSpeakers: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenLipSync: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Speakers
        StudioToolButton(
            icon = Icons.Default.Group,
            label = "${state.speakers.size} Locutores",
            color = AccentPurple,
            onClick = onOpenSpeakers,
            modifier = Modifier.weight(1f)
        )

        // Minha Voz
        StudioToolButton(
            icon = Icons.Default.Mic,
            label = if (state.voiceProfile.hasConsent) "Clone Ativo" else "Minha Voz",
            color = AccentEmerald,
            onClick = onOpenVoice,
            modifier = Modifier.weight(1f)
        )

        // Lip Sync
        StudioToolButton(
            icon = Icons.Default.Face,
            label = state.lipSyncQuality.labelPt,
            color = AccentCyan,
            onClick = onOpenLipSync,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StudioToolButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
fun TimelineSegmentCard(
    segment: TranscriptSegment,
    speaker: Speaker?,
    isActive: Boolean,
    isEditing: Boolean,
    editingText: String,
    onEditingTextChange: (String) -> Unit,
    onStartEditing: () -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenReview: () -> Unit,
    onEmotionChange: (EmotionType) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isActive) AccentPurple.copy(alpha = 0.12f) else DarkSurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) AccentPurple else DarkBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("segment_card_${segment.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Bar: Time, Speaker, Emotion Tag, Play TTS
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Time badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = DarkSurfaceHighlight
                    ) {
                        Text(
                            "${formatSeconds(segment.startTimeSec)} — ${formatSeconds(segment.endTimeSec)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentCyanGlow,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Speaker Badge
                    if (speaker != null) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(speaker.avatarColorHex).copy(alpha = 0.2f)
                        ) {
                            Text(
                                speaker.name.substringBefore(" ("),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(speaker.avatarColorHex),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Emotion Tag
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(segment.emotion.colorHex).copy(alpha = 0.2f)
                    ) {
                        Text(
                            "${segment.emotion.icon} ${segment.emotion.labelPt}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(segment.emotion.colorHex),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Audio TTS Preview Button
                    IconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("play_segment_audio_${segment.id}")
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = "Ouvir dublagem",
                            tint = AccentPurpleGlow,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Linguistic 4-Step Review trigger
                    IconButton(
                        onClick = onOpenReview,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Translate,
                            contentDescription = "Revisão Linguística",
                            tint = AccentCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Original Speech Reference
            Text(
                "Original: ${segment.originalText}",
                fontSize = 12.sp,
                color = TextMuted,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Dubbing Script (Inline Editable)
            if (isEditing) {
                OutlinedTextField(
                    value = editingText,
                    onValueChange = onEditingTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_script_input_${segment.id}"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkSurfaceElevated,
                        unfocusedContainerColor = DarkSurfaceElevated
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onCancelEdit) {
                        Text("Cancelar", color = TextSecondary, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = onSaveEdit,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Salvar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = segment.dubbingScript,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onStartEditing,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar fala",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom Metrics: WPM and Lip-Sync Confidence
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cadência: ${segment.speedWpm} WPM • Sincronia Labial: ${segment.lipSyncConfidence}%",
                    fontSize = 10.sp,
                    color = TextMuted
                )

                if (segment.isEdited) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AccentAmber.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "EDITADO",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentAmber,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

fun formatSeconds(sec: Float): String {
    val totalSecs = sec.toInt()
    val mins = totalSecs / 60
    val remainderSecs = totalSecs % 60
    return String.format(java.util.Locale.US, "%02d:%02d", mins, remainderSecs)
}
