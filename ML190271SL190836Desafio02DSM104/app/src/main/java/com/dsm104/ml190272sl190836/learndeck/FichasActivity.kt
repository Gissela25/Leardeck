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
import com.dsm104.ml190272sl190836.learndeck.adaptador.AdaptadorFicha
import com.dsm104.ml190272sl190836.learndeck.modelo.Ficha
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

private var nombreTematica = ""
private lateinit var database: DatabaseReference
class FichasActivity : AppCompatActivity() {

    var fichas: MutableList<Ficha>? = null
    var listaFichas: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fichas)

        //Obteniendo los valores que fueron enviados de la actividad anterior
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
        var refFichas: DatabaseReference = database.getReference(raizNodo)
        var consultaOrdenada: Query = refFichas.orderByChild("enunciado")

        val agregarFicha: FloatingActionButton = findViewById<FloatingActionButton>(R.id.agregarFicha)
        listaFichas = findViewById<ListView>(R.id.listaFichas)

        // Cuando el usuario haga clic en la lista (para editar registro)
        listaFichas!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), AgregarFicha::class.java)
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

        // Cuando el usuario hace un LongClic (clic sin soltar elemento por mas de 2 segundos)
        // Es por que el usuario quiere eliminar el registro
        listaFichas!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                // Preparando cuadro de dialogo para preguntar al usuario
                // Si esta seguro de eliminar o no el registro
                val ad = AlertDialog.Builder(this@FichasActivity)
                ad.setMessage("Está seguro de eliminar registro?")
                    .setTitle("Confirmación")
                ad.setPositiveButton(
                    "Si"
                ) { dialog, id ->
                    fichas!![position].hashkeyFicha?.let {
                        refFichas.child(it).removeValue()
                    }

                    //var refFichaEspec: DatabaseReference = MainActivity.database.getReference("fichas")
                    //refFichaEspec.child(personas!![position] .toString().toLowerCase()).removeValue()
                    Toast.makeText(
                        this@FichasActivity,
                        "Registro borrado!", Toast.LENGTH_SHORT
                    ).show()
                }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        Toast.makeText(
                            this@FichasActivity,
                            "Operación de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                ad.show()
                return true
            }
        }
        // Agregar nuevo registro
        agregarFicha.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), AgregarFicha::class.java)
            i.putExtra("accion", "a") // Agregar
            i.putExtra("key", "")
            i.putExtra("enunciado", "")
            i.putExtra("solucion", "")
            i.putExtra("hashkeyFicha", "")
            i.putExtra("nombreTematica", nombreTematica)
            startActivity(i)
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
                val adapter = AdaptadorFicha(
                    this@FichasActivity,
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