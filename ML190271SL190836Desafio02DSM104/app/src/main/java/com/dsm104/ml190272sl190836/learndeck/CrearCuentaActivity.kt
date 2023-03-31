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
        val biniciars : TextView = findViewById(R.id.TVISesion)

        bcrearcuenta.setOnClickListener {
            val nombre = etnombre.text.toString().trim()
            val usuario = etusuario.text.toString().trim()
            val clave = etclave.text.toString().trim()
            val clavev = etclavev.text.toString().trim()

            if (nombre.isEmpty() || usuario.isEmpty() || clave.isEmpty() || clavev.isEmpty()) {
                Toast.makeText(baseContext, "Por favor, llene todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!nombre.matches("[a-zA-Z]+".toRegex())) {
                Toast.makeText(baseContext, "El nombre solo puede contener letras.", Toast.LENGTH_SHORT).show()
                etnombre.requestFocus()
                return@setOnClickListener
            }
            if (!usuario.matches("[a-zA-Z0-9]+".toRegex())) {
                Toast.makeText(baseContext, "El nombre de usuario solo puede contener letras y números.", Toast.LENGTH_SHORT).show()
                etusuario.requestFocus()
                return@setOnClickListener
            }
            if (clave.length < 6) {
                Toast.makeText(baseContext, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                etclave.requestFocus()
                return@setOnClickListener
            }
            if (clave != clavev) {
                Toast.makeText(baseContext, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                etclave.requestFocus()
                return@setOnClickListener
            }

            crearCuenta(nombre, usuario, clave)
        }
        biniciars.setOnClickListener()
        {
            val i = Intent(this,IniciarSesionActivity::class.java)
            startActivity(i)
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

                    val database = FirebaseDatabase.getInstance()

                    val usersRef = database.getReference("users")

                    val userDetails = mapOf(
                        "username" to nusuario,
                        "email" to firebasecorreo
                    )

                    userId?.let { usersRef.child(it).setValue(userDetails) }

                    userId?.let { usersRef.child(it).child("name").setValue(nombre) }

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