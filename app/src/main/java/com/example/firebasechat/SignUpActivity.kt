package com.example.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val btnSignUp = findViewById<Button>(R.id.btnSignup)
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassord = findViewById<EditText>(R.id.etConfirmPassword)

        btnSignUp.setOnClickListener{
            val userName = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassord.text.toString()

            if(TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext, "Username é necessário", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext, "Email é necessário", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "Password é necessário", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext, "Confirmar Password é necessário", Toast.LENGTH_SHORT).show()
            }
            if(password != confirmPassword){
                Toast.makeText(applicationContext, "Os dois campos precisam coincidir", Toast.LENGTH_SHORT).show()
            }

            registerUser(userName,email,password)
        }

    }

    private fun registerUser(userName:String, email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        var user: FirebaseUser? = auth.currentUser
                        var userId: String = user!!.uid

                        databaseReference = FirebaseDatabase
                                .getInstance()
                                .getReference("User")
                                .child(userId)

                        var hashMap:HashMap<String, String> = HashMap()
                        hashMap.put("userId", userId)
                        hashMap.put("userName", userName)
                        hashMap.put("profileImage", "")

                        databaseReference.setValue(hashMap).addOnCompleteListener(this){
                            if(it.isSuccessful){
                                var intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
    }

}