package com.mobdeve.s13.martin.elaine.taskagotchi

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlin.math.sign

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Creating splash screen or loading screen
        Thread.sleep(3000)
        installSplashScreen()

        setContentView(R.layout.activity_login)

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
        val sign_up_tv: TextView = findViewById(R.id.sign_up_now)
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
