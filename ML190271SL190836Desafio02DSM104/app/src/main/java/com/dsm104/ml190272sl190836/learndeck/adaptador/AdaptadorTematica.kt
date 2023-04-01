package com.dsm104.ml190272sl190836.learndeck.adaptador

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dsm104.ml190272sl190836.learndeck.R
import com.dsm104.ml190272sl190836.learndeck.modelo.Tematica

class AdaptadorTematica(private val context: Activity, var tematicas: List<Tematica>) :
    ArrayAdapter<Tematica?>(context, R.layout.tematica_adaptador_layout, tematicas) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo
        rowview = view ?: layoutInflater.inflate(R.layout.tematica_adaptador_layout, null)
        val tvNombre = rowview!!.findViewById<TextView>(R.id.txtNombreTematica)
        val tematicaKey = rowview!!.findViewById<TextView>(R.id.tematicaKey)
        tvNombre.text =  tematicas[position].nombre
        tematicaKey.text = tematicas[position].hashkey
        return rowview
    }
}