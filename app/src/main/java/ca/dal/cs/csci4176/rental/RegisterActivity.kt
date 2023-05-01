package ca.dal.cs.csci4176.rental

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    //initialize the variables

    private lateinit var auth: FirebaseAuth

    private lateinit var signUpName: EditText
    private lateinit var signUpEmailAddress: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        signUpName = findViewById(R.id.signUpName)
        signUpEmailAddress = findViewById(R.id.signUpEmailAddress)
        signUpPassword = findViewById(R.id.signUpPassword)
        signUpConfirmPassword = findViewById(R.id.SignUpConfirmPassword)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener{
            signUp()
        }

        val loginButton = findViewById<Button>(R.id.signUpLoginButton)
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.signUpBackButton)
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun signUp() {
        //check the user input
        val name = signUpName.text.toString().trim()
        val email = signUpEmailAddress.text.toString().trim()
        val pass = signUpPassword.text.toString().trim()
        val confirmPassword = signUpConfirmPassword.text.toString().trim()

        if (name.isBlank()){
            signUpName.setError("Full name is required!")
            signUpName.requestFocus()
            return
        }

        if (email.isBlank()){
            signUpEmailAddress.setError("Email is required!")
            signUpEmailAddress.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmailAddress.setError("Please enter a valid email!")
            signUpEmailAddress.requestFocus()
            return
        }

        if (pass.isBlank()){
            signUpPassword.setError("Password is required!")
            signUpPassword.requestFocus()
            return
        }

        if (!isValidPassword(pass)){
            signUpPassword.setError("Password is invalid! (MUST contain at least 8 characters, at least one uppercase letter, at least one lowercase letter, at least one number, at least one special character)")
            signUpPassword.requestFocus()
            return
        }

        if (confirmPassword.isBlank()){
            signUpConfirmPassword.setError("Confirm Password is required!")
            signUpConfirmPassword.requestFocus()
            return
        }

        if (pass != confirmPassword) {
            signUpConfirmPassword.setError("Password and Confirm Password do not match")
            signUpConfirmPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val user = User(name, email)

                FirebaseAuth.getInstance().currentUser?.let { it1 ->
                    FirebaseDatabase.getInstance().getReference("Users").child(it1.uid).setValue(user)
                }

                Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Signed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //boolean method to check validation of password
    fun isValidPassword(password: String?): Boolean {
        if (password == null) {
            return false
        }
        val passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[.#?!@$%^&*_+-]).{8,}$"
        val pattern = Pattern.compile(passwordRegex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

}