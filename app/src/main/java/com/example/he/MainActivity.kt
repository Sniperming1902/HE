package com.example.he

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var GoProfile = findViewById(R.id.GoProfile) as CardView
        var GoCourse = findViewById(R.id.GoCourse) as CardView
        var GoExe = findViewById(R.id.GoExe) as CardView
        var Logout = findViewById(R.id.GoPuz) as CardView
        var GoQuiz = findViewById(R.id.GoQuiz) as CardView

        findViewById<TextView>(R.id.main_username).text =
            getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
                ?.getString("username", "Username")

        GoProfile.setOnClickListener{
            val intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
        }
        GoCourse.setOnClickListener{
            val intent = Intent(this,MainActivity ::class.java)
            startActivity(intent)
        }
        GoExe.setOnClickListener{
            val intent = Intent(this,Exe ::class.java)
            startActivity(intent)
        }
        Logout.setOnClickListener{

            getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE).edit().clear().apply()

            val intent = Intent(this, LoginControl::class.java)
            startActivity(intent)
            finish()
        }
        GoQuiz.setOnClickListener{
            val intent = Intent(this,Quiz ::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        //Update
        findViewById<TextView>(R.id.main_username).text =
            getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
                ?.getString("username", "Username")
    }
}