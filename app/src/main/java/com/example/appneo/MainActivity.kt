package com.example.appneo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registrar_button.setOnClickListener {
            realizarRegistro()
        }

        cuenta_existente_textView.setOnClickListener {
            Log.d("MainActivity","Trying to show login Activity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun realizarRegistro(){
        val email = email_editText.text.toString()
        val password = password_editText.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Por favor completa los campos",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity","Email is: $email")
        Log.d("MainActivity","Password: $password")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Usuario registrado correctamente con id: ${it.result}")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al crear usuario: ${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("Main","Fallo al crear usuario: ${it.message}")
            }
    }
}
