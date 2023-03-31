package com.dsm104.ml190272sl190836.learndeck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val bIniciarS : Button = findViewById(R.id.BIniciarS)
        val TvCrearC : TextView = findViewById(R.id.TVRegistrarse)

        bIniciarS.setOnClickListener()
        {
            val i = Intent(this,IniciarSesionActivity::class.java)
            startActivity(i)
        }
        TvCrearC.setOnClickListener()
        {
            val i = Intent(this,CrearCuentaActivity::class.java)
            startActivity(i)
        }
    }
}