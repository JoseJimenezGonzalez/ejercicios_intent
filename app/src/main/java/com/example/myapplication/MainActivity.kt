package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMandarCorreo.setOnClickListener {
            val correo = binding.etCorreo.text.toString().trim()
            sendEmail(correo)
        }

        binding.btnAbrirUrl.setOnClickListener {
            val url = binding.etUrl.text.toString().trim()
            abrirURLenNavegador(url)
        }

        binding.btnLlamar.setOnClickListener {
            val movil = binding.etTelefono.text.toString()
            llamar(movil)
        }
    }

    //Por seguridad no se permite poner nada, solo lo abre
    fun sendEmail(correo: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:$correo")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto del correo")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Cuerpo del correo")

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No hay clientes de correo instalados.", Toast.LENGTH_SHORT).show()
        }
    }

    fun abrirURLenNavegador(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun llamar(numero: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$numero")
        startActivity(intent)
    }
}