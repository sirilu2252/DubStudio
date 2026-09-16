package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.service.GeminiDubService
import com.example.data.service.SampleData
import com.example.data.service.TtsSpeechEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    UPLOAD_SETUP,
    PIPELINE_PROCESSING,
    STUDIO_EDITOR
}

data class StudioUiState(
    val currentScreen: AppScreen = AppScreen.UPLOAD_SETUP,
    val videoMetadata: VideoMetadata = SampleData.sampleVideos[0],
    val sourceLanguage: Language = LanguageCatalog.languages[0], // Português
    val targetLanguage: Language = LanguageCatalog.languages[1], // Inglês
    val autoDetectLanguage: Boolean = true,
    val detectedSourceLanguage: Language? = null,
    val lipSyncQuality: LipSyncQuality = LipSyncQuality.ADVANCED,
    val audioStems: AudioStems = AudioStems(),
    val voiceProfile: VoiceProfile = VoiceProfile(
        id = "user_voice_1",
        name = "Minha Voz Clonal",
        hasConsent = true,
        sampleDurationSec = 22,
        timbre = "Médio Expressivo",
        pitchHz = 125,
        paceWpm = 148,
        energyLevel = "Dinâmica",
        style = "Conversacional Natural"
    ),
    val speakers: List<Speaker> = SampleData.getSampleSpeakers("sample_tech"),
    val segments: List<TranscriptSegment> = SampleData.getSampleSegments("sample_tech", "en"),
    val activeSegmentId: String = "seg_1",
    val isPlaying: Boolean = false,
    val currentTimeSec: Float = 0f,
    val pipelineStage: PipelineStage = PipelineStage.AUDIO_EXTRACTION,
    val pipelineProgress: Float = 0f,
    val qaScore: Int = 98
)

class DubStudioViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    private val ttsEngine = TtsSpeechEngine(application)
    private val geminiService = GeminiDubService()

    private var playbackJob: Job? = null
    private var pipelineJob: Job? = null

    init {
        // Initialize with default sample
        loadSampleVideo(SampleData.sampleVideos[0])
    }

    fun loadSampleVideo(sample: VideoMetadata) {
        val speakers = SampleData.getSampleSpeakers(sample.sampleId ?: "sample_tech")
        val segments = SampleData.getSampleSegments(
            sample.sampleId ?: "sample_tech",
            _uiState.value.targetLanguage.code
        )
        _uiState.value = _uiState.value.copy(
            videoMetadata = sample,
            speakers = speakers,
            segments = segments,
            activeSegmentId = segments.firstOrNull()?.id ?: "",
            currentTimeSec = 0f,
            isPlaying = false
        )
    }

    fun setUploadedVideo(name: String, format: String, sizeBytes: Long, durationSec: Float, uri: String) {
        val metadata = VideoMetadata(
            title = name,
            durationSec = durationSec,
            resolution = "1080p (1080x1920)",
            format = format.uppercase(),
            fileSizeBytes = sizeBytes,
            fps = 30,
            uri = uri,
            sampleId = null
        )
        val customSpeakers = listOf(
            Speaker("spk_1", "Locutor Principal", "Minha Voz (Clone Autorizado)", "Masculino", 1.0f, 1.0f, true, 0xFF8B5CF6)
        )
        val customSegments = listOf(
            TranscriptSegment(
                id = "seg_user_1",
                startTimeSec = 0f,
                endTimeSec = 6.5f,
                speakerId = "spk_1",
                originalText = "Vídeo enviado com sucesso. Pronto para ser traduzido e dublado com sua própria voz.",
                directTranslation = "Uploaded video with success. Ready to be translated and dubbed with your own voice.",
                revisedTranslation = "Video successfully uploaded! Ready for AI dubbing with your authentic vocal identity.",
                dubbingScript = "Video uploaded! Ready for AI dubbing with your authentic voice.",
                emotion = EmotionType.ENTHUSIASTIC,
                speedWpm = 145,
                lipSyncConfidence = 98,
                linguisticNotes = "Otimizado para abertura limpa e expressiva."
            )
        )
        _uiState.value = _uiState.value.copy(
            videoMetadata = metadata,
            speakers = customSpeakers,
            segments = customSegments,
            activeSegmentId = "seg_user_1",
            currentTimeSec = 0f
        )
    }

    fun setSourceLanguage(language: Language) {
        _uiState.value = _uiState.value.copy(
            sourceLanguage = language,
            autoDetectLanguage = false
        )
    }

    fun setTargetLanguage(language: Language) {
        _uiState.value = _uiState.value.copy(targetLanguage = language)
        // Refresh segments for sample if currently in sample
        val sampleId = _uiState.value.videoMetadata.sampleId
        if (sampleId != null) {
            val refreshedSegments = SampleData.getSampleSegments(sampleId, language.code)
            _uiState.value = _uiState.value.copy(segments = refreshedSegments)
        }
    }

    fun toggleAutoDetect(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoDetectLanguage = enabled)
    }

    fun setLipSyncQuality(quality: LipSyncQuality) {
        _uiState.value = _uiState.value.copy(lipSyncQuality = quality)
    }

    fun updateAudioStems(stems: AudioStems) {
        _uiState.value = _uiState.value.copy(audioStems = stems)
    }

    fun updateVoiceProfile(profile: VoiceProfile) {
        _uiState.value = _uiState.value.copy(voiceProfile = profile)
    }

    fun updateSpeaker(speaker: Speaker) {
        val updated = _uiState.value.speakers.map {
            if (it.id == speaker.id) speaker else it
        }
        _uiState.value = _uiState.value.copy(speakers = updated)
    }

    fun updateSegmentScript(segmentId: String, newScript: String) {
        val updated = _uiState.value.segments.map {
            if (it.id == segmentId) it.copy(dubbingScript = newScript, isEdited = true) else it
        }
        _uiState.value = _uiState.value.copy(segments = updated)
    }

    fun updateSegmentEmotion(segmentId: String, emotion: EmotionType) {
        val updated = _uiState.value.segments.map {
            if (it.id == segmentId) it.copy(emotion = emotion) else it
        }
        _uiState.value = _uiState.value.copy(segments = updated)
    }

    fun startPipeline() {
        pipelineJob?.cancel()
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.PIPELINE_PROCESSING,
            pipelineStage = PipelineStage.AUDIO_EXTRACTION,
            pipelineProgress = 0f
        )

        pipelineJob = viewModelScope.launch {
            val stages = PipelineStage.values()
            for (i in stages.indices) {
                val stage = stages[i]
                _uiState.value = _uiState.value.copy(
                    pipelineStage = stage,
                    pipelineProgress = (i + 1).toFloat() / stages.size.toFloat()
                )

                if (stage == PipelineStage.LANGUAGE_DETECTION && _uiState.value.autoDetectLanguage) {
                    _uiState.value = _uiState.value.copy(
                        detectedSourceLanguage = LanguageCatalog.findByCode("pt")
                    )
                }

                // Simulate processing time per stage with realistic progression
                delay(300)
            }

            // Processing complete -> Enter Studio Editor!
            delay(400)
            _uiState.value = _uiState.value.copy(
                currentScreen = AppScreen.STUDIO_EDITOR,
                pipelineStage = PipelineStage.READY,
                currentTimeSec = 0f,
                isPlaying = false
            )
        }
    }

    fun returnToSetup() {
        stopPlayback()
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.UPLOAD_SETUP,
            isPlaying = false
        )
    }

    fun playSegmentAudio(segment: TranscriptSegment) {
        _uiState.value = _uiState.value.copy(
            activeSegmentId = segment.id,
            currentTimeSec = segment.startTimeSec
        )
        val targetLangCode = _uiState.value.targetLanguage.code
        val speaker = _uiState.value.speakers.firstOrNull { it.id == segment.speakerId }
        val pitch = speaker?.pitchMultiplier ?: 1.0f
        val speed = speaker?.speedMultiplier ?: 1.0f

        ttsEngine.speak(
            text = segment.dubbingScript,
            targetLanguageCode = targetLangCode,
            emotion = segment.emotion,
            pitchMultiplier = pitch,
            speedMultiplier = speed
        )
    }

    fun togglePlayPause() {
        val currentlyPlaying = _uiState.value.isPlaying
        if (currentlyPlaying) {
            stopPlayback()
        } else {
            startPlayback()
        }
    }

    private fun startPlayback() {
        _uiState.value = _uiState.value.copy(isPlaying = true)
        playbackJob?.cancel()

        // Speak active segment
        val currentSeg = _uiState.value.segments.firstOrNull {
            _uiState.value.currentTimeSec in it.startTimeSec..it.endTimeSec
        } ?: _uiState.value.segments.firstOrNull()

        if (currentSeg != null) {
            playSegmentAudio(currentSeg)
        }

        playbackJob = viewModelScope.launch {
            val totalDuration = _uiState.value.videoMetadata.durationSec
            while (_uiState.value.isPlaying && _uiState.value.currentTimeSec < totalDuration) {
                delay(100)
                val newTime = _uiState.value.currentTimeSec + 0.1f
                val activeSeg = _uiState.value.segments.firstOrNull {
                    newTime in it.startTimeSec..it.endTimeSec
                }
                _uiState.value = _uiState.value.copy(
                    currentTimeSec = newTime,
                    activeSegmentId = activeSeg?.id ?: _uiState.value.activeSegmentId
                )
            }
            if (_uiState.value.currentTimeSec >= totalDuration) {
                stopPlayback()
                _uiState.value = _uiState.value.copy(currentTimeSec = 0f)
            }
        }
    }

    private fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null
        ttsEngine.stop()
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    fun seekTo(seconds: Float) {
        val clamped = seconds.coerceIn(0f, _uiState.value.videoMetadata.durationSec)
        val activeSeg = _uiState.value.segments.firstOrNull {
            clamped in it.startTimeSec..it.endTimeSec
        }
        _uiState.value = _uiState.value.copy(
            currentTimeSec = clamped,
            activeSegmentId = activeSeg?.id ?: _uiState.value.activeSegmentId
        )
    }

    override fun onCleared() {
        super.onCleared()
        ttsEngine.release()
        playbackJob?.cancel()
        pipelineJob?.cancel()
    }
}
