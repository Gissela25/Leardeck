package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.dsm104.ml190272sl190836.learndeck.modelo.Tematica
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AgregarTematica : AppCompatActivity() {
    private var edtTematicaNombre: EditText? = null
    private var txtTematicaHashKey: TextView? = null
    private lateinit var btnVerFichas: Button
    private var key = ""
    private var accion = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tematica)
        inicializar()
    }

    private fun inicializar() {
        edtTematicaNombre = findViewById<EditText>(R.id.edtTematicaNombre)
        txtTematicaHashKey = findViewById<TextView>(R.id.txtTematicaHashKey)
        btnVerFichas = findViewById(R.id.btnVerFichas)
        val edtTematicaNombre = findViewById<EditText>(R.id.edtTematicaNombre)
        // Se ocupar para identificar la key (el nodo) y recuperar el registro para manipularlo
        val txtTematicaHashKey = findViewById<TextView>(R.id.txtTematicaHashKey)


        // Obtención de datos que envia la actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key = datos.getString("key").toString()
            edtTematicaNombre.setText(intent.getStringExtra("nombre").toString())
            accion = datos.getString("accion").toString()
            //Ya que utilizamo el mismo acticity para agregar y actualizar. Quitamos la visibilidad
            //del boton ver fichas
            if (accion == "a") {
                btnVerFichas.visibility = View.INVISIBLE
                btnVerFichas.layoutParams.width = 0
                btnVerFichas.layoutParams.height = 0
            }
            txtTematicaHashKey.setText(intent.getStringExtra("hashkey").toString())
        }

    }

    //Nos redirigimos a las fichas de la tematicas seleccionada
    fun fichas(v: View?) {
        val intent = Intent(baseContext, FichasActivity::class.java)
        intent.putExtra("nombreTematica",txtTematicaHashKey?.text.toString())
        startActivity(intent)
    }

    fun guardar(v: View?) {
        val nombre: String = edtTematicaNombre?.text.toString().trim()
        val actualHashkey: String = txtTematicaHashKey?.text.toString().trim()
        //creamos una llave que identifique al registro
        key = UUID.randomUUID().toString()
        val nuevohashkey = key
        //hacemos una referencia
        database = FirebaseDatabase.getInstance().getReference("tematicas")

        // Se forma objeto tematica
        val tematica = Tematica(nombre, nuevohashkey)

        if (accion == "a") { //Agregar registro

            database.child(key).setValue(tematica).addOnSuccessListener {
                Toast.makeText(this, "Se guardó con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
            }
        } else  // Editar registro
        {
            val refEspecifica = database.child(actualHashkey)
            if (key == null) {
                Toast.makeText(this, "Llave vacia", Toast.LENGTH_SHORT).show()
            }
            val childUpdates = hashMapOf<String, Any>(
                "nombre" to "$nombre"
            )
            refEspecifica.updateChildren(childUpdates)
            Toast.makeText(this, "Se actualizó con exito", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    fun cancelar(v: View?) {
        val i = Intent(this, TematicaActivity::class.java)
        startActivity(i)
    }
}