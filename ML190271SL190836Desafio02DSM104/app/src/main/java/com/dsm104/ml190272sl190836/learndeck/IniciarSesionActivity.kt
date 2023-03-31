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

        val etusuario : TextView = findViewById(R.id.EtUsuario)
        val etclave : TextView = findViewById(R.id.EtClave)
        val biniciar : Button = findViewById(R.id.BIniciarSesion)

        firebaseAuth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        biniciar.setOnClickListener()
        {
            val nusuario = etusuario.text.toString().trim()
            val clave = etclave.text.toString().trim()
            if (nusuario.matches("[A-Za-z0-9]+".toRegex())) {
                IniciarS(nusuario, clave)
            } else {
                Toast.makeText(baseContext, "El usuario debe contener solo letras y números", Toast.LENGTH_SHORT).show()
            }
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
                    userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val name = snapshot.child("name").value.toString()
                            if (name == "admin") {
                                val i = Intent(this@IniciarSesionActivity,AdministradorActivity::class.java)
                                startActivity(i)
                            } else {
                                val i = Intent(this@IniciarSesionActivity, UsuariosActivity::class.java)
                                startActivity(i)
                            }
                            findViewById<TextView>(R.id.EtUsuario).setText("")
                            findViewById<TextView>(R.id.EtClave).setText("")
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(baseContext, "Error de acceso a base de datos", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })
                } else {
                    Toast.makeText(baseContext, "Error de Email y/o Contrasena", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}
