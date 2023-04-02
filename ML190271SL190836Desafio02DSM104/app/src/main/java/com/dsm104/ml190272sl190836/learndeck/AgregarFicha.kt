package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dsm104.ml190272sl190836.learndeck.modelo.Ficha
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
private var edtEnun: EditText? = null
private var edtSln: EditText? = null
private var txtFichaHashKey: TextView? = null
private var imgFicha: ImageView? = null
private var imagenUri: Uri? = null
private val PICK_IMAGE_REQUEST = 1
private var imarefURl:String? =null

class AgregarFicha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_ficha)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        //
        //binding = ActivityAgregarFichaBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        hashparaImg = UUID.randomUUID().toString()
        inicializar()
        //
    }

    private fun inicializar() {
        edtEnun = findViewById<EditText>(R.id.edtEnunFicha)
        edtSln = findViewById<EditText>(R.id.edtSlnFicha)
        txtFichaHashKey = findViewById<TextView>(R.id.txtfichaHashKey)
        imgFicha = findViewById(R.id.imgFicha)
        val edtEnum = findViewById<EditText>(R.id.edtEnunFicha)
        val edtSln = findViewById<EditText>(R.id.edtSlnFicha)
        val txtFichaHashKey = findViewById<TextView>(R.id.txtfichaHashKey)
        // Se ocupar para identificar la key (el nodo)


        // Obtención de datos que envia actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key = datos.getString("key").toString()
        }
        if (datos != null) {
            edtEnum.setText(intent.getStringExtra("enunciado").toString())
        }
        if (datos != null) {
            edtSln.setText(intent.getStringExtra("solucion").toString())
        }
        if (datos != null) {
            accion = datos.getString("accion").toString()
        }
        if (datos != null) {
            nombreTematica = datos.getString("nombreTematica").toString()
        }
        if (datos != null) {
            txtFichaHashKey.setText(intent.getStringExtra("hashkeyFicha").toString())
        }
        if(datos != null)
        {
            if (sImage != null && sImage!!.isNotEmpty()) {
                Picasso.get().load(sImage).into(imgFicha)
            }
        }

    }


    fun guardar(v: View?) {
        val enunciado: String = edtEnun?.text.toString().trim()
        val solucion: String = edtSln?.text.toString().trim()
        val actualHashkeyFicha: String = txtFichaHashKey?.text.toString()
        key = UUID.randomUUID().toString()
        val nuevohashkeyFicha = key
        var raiz = "fichas/$nombreTematica"
        database = FirebaseDatabase.getInstance().getReference(raiz)


        if (accion == "a") { //Agregar registro
            val ficha = Ficha(enunciado, solucion, nuevohashkeyFicha, imarefURl )
            database.child(key).setValue(ficha).addOnSuccessListener {
                Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
        } else  // Editar registro
        {
            val temporalUrl = "https://www.adobe.com/es/express/feature/image/media_16ad2258cac6171d66942b13b8cd4839f0b6be6f3.png?width=750&format=png&optimize=medium"
            val ficha = Ficha(enunciado, solucion, nuevohashkeyFicha, temporalUrl )
            val refEspecifica = database.child(actualHashkeyFicha)
            if (key == null) {
                Toast.makeText(this, "Llave vacia", Toast.LENGTH_SHORT).show()
            }
            val childUpdates = hashMapOf<String, Any>(
                "enunciado" to "$enunciado",
                "solucion" to "$solucion"
            )

            if (imagenUri != null) { // Si se seleccionó una imagen, guardarla
                val storageRef = FirebaseStorage.getInstance().reference.child("fichas/$nombreTematica/$hashparaImg.jpg")
                storageRef.putFile(imagenUri!!)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            childUpdates["imagen"] = uri.toString() // Agregar la URL de la imagen en childUpdates
                            refEspecifica.updateChildren(childUpdates)
                            Toast.makeText(this, "Se actualizo con exito", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                    }
            } else { // Si no se seleccionó una imagen, actualizar sin imagen
                refEspecifica.updateChildren(childUpdates)
                Toast.makeText(this, "Se actualizo con exito", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    fun cancelar(v: View?) {
        finish()
    }
    fun seleccionarImg(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imgUri = data?.data
            storage = FirebaseStorage.getInstance()
            storageReference = storage.reference
            val fileRef: StorageReference
            if (accion == "a") {
                fileRef = storageReference.child("fichas/$nombreTematica/$hashparaImg.jpg")
            } else if (accion == "e") {
                fileRef = storageReference.child("images_case_e/${UUID.randomUUID()}")
            } else {
                // handle unknown request code
                return
            }

            imgUri?.let {
                fileRef.putFile(it)
                    .addOnSuccessListener {
                        // Imagen cargada con éxito, ahora puedes obtener la URL de descarga.
                        fileRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                // Aquí se carga la imagen en ImageView usando Picasso.
                                imarefURl = uri.toString()
                                Picasso.get().load(uri.toString()).into(imgFicha)
                            }
                    }
                    .addOnFailureListener {
                        // Error al cargar la imagen.
                        Toast.makeText(this, "Error al cargar la imagen.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

}