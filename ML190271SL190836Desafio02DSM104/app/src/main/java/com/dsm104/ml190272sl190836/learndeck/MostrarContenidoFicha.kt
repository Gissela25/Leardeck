package com.dsm104.ml190272sl190836.learndeck

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*


private var key = ""
private var accion = ""
private var nombreTematica = ""
private var sImage: String? = null
private lateinit var database: DatabaseReference
private lateinit var storage: FirebaseStorage
private lateinit var storageReference: StorageReference
private var hashparaImg = ""

private var imgFicha: ImageView? = null
private var imagenUri: Uri? = null
private val PICK_IMAGE_REQUEST = 1
private var imarefURl:String? =null

class MostrarContenidoFicha : AppCompatActivity() {

    private lateinit var txtEnun: TextView
    private lateinit var txtSln: TextView
    private lateinit var txtFichaHashKey: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_contenido_ficha)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        //
        //binding = ActivityAgregarFichaBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        hashparaImg = UUID.randomUUID().toString()
        inicializar()
    }

    fun regresar(v: View?){
        finish()
    }

    private fun inicializar() {
        txtEnun = findViewById(R.id.txtEnunFicha)
        txtSln = findViewById(R.id.txtSlnficha)
        imgFicha = findViewById(R.id.imgFicha)

        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            txtEnun.text = intent.getStringExtra("enunciado").toString()
        }
        if (datos != null) {
            txtSln.text = intent.getStringExtra("solucion").toString()
        }
        if (datos != null) {
            accion = datos.getString("accion").toString()
        }
        if (datos != null) {
            txtSln.text = intent.getStringExtra("solucion").toString()
        }
        if(datos != null)
        {
            val sImage  = datos.getString("imgUrl").toString()
            if (sImage != null && sImage!!.isNotEmpty()) {
                Picasso.get().load(sImage).into(imgFicha)
            }
        }
    }


}