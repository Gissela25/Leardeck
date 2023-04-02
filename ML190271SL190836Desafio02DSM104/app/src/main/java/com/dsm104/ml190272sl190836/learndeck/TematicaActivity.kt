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

class TematicaActivity : AppCompatActivity() {

    // Ordenamiento para hacer la consultas a los datos
    var consultaOrdenada: Query = refTematicas.orderByChild("nombre")
    var tematicas: MutableList<Tematica>? = null
    var listaTematicas: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tematica)

        inicializar()
    }

    private fun inicializar() {
        //inicializando variables
        val btnAgregarTematica: FloatingActionButton = findViewById<FloatingActionButton>(R.id.agregarTematica)
        listaTematicas = findViewById<ListView>(R.id.listaTematicas)

        // Cuando el usuario haga click en la lista (para editar registro)
        listaTematicas!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val intent = Intent(getBaseContext(), AgregarTematica::class.java)
                intent.putExtra("accion", "e") // Editar
                intent.putExtra("key", tematicas!![i].key)
                intent.putExtra("nombre", tematicas!![i].nombre)
                intent.putExtra("hashkey", tematicas!![i].hashkey)
                startActivity(intent)
            }
        })


        // Cuando el usuario hace un LongClic (clic sin soltar elemento por mas de 2 segundos)
        // Es por que el usuario quiere eliminar el registro
        listaTematicas!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                position: Int,
                l: Long
            ): Boolean {
                // Preparando cuadro de dialogo para preguntar al usuario
                // Si esta seguro de eliminar o no el registro
                val ad = AlertDialog.Builder(this@TematicaActivity)
                ad.setMessage("Está seguro de eliminar registro? Se eliminarán las bichas también.")
                    .setTitle("Confirmación")
                ad.setPositiveButton(
                    "Si"
                ) { dialog, id ->
                    tematicas!![position].hashkey?.let {
                        refTematicas.child(it).removeValue()
                    }

                    var refFichaEspec: DatabaseReference = database.getReference("fichas")
                    refFichaEspec.child(tematicas!![position].nombre.toString().toLowerCase())
                        .removeValue()
                    Toast.makeText(
                        this@TematicaActivity,
                        "Registro borrado!", Toast.LENGTH_SHORT
                    ).show()
                }
                ad.setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        Toast.makeText(
                            this@TematicaActivity,
                            "Operación de borrado cancelada!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                ad.show()
                return true
            }
        }

        // Cuando el usuario quiere agregar un nuevo registro (clic sobre el btnAgregarTematica)
        btnAgregarTematica.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), AgregarTematica::class.java)
            i.putExtra("accion", "a") // Agregar
            i.putExtra("key", "")
            i.putExtra("nombre", "")
            startActivity(i)
        })

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
                    this@TematicaActivity,
                    tematicas as ArrayList<Tematica>
                )
                listaTematicas!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        //Hasta aqui

    }

    companion object {
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var refTematicas: DatabaseReference = database.getReference("tematicas")
    }
}