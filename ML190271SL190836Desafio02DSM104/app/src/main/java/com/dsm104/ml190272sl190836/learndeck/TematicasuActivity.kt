package com.dsm104.ml190272sl190836.learndeck

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.dsm104.ml190272sl190836.learndeck.adaptador.AdaptadorTematica
import com.dsm104.ml190272sl190836.learndeck.modelo.Tematica
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class TematicasuActivity : AppCompatActivity() {

    var tematicas: MutableList<Tematica>? = null
    var consultaOrdenada: Query = TematicaActivity.refTematicas.orderByChild("nombre")
    var listaTematicas: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tematicasu)


        //A partir de estas lineas de codigo se genera la lista de tematicas en pantalla

        tematicas = ArrayList<Tematica>()

        // Cambiarlo refProductos a consultaOrdenada para ordenar lista
        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                tematicas!!.removeAll(tematicas!!)
                for (dato in dataSnapshot.getChildren()) {
                    val tematica: Tematica? = dato.getValue(Tematica::class.java)
                    tematica?.key(dato.key)
                    if (tematica != null) {
                        tematicas!!.add(tematica)
                    }
                }
                val adapter = AdaptadorTematica(
                    this@TematicasuActivity,
                    tematicas as ArrayList<Tematica>
                )
                listaTematicas!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}