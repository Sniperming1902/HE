package com.example.he

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Register : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var registerButton = findViewById<Button>(R.id.registerRegister)
        var registerBack = findViewById<ImageView>(R.id.registerReturn)

        firebaseAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
            val username = findViewById<EditText>(R.id.registerUsername).text.toString()
            val email = findViewById<EditText>(R.id.registerEmail).text.toString()
            val password = findViewById<EditText>(R.id.registerPassword).text.toString()
            val repassword = findViewById<EditText>(R.id.registerPassword2).text.toString()

            if(!validation(username, email, password, repassword)){
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    saveToFirebase(email, username)
                    Toast.makeText(this,"Email Is Registered!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginControl::class.java)
                    startActivity(intent)
                } else if(it.exception is FirebaseAuthUserCollisionException) {
                    Toast.makeText(this,"Provided Email Is Already Registered!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        registerBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun validation(username : String, email : String, password : String, repassword : String) : Boolean{

        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (username.length !in 4..10) {
            Toast.makeText(this, "Username need to in between 4 to 10 letters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validEmail(email)) {
            Toast.makeText(baseContext, "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password length was more than 5 letters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != repassword) {
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validEmail(target: String): Boolean {
        return if (TextUtils.isEmpty(target)) false else Patterns.EMAIL_ADDRESS.matcher(target).matches()

    }

    private fun saveToFirebase(email : String, username : String) {

        // Firebase database
        val date:String = getCurrentDate()
        var reference = Firebase.database.getReference("empty")
        if (email != null) {
            val encodedEmail = email.replace(".", ",")

            // Write data to the database under the user's email key
            reference = Firebase.database.getReference("users/$encodedEmail/information")
        }
        val User = User(username, date)
        reference.setValue(User).addOnCompleteListener { task ->
            if (task.isSuccessful) {

            } else {
                Toast.makeText(this, "Fail to upload data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().toString()
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.format(Date())
        }
        return currentDate
    }

    class User(val username : String? = null, val dateCreate : String? = null){

    }
}