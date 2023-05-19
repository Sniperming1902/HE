package com.example.he

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginControl : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_control)

        val login_status =  getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
            ?.getString("user_login_status", "false")

        if(login_status== "true"){
            val login = Intent(this,MainActivity::class.java)
            startActivity(login)
            finish()
            return
        }

        var loginButton = findViewById<Button>(R.id.loginControlLoginButton)
        var registerButton = findViewById<Button>(R.id.loginControlRegisterButton)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.loginControlLayout, login_control_plus())
                .commit()
        }

        loginButton.setOnClickListener {
            val login = Intent(this,Login::class.java)
            startActivity(login)
        }

        registerButton.setOnClickListener {
            val register = Intent(this,Register::class.java)
            startActivity(register)
        }
    }


}