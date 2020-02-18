package com.example.appneo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button.setOnClickListener {
            val email = login_mail_editText.text.toString()
            val password = login_password_editText.text.toString()

            Log.d("Login","Intentando realizar login con: $email")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        }

    }
}
