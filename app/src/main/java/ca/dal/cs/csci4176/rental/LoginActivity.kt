package ca.dal.cs.csci4176.rental

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(){
    //initialize the variables

    private lateinit var auth: FirebaseAuth

    private lateinit var loginEmailAddress: EditText
    private lateinit var loginPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        loginEmailAddress = findViewById(R.id.loginEmailAddress)
        loginPassword = findViewById(R.id.loginPassword)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            login()
        }

        val signUpButton = findViewById<Button>(R.id.loginSignUpButton)
        signUpButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val backButton = findViewById<Button>(R.id.loginBackButton)
        backButton.setOnClickListener {
            finish()
        }

    }

    private fun login() {
        //check the user input
        val email = loginEmailAddress.text.toString().trim()
        val pass = loginPassword.text.toString().trim()

        if(email.isBlank()){
            loginEmailAddress.setError("Email is required!")
            loginEmailAddress.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmailAddress.setError("Please enter a valid email!")
            loginEmailAddress.requestFocus()
            return
        }

        if(pass.isBlank()){
            loginPassword.setError("Password is required!")
            loginPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
        }
    }
}