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
    private var edtNombre: EditText? = null
    private var txtHashKey: TextView? = null
    private lateinit var btnGuardar: Button
    private var key = ""
    private var accion = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tematica)
        inicializar()
    }

    private fun inicializar() {
        edtNombre = findViewById<EditText>(R.id.edtTematicaNombre)
        txtHashKey = findViewById<TextView>(R.id.txtTematicaHashKey)
        btnGuardar = findViewById(R.id.btnGuardarT)
        val edtNombre = findViewById<EditText>(R.id.edtTematicaNombre)
        // Se ocupar para identificar la key (el nodo)
        val txtHashKey = findViewById<TextView>(R.id.txtTematicaHashKey)


        // Obtención de datos que envia la actividad anterior
        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            key = datos.getString("key").toString()
        }
        if (datos != null) {
            edtNombre.setText(intent.getStringExtra("nombre").toString())
        }
        if (datos != null) {
            accion = datos.getString("accion").toString()
            if (accion == "a") {
                btnGuardar.visibility = View.INVISIBLE
            }
        }
        if (datos != null) {
            txtHashKey.setText(intent.getStringExtra("hashkey").toString())
        }


    }

    fun fichas(v: View?) {
        val intent = Intent(baseContext, FichasActivity::class.java)
        intent.putExtra("hashkeyTematica", txtHashKey?.text.toString().toLowerCase())
        intent.putExtra("nombreTematica", edtNombre?.text.toString())
        startActivity(intent)
    }

    fun guardar(v: View?) {
        val nombre: String = edtNombre?.text.toString()
        val actualHashkey: String = txtHashKey?.text.toString()
        key = UUID.randomUUID().toString()
        val nuevohashkey = key
        database = FirebaseDatabase.getInstance().getReference("tematicas")

        // Se forma objeto tematica
        val tematica = Tematica(nombre, nuevohashkey)

        if (accion == "a") { //Agregar registro

            database.child(key).setValue(tematica).addOnSuccessListener {
                Toast.makeText(this, "Se guardo con exito", Toast.LENGTH_SHORT).show()
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
        finish()
    }
}