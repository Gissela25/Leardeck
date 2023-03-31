package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class IniciarSesionActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        supportActionBar?.hide()

        val etusuario: TextView = findViewById(R.id.EtUsuario)
        val etclave: TextView = findViewById(R.id.EtClave)
        val biniciar: Button = findViewById(R.id.BIniciarSesion)
        val bCuenta: TextView = findViewById(R.id.TVCrearC)

        firebaseAuth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        biniciar.setOnClickListener()
        {
            val nusuario = etusuario.text.toString().trim()
            val clave = etclave.text.toString().trim()

            if (nusuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(baseContext, "Por favor, llene todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!nusuario.matches("[a-zA-Z0-9]+".toRegex())) {
                Toast.makeText(baseContext, "El nombre de usuario solo puede contener letras y números.", Toast.LENGTH_SHORT).show()
                etusuario.requestFocus()
                return@setOnClickListener
            }
            if (clave.length < 6) {
                Toast.makeText(baseContext, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                etclave.requestFocus()
                return@setOnClickListener
            }
            IniciarS(etusuario.text.toString(),etclave.text.toString())
        }
        bCuenta.setOnClickListener()
        {
            val i = Intent(this, CrearCuentaActivity::class.java)
            startActivity(i)
        }
    }

    private fun IniciarS(nusuario: String, clave: String) {
        val correo = "${nusuario}@dsm.com"
        firebaseAuth.signInWithEmailAndPassword(correo, clave)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val uid = user!!.uid
                    val userRef = database.getReference("users").child(uid)
                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val name = snapshot.child("name").value.toString()
                            if (name == "admin") {
                                val i = Intent(
                                    this@IniciarSesionActivity,
                                    AdministradorActivity::class.java
                                )
                                startActivity(i)
                            } else {
                                val i =
                                    Intent(this@IniciarSesionActivity, UsuariosActivity::class.java)
                                startActivity(i)
                            }
                            findViewById<TextView>(R.id.EtUsuario).setText("")
                            findViewById<TextView>(R.id.EtClave).setText("")
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                baseContext,
                                "Error de acceso a base de datos",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    })
                } else {
                    Toast.makeText(baseContext, "Error de Usuario y/o Contraseña", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}

