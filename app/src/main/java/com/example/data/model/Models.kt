package com.example.data.model

data class Language(
    val code: String,
    val name: String,
    val flag: String,
    val isPopular: Boolean = false
)

object LanguageCatalog {
    val languages = listOf(
        Language("pt", "Português", "🇧🇷", isPopular = true),
        Language("en", "Inglês", "🇺🇸", isPopular = true),
        Language("es", "Espanhol", "🇪🇸", isPopular = true),
        Language("fr", "Francês", "🇫🇷", isPopular = true),
        Language("de", "Alemão", "🇩🇪", isPopular = true),
        Language("it", "Italiano", "🇮🇹", isPopular = true),
        Language("ja", "Japonês", "🇯🇵", isPopular = true),
        Language("ko", "Coreano", "🇰🇷", isPopular = true),
        Language("zh", "Chinês / Mandarim", "🇨🇳", isPopular = true),
        Language("zh-yue", "Cantonês", "🇭🇰"),
        Language("ar", "Árabe", "🇸🇦", isPopular = true),
        Language("hi", "Hindi", "🇮🇳", isPopular = true),
        Language("bn", "Bengali", "🇧🇩"),
        Language("ur", "Urdu", "🇵🇰"),
        Language("ru", "Russo", "🇷🇺"),
        Language("uk", "Ucraniano", "🇺🇦"),
        Language("tr", "Turco", "🇹🇷"),
        Language("nl", "Holandês", "🇳🇱"),
        Language("pl", "Polonês", "🇵🇱"),
        Language("sv", "Sueco", "🇸🇪"),
        Language("no", "Norueguês", "🇳🇴"),
        Language("da", "Dinamarquês", "🇩🇰"),
        Language("fi", "Finlandês", "🇫🇮"),
        Language("el", "Grego", "🇬🇷"),
        Language("he", "Hebraico", "🇮🇱"),
        Language("th", "Tailandês", "🇹🇭"),
        Language("vi", "Vietnamita", "🇻🇳"),
        Language("id", "Indonésio", "🇮🇩"),
        Language("ms", "Malaio", "🇲🇾"),
        Language("fil", "Filipino", "🇵🇭"),
        Language("cs", "Tcheco", "🇨🇿"),
        Language("sk", "Eslovaco", "🇸🇰"),
        Language("ro", "Romeno", "🇷🇴"),
        Language("hu", "Húngaro", "🇭🇺"),
        Language("bg", "Búlgaro", "🇧🇬"),
        Language("hr", "Croata", "🇭🇷"),
        Language("sr", "Sérvio", "🇷🇸"),
        Language("sl", "Esloveno", "🇸🇮"),
        Language("sw", "Suaíli", "🇰🇪"),
        Language("zu", "Zulu", "🇿🇦"),
        Language("am", "Amárico", "🇪🇹"),
        Language("af", "Afrikaans", "🇿🇦"),
        Language("yo", "Yoruba", "🇳🇬"),
        Language("ig", "Igbo", "🇳🇬"),
        Language("ha", "Hausa", "🇳🇬"),
        Language("xh", "Xhosa", "🇿🇦"),
        Language("rw", "Kinyarwanda", "🇷🇼")
    )

    fun findByCode(code: String): Language =
        languages.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: languages[0]
}

enum class EmotionType(val labelPt: String, val icon: String, val colorHex: Long) {
    HAPPY("Feliz", "😊", 0xFF10B981),
    ENTHUSIASTIC("Entusiasmado", "⚡", 0xFFF59E0B),
    CALM("Calmo", "🌊", 0xFF06B6D4),
    SERIOUS("Sério", "💼", 0xFF6366F1),
    SURPRISED("Surpreso", "😲", 0xFFEC4899),
    ANGRY("Irritado", "🔥", 0xFFEF4444),
    SAD("Triste", "🌧️", 0xFF64748B),
    MOTIVATIONAL("Motivacional", "🚀", 0xFF8B5CF6),
    DRAMATIC("Dramático", "🎭", 0xFFA855F7),
    NEUTRAL("Neutro", "🎙️", 0xFF94A3B8)
}

enum class LipSyncQuality(val labelPt: String, val description: String, val accuracyPercent: Int) {
    DISABLED("Desativada", "Apenas alinhamento temporal de áudio", 70),
    BASIC("Básica", "Sincronização fonética aproximada", 84),
    ADVANCED("Avançada", "Alinhamento fonema-lábio com IA generativa", 94),
    MAXIMUM("Máxima qualidade", "Refinamento fotorrealista milimétrico e microexpressões", 99)
}

data class Speaker(
    val id: String,
    val name: String,
    val assignedVoiceName: String,
    val gender: String, // "Masculino", "Feminino", "Neutro"
    val pitchMultiplier: Float = 1.0f,
    val speedMultiplier: Float = 1.0f,
    val isCustomVoice: Boolean = false,
    val avatarColorHex: Long = 0xFF7C4DFF
)

data class TranscriptSegment(
    val id: String,
    val startTimeSec: Float,
    val endTimeSec: Float,
    val speakerId: String,
    val originalText: String,
    val directTranslation: String,
    val revisedTranslation: String,
    val dubbingScript: String,
    val emotion: EmotionType = EmotionType.NEUTRAL,
    val speedWpm: Int = 145,
    val lipSyncConfidence: Int = 96,
    val linguisticNotes: String = "",
    val isEdited: Boolean = false
)

data class AudioStems(
    val voiceVolume: Float = 1.0f,
    val musicVolume: Float = 0.65f,
    val sfxVolume: Float = 0.70f,
    val ambientVolume: Float = 0.50f,
    val voiceMuted: Boolean = false,
    val musicMuted: Boolean = false,
    val sfxMuted: Boolean = false,
    val ambientMuted: Boolean = false
)

data class VoiceProfile(
    val id: String,
    val name: String,
    val hasConsent: Boolean,
    val sampleDurationSec: Int,
    val timbre: String, // "Grave Aveludado", "Médio Expressivo", "Brilhante e Ágil"
    val pitchHz: Int,
    val paceWpm: Int,
    val energyLevel: String, // "Alta", "Moderada", "Dinâmica"
    val style: String,
    val isUserVoice: Boolean = true,
    val sampleAudioUri: String? = null
)

data class VideoMetadata(
    val title: String,
    val durationSec: Float,
    val resolution: String,
    val format: String,
    val fileSizeBytes: Long,
    val fps: Int = 30,
    val uri: String? = null,
    val sampleId: String? = null
) {
    val formattedSize: String
        get() {
            val mb = fileSizeBytes / (1024.0 * 1024.0)
            return String.format(java.util.Locale.US, "%.1f MB", mb)
        }

    val formattedDuration: String
        get() {
            val mins = (durationSec / 60).toInt()
            val secs = (durationSec % 60).toInt()
            return String.format(java.util.Locale.US, "%02d:%02d", mins, secs)
        }
}

enum class PipelineStage(val stageNumber: Int, val title: String, val detail: String) {
    AUDIO_EXTRACTION(1, "Extração de Áudio", "Isolando faixa master em estéreo de 48kHz"),
    LANGUAGE_DETECTION(2, "Detecção de Idioma", "Identificando fala nativa e acentos regionais"),
    SPEECH_RECOGNITION(3, "Reconhecimento de Fala (ASR)", "Gerando transcrição fonética com timestamps"),
    SPEAKER_DIARIZATION(4, "Diarização de Locutores", "Mapeando e separando vozes distintas no vídeo"),
    STEMS_SEPARATION(5, "Separação de Stems IA", "Dividindo canais: Voz, Música, SFX e Ambiente"),
    EMOTION_ANALYSIS(6, "Análise de Emoção & Tom", "Detectando expressividade, entonação e pausas"),
    CONTEXTUAL_TRANSLATION(7, "Tradução Contextual", "Localizando gírias, humor e referências culturais"),
    LINGUISTIC_REVISION(8, "Revisão Linguística IA", "Ajustando cadência e script para fala natural"),
    VOICE_SYNTHESIS(9, "Síntese Vocal Personalizada", "Gerando voz com identidade vocal autorizada"),
    TIMING_ALIGNMENT(10, "Ajuste Temporal & Ritmo", "Equalizando duração de frases para o corte original"),
    LIP_SYNC(11, "Sincronização Labial IA", "Alinhando fonemas aos movimentos faciais"),
    AUDIO_RECOMBINATION(12, "Recombinação de Faixas", "Mixando nova voz dublada, trilha e efeitos"),
    QUALITY_CONTROL(13, "Controle de Qualidade (QA)", "Verificando precisão fonética e sincronia"),
    READY(14, "Processamento Concluído", "Vídeo localizado pronto para revisão e exportação")
}
