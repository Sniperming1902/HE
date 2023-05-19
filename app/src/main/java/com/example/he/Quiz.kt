package com.example.he

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Quiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val Qadd = findViewById<ImageView>(R.id.Qadd)
        val Qminus = findViewById<ImageView>(R.id.Qminus)
        val Qmulti = findViewById<ImageView>(R.id.Qmultipulation)
        val Qdivision = findViewById<ImageView>(R.id.Qdivision)
        val Qretuen=findViewById<ImageView>(R.id.Qreturn)

        Qretuen.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        Qadd.setOnClickListener {
            val intent = Intent(this,QuizRun::class.java)
            intent.putExtra("cal","+")
            startActivity(intent)
        }

        Qminus.setOnClickListener {
            val intent = Intent(this,QuizRun::class.java)
            intent.putExtra("cal","-")
            startActivity(intent)
        }

        Qmulti.setOnClickListener {
            val intent = Intent(this,QuizRun::class.java)
            intent.putExtra("cal","x")
            startActivity(intent)
        }

        Qdivision.setOnClickListener {
            val intent = Intent(this,QuizRun::class.java)
            intent.putExtra("cal","/")
            startActivity(intent)
        }
    }

}