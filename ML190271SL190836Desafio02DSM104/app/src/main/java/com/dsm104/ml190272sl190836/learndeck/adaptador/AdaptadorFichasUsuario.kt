package com.dsm104.ml190272sl190836.learndeck.adaptador

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dsm104.ml190272sl190836.learndeck.R
import com.dsm104.ml190272sl190836.learndeck.modelo.Ficha

class AdaptadorFichasUsuario (private val context: Activity, var fichas: List<Ficha>) :
    ArrayAdapter<Ficha?>(context, R.layout.adaptador_fichasu_layout, fichas) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        // Método invocado tantas veces como elementos tenga la coleccion personas
        // para formar a cada item que se visualizara en la lista personalizada
        val layoutInflater = context.layoutInflater
        var rowview: View? = null
        // optimizando las diversas llamadas que se realizan a este método
        // pues a partir de la segunda llamada el objeto view ya viene formado
        // y no sera necesario hacer el proceso de "inflado" que conlleva tiempo y
        // desgaste de bateria del dispositivo

        rowview = view ?: layoutInflater.inflate(R.layout.adaptador_fichasu_layout, null)
        val txtEnun = rowview!!.findViewById<TextView>(R.id.txtEnunciadoFicha)
        //Traemos de nuevo la key de la ficha para poder realizar edicioines o eliminar la ficha
        val txtHashFicha = rowview!!.findViewById<TextView>(R.id.txtFichaKey)

        txtEnun.text = fichas[position].enunciado
        txtHashFicha.text = fichas[position].hashkeyFicha
        return rowview
    }
}