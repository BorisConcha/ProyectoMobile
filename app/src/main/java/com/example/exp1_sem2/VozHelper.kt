package com.example.exp1_sem2
import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale


class VozHelper(private val context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("es", "ES"))
                isInitialized = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    fun hablar(texto: String) {
        if (isInitialized) {
            textToSpeech?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun detener() {
        textToSpeech?.stop()
    }

    fun liberar() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}