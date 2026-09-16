package com.example.data.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.EmotionType
import java.util.Locale

class TtsSpeechEngine(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var onDoneCallback: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        onDoneCallback?.invoke()
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        onDoneCallback?.invoke()
                    }
                })
            } else {
                Log.e("TtsSpeechEngine", "TextToSpeech init failed with status: $status")
            }
        }
    }

    fun speak(
        text: String,
        targetLanguageCode: String,
        emotion: EmotionType = EmotionType.NEUTRAL,
        pitchMultiplier: Float = 1.0f,
        speedMultiplier: Float = 1.0f,
        onFinished: () -> Unit = {}
    ) {
        if (!isInitialized || tts == null) {
            onFinished()
            return
        }

        onDoneCallback = onFinished
        val locale = getLocaleForCode(targetLanguageCode)
        tts?.language = locale

        // Modulate pitch and speech rate based on emotion & speaker settings
        val emotionPitch = when (emotion) {
            EmotionType.HAPPY -> 1.15f
            EmotionType.ENTHUSIASTIC -> 1.25f
            EmotionType.SURPRISED -> 1.30f
            EmotionType.CALM -> 0.90f
            EmotionType.SERIOUS -> 0.85f
            EmotionType.DRAMATIC -> 0.92f
            EmotionType.ANGRY -> 1.10f
            EmotionType.SAD -> 0.80f
            EmotionType.MOTIVATIONAL -> 1.08f
            EmotionType.NEUTRAL -> 1.0f
        }

        val emotionRate = when (emotion) {
            EmotionType.ENTHUSIASTIC -> 1.15f
            EmotionType.HAPPY -> 1.08f
            EmotionType.CALM -> 0.90f
            EmotionType.SERIOUS -> 0.92f
            EmotionType.DRAMATIC -> 0.88f
            EmotionType.SAD -> 0.80f
            EmotionType.MOTIVATIONAL -> 1.05f
            else -> 1.0f
        }

        tts?.setPitch(emotionPitch * pitchMultiplier)
        tts?.setSpeechRate(emotionRate * speedMultiplier)

        val params = android.os.Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "dub_utterance_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    private fun getLocaleForCode(code: String): Locale {
        return when (code.lowercase()) {
            "pt" -> Locale("pt", "BR")
            "en" -> Locale.US
            "es" -> Locale("es", "ES")
            "fr" -> Locale.FRENCH
            "de" -> Locale.GERMAN
            "it" -> Locale.ITALIAN
            "ja" -> Locale.JAPANESE
            "ko" -> Locale.KOREAN
            "zh", "zh-yue" -> Locale.CHINESE
            else -> Locale(code)
        }
    }
}
