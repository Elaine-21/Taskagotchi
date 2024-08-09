package com.mobdeve.s13.martin.elaine.taskagotchi

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityLoginBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : ComponentActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Creating splash screen or loading screen
        Thread.sleep(3000)
        installSplashScreen()
        printHashKey()

        val viewBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        applyFont()


        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        viewBinding.signInBtn.setOnClickListener {
            val loginUsername = viewBinding.loginUsernameInput.text.toString()
            val loginPass = viewBinding.loginPassInput.text.toString()

            if(loginUsername.isNotEmpty() && loginPass.isNotEmpty()){
                loginUser(loginUsername, loginPass)
            }else{
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.signUpNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.e("Facebook", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Facebook", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("Facebook", "printHashKey()", e)
        }
    }
    private fun loginUser(username: String, password: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val userData = userSnapshot.getValue(UserData::class.java)
                        if(userData != null && userData.password == password){
//                            Log.d("LoginActivity", "User ID: ${userData.id}")//remove later
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            // Put the username and password into the Intent
                            intent.putExtra("username", username)
                            intent.putExtra("userId", userData.id)
                            startActivity(intent)
                            finish()
                            return
                        }

                    }
                }
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun applyFont(){
        // Getting all the preloaded font styles
        val luckiest_guy: Typeface? = ResourcesCompat.getFont(this, R.font.luckiest_guy)
        val lexend: Typeface? = ResourcesCompat.getFont(this, R.font.lexend)
        val lexend_medium: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_medium)
        val lexend_light: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_light)
        val lexend_extralight: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_extralight)

        // Applying luckiest_guy font to text views
        val welcome_tv: TextView = findViewById(R.id.welcome_tv)
        val title_tv: TextView = findViewById(R.id.title_tv)
        welcome_tv.typeface = luckiest_guy
        title_tv.typeface = luckiest_guy

        // Applying lexend_medium font to text views
        val username_tv: TextView = findViewById(R.id.login_username_tv)
        val password_tv: TextView = findViewById(R.id.login_pass_tv)
        username_tv.typeface = lexend_medium
        password_tv.typeface = lexend_medium

        // Applying lexend_light font to text views
        val login_username_input: EditText = findViewById(R.id.login_username_input)
        val login_pass_input: EditText = findViewById(R.id.login_pass_input)
        val sign_up_tv: TextView = findViewById(R.id.signUpNow)
        login_username_input.typeface = lexend_light
        login_pass_input.typeface = lexend_light
        sign_up_tv.typeface = lexend_light

        // Applying lexend_extralight font to text views
        val textView5: TextView = findViewById(R.id.textView5)
        textView5.typeface = lexend_extralight

        // Applying lexend font to button
        val signIn_btn: Button = findViewById(R.id.signIn_btn)
        signIn_btn.typeface = lexend
    }


}
