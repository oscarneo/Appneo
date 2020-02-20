package com.example.appneo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var firebaseRemoteConfig:FirebaseRemoteConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().build()
        firebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)

        val defaultValue = HashMap<String,Any>()
        defaultValue["image_link"] = "https://lh3.googleusercontent.com/proxy/-mQox5jNUX8gpGtYbnweygjO0ljc-_2VYwK-wOF6G75uYXcZSjpxP9-5Mj5NKqXO60BUbk0K3066BQWzjIMq9rP6PmIYRm2AqLFbUAkjcSBH8LNkvNU"
        defaultValue["btn_enable"] = true
        firebaseRemoteConfig!!.setDefaultsAsync(defaultValue)

        Picasso.get().load("https://lh3.googleusercontent.com/proxy/-mQox5jNUX8gpGtYbnweygjO0ljc-_2VYwK-wOF6G75uYXcZSjpxP9-5Mj5NKqXO60BUbk0K3066BQWzjIMq9rP6PmIYRm2AqLFbUAkjcSBH8LNkvNU")
            .into(imageView)

        button_update.setOnClickListener {
            firebaseRemoteConfig!!.fetch(0) //aqui podemos configurar el tiempo en cache del resultado, si pones 0, no se cachea
                .addOnCompleteListener(this@MainActivity){task ->
                    if(task.isSuccessful){
                        firebaseRemoteConfig!!.activate() //activa todos los valores
                        button_update.isEnabled = firebaseRemoteConfig!!.getBoolean("btn_enable")
                        Picasso.get().load(firebaseRemoteConfig!!.getString("image_link"))
                            .into(imageView)
                    }else{
                        Toast.makeText(this@MainActivity,""+task.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
                }
        }
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
