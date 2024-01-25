package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var imageView: ImageView

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = data?.data

            // Mostrar la imagen seleccionada en el ImageView
            imageView.setImageURI(selectedImageUri)

            // Guardar la imagen en el directorio de recursos 'drawable'
            selectedImageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                saveImageToDrawable(bitmap, "profile_image.png")
            }
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageBitmap = data?.extras?.get("data") as Bitmap

            // Asegúrate de que imageView esté inicializado
            if (::imageView.isInitialized) {
                // Mostrar la imagen capturada en el ImageView
                imageView.setImageBitmap(imageBitmap)

                // Puedes guardar la imagen en el directorio de recursos 'drawable' aquí si es necesario
            } else {
                // Manejar la situación cuando imageView no está inicializado
                Toast.makeText(this, "Error: imageView no está inicializado.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.ivFoto


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

        /*binding.btnFoto.setOnClickListener {
            imageView = binding.ivFoto
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(galleryIntent)
        }*/

        binding.btnFoto.setOnClickListener {
            // Abrir la cámara para capturar una imagen
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePicture.launch(captureIntent)
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

    private fun saveImageToDrawable(bitmap: Bitmap, filename: String) {
        val outputStream = openFileOutput(filename, MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()
    }
}