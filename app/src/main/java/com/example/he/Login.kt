package com.example.he

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.Main
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Login : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loginButton = findViewById<Button>(R.id.loginLoginButton)
        var loginBack = findViewById<ImageView>(R.id.loginReturn)
        var forgetPassword = findViewById<TextView>(R.id.loginForgetPassword)
        firebaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.loginEmail).text.toString()
            val password = findViewById<EditText>(R.id.loginPassword).text.toString()
            val loginStayLogin  =findViewById<CheckBox>(R.id.loginRememberMe)
            if(!validation(email, password)){
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {

                    if (loginStayLogin.isChecked) {

                        getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)?.edit()
                            ?.putString("user_login_status", "true")?.apply()
                    }

                    getDataFromFirebase(email)

                    val mainActivity = Intent(this,MainActivity::class.java)
                    startActivity(mainActivity)
                    finish()

                } else if (it.exception is FirebaseAuthInvalidCredentialsException || it.exception is FirebaseAuthException) {
                    Toast.makeText(this, "Invalid password!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        forgetPassword.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.dialog_forget, null)
                val userEmail = view.findViewById<EditText>(R.id.forget_editBox)

                builder.setView(view)
                val dialog = builder.create()

                view.findViewById<Button>(R.id.forget_btnReset).setOnClickListener {
                    validResetPassword(userEmail)
                    dialog.dismiss()
                }

                //cancel button in forget page
                view.findViewById<Button>(R.id.forger_btnCancel).setOnClickListener {
                    dialog.dismiss()
                }

                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                }
                dialog.show()
            }

        loginBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun validation(email : String, password : String) : Boolean{

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
            Toast.makeText(
                this,
                "Password length was more than 5 letters",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun validEmail(target: String): Boolean {
        return if (TextUtils.isEmpty(target)) false else Patterns.EMAIL_ADDRESS.matcher(target).matches()

    }

    private fun validResetPassword(email: EditText) {
        if (email.text.toString().isEmpty()) {
            return
        }
        if (!validEmail(email.text.toString())) {
            return
        }

        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataFromFirebase(email: String) {

        var reference = Firebase.database.getReference("empty")
        if (email != null) {
            val encodedEmail = email.replace(".", ",")
            reference = FirebaseDatabase.getInstance().getReference("users/$encodedEmail/information")
        }

        reference.get().addOnSuccessListener {
            if (it.exists()) {
                getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)?.edit()
                    ?.putString("username", it.child("username").value.toString())?.apply()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Server down", Toast.LENGTH_SHORT).show()
        }
    }

}