package com.example.exp1_sem2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale


class VozHelper(private val context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var isTtsInitialized = false

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    private var onResultCallback: ((String) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null
    private var onPartialResultCallback: ((String) -> Unit)? = null

    init {
        initializeTextToSpeech()
        initializeSpeechRecognizer()
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("es", "ES"))
                isTtsInitialized = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    fun hablar(texto: String) {
        if (isTtsInitialized) {
            textToSpeech?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun detenerHablar() {
        textToSpeech?.stop()
    }

    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {
                    Log.d("VozHelper", "Listo para escuchar")
                }

                override fun onBeginningOfSpeech() {
                    Log.d("VozHelper", "Comenzó a hablar")
                }

                override fun onRmsChanged(rmsdB: Float) {
                    // Cambios en el volumen de la voz
                }

                override fun onBufferReceived(buffer: ByteArray?) {}

                override fun onEndOfSpeech() {
                    Log.d("VozHelper", "Terminó de hablar")
                    isListening = false
                }

                override fun onError(error: Int) {
                    isListening = false
                    val errorMessage = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                        SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permisos insuficientes"
                        SpeechRecognizer.ERROR_NETWORK -> "Error de red"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera agotado"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No se reconoció la voz"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                        SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz"
                        else -> "Error desconocido"
                    }
                    Log.e("VozHelper", "Error: $errorMessage")
                    onErrorCallback?.invoke(errorMessage)
                }

                override fun onResults(results: Bundle?) {
                    isListening = false
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        val textoReconocido = matches[0]
                        Log.d("VozHelper", "Texto reconocido: $textoReconocido")
                        onResultCallback?.invoke(textoReconocido)
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                    )
                    if (!matches.isNullOrEmpty()) {
                        onPartialResultCallback?.invoke(matches[0])
                    }
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    fun iniciarEscucha(
        onResult: (String) -> Unit,
        onError: (String) -> Unit,
        onPartialResult: ((String) -> Unit)? = null
    ) {
        if (isListening) {
            detenerEscucha()
        }

        onResultCallback = onResult
        onErrorCallback = onError
        onPartialResultCallback = onPartialResult

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            speechRecognizer?.startListening(intent)
            isListening = true
        } catch (e: Exception) {
            Log.e("VozHelper", "Error al iniciar escucha: ${e.message}")
            onErrorCallback?.invoke("Error al iniciar el reconocimiento de voz")
        }
    }

    fun detenerEscucha() {
        if (isListening) {
            speechRecognizer?.stopListening()
            isListening = false
        }
    }

    fun estaEscuchando(): Boolean = isListening

    fun liberar() {
        detenerHablar()
        detenerEscucha()

        textToSpeech?.shutdown()
        speechRecognizer?.destroy()

        textToSpeech = null
        speechRecognizer = null
        onResultCallback = null
        onErrorCallback = null
        onPartialResultCallback = null
    }
}