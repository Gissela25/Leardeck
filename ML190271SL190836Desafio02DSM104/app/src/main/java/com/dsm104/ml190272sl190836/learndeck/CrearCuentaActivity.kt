package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        supportActionBar?.hide()

        val etnombre : TextView = findViewById(R.id.EtNombre)
        val etusuario : TextView = findViewById(R.id.EtUsuario)
        val etclave : TextView = findViewById(R.id.EtClave)
        val etclavev : TextView = findViewById(R.id.EtClaveVerificar)
        val bcrearcuenta : Button = findViewById(R.id.BCrearCuenta)

        bcrearcuenta.setOnClickListener()
        {
            var clave = etclave.text.toString()
            var clave2 = etclavev.text.toString()
            if(clave.equals(clave2)){
                crearCuenta(etnombre.text.toString(),etusuario.text.toString(),etclave.text.toString())
            } else {
                Toast.makeText(baseContext, "Error:Los passwords no coinciden", Toast.LENGTH_SHORT)
                    .show()
                etclave.requestFocus()
            }
        }
        firebaseAuth = Firebase.auth
    }
    private fun crearCuenta(nombre: String, nusuario: String, clave: String) {
        val firebasecorreo = "$nusuario@dsm.com"
        firebaseAuth.createUserWithEmailAndPassword(firebasecorreo, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Cuenta Creada Correctamente", Toast.LENGTH_SHORT)
                        .show()
                    val userId = firebaseAuth.currentUser?.uid

                    // Crear una instancia de la base de datos de Firebase
                    val database = FirebaseDatabase.getInstance()

                    // Obtener una referencia al nodo de usuarios
                    val usersRef = database.getReference("users")

                    // Crear un mapa con los detalles del usuario
                    val userDetails = mapOf(
                        "username" to nusuario,
                        "email" to firebasecorreo
                    )

                    // Guardar los detalles del usuario en la base de datos
                    userId?.let { usersRef.child(it).setValue(userDetails) }

                    // Guardar el nombre del usuario en la base de datos
                    userId?.let { usersRef.child(it).child("name").setValue(nombre) }

                    // Iniciar la actividad principal y limpiar los campos de entrada
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    findViewById<TextView>(R.id.EtNombre).setText("")
                    findViewById<TextView>(R.id.EtUsuario).setText("")
                    findViewById<TextView>(R.id.EtClave).setText("")
                    findViewById<TextView>(R.id.EtClaveVerificar).setText("")
                } else {
                    Toast.makeText(
                        baseContext,
                        "Algo Salio mal, Error:" + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}