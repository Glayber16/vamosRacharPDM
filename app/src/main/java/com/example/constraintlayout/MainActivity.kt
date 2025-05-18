package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity(), TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var edtRacha: TextView
    private var ttsSucess: Boolean = false;
    private lateinit var shareButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtConta.addTextChangedListener(this)
        edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        edtPessoas.addTextChangedListener(this)
        edtRacha = findViewById(R.id.edtRacha)
        shareButton = findViewById(R.id.ShareButton)
        shareButton.setOnClickListener {
            val texto = edtRacha.text.toString()
            if (texto.isNotBlank()) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, texto)
                    type = "text/plain"
                }

                startActivity(intent)
            }
        }
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        val valor1: Double
        val valor2: Double


        val Conta = edtConta.text.toString()
        val Pessoas = edtPessoas.text.toString()
        if(Conta.isNotEmpty() && Pessoas.isNotEmpty()) {
            valor1 = Conta.toDouble()
            valor2 = Pessoas.toDouble()
            val racha :Double = (valor1/valor2)
            Log.d("PDM24", "v: " + valor1)
            Log.d("PDM24", "v: " + valor2)
            Log.d("PMD25", "v: " + (racha))
            edtRacha.text = "Valor para cada: %.2f Reais".format(racha)
            shareButton.setOnClickListener{
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, edtRacha.text)
                    type = "text/plain"

            }
                val chooser = Intent.createChooser(intent, "Compartilhar valor do racha")
                startActivity(chooser)

        }

    }
        }

    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak(edtRacha.text, TextToSpeech.QUEUE_FLUSH, null, null)
        }




    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

