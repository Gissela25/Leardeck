package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.dsm104.ml190272sl190836.learndeck.adaptador.AdaptadorFicha
import com.dsm104.ml190272sl190836.learndeck.adaptador.AdaptadorFichasUsuario
import com.dsm104.ml190272sl190836.learndeck.modelo.Ficha
import com.google.firebase.database.*

private var nombreTematica = ""
private lateinit var database: DatabaseReference

class FichasuActivity : AppCompatActivity() {

    var fichas: MutableList<Ficha>? = null
    var listaFichas: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fichasu)

        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            nombreTematica = datos.getString("nombreTematica").toString().toLowerCase()
        }

        inicializar()
    }

    private fun inicializar() {
        //Esta variable se utiliza para llegar a la raiz del nodo que nosotros desemos
        // podria ser fichas/historia y llegar a las fichas
        var raizNodo = "fichas/${nombreTematica}"
        var refFichas: DatabaseReference = FichasActivity.database.getReference(raizNodo)
        var consultaOrdenada: Query = refFichas.orderByChild("enunciado")

        listaFichas = findViewById<ListView>(R.id.listaFichasU)

        listaFichas!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), MostrarContenidoFicha::class.java)
                intent.putExtra("accion", "e") // Agregar
                intent.putExtra("key", fichas!![i].key)
                intent.putExtra("enunciado",fichas!![i].enunciado )
                intent.putExtra("solucion", fichas!![i].solucion)
                intent.putExtra("hashkeyFicha", fichas!![i].hashkeyFicha)
                intent.putExtra("imgUrl",fichas!![i].imgUrl)
                intent.putExtra("nombreTematica", nombreTematica)
                startActivity(intent)
            }
        })

        //Impresion de la lista actualiozada el tiempo real
        fichas = ArrayList<Ficha>()

        consultaOrdenada.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Procedimiento que se ejecuta cuando hubo algun cambio
                // en la base de datos
                // Se actualiza la coleccion de personas
                fichas!!.removeAll(fichas!!)
                for (dato in dataSnapshot.getChildren()) {
                    val persona: Ficha? = dato.getValue(Ficha::class.java)
                    persona?.key(dato.key)
                    if (persona != null) {
                        fichas!!.add(persona)
                    }
                }
                val adapter = AdaptadorFichasUsuario(
                    this@FichasuActivity,
                    fichas as ArrayList<Ficha>
                )
                listaFichas!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    }
}