package com.mobdeve.s13.martin.elaine.taskagotchi

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Creating splash screen or loading screen
        Thread.sleep(3000)
        installSplashScreen()

        setContentView(R.layout.activity_main)

        // Getting all the preloaded font styles
        val luckiest_guy: Typeface? = ResourcesCompat.getFont(this, R.font.luckiest_guy)
        val lexend_medium: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_medium)
        val lexend_light: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_light)
        val lexend_extralight: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_extralight)

        // Applying luckiest_guy font to text views
        val welcome_tv: TextView = findViewById(R.id.welcome_tv)
        val title_tv: TextView = findViewById(R.id.title_tv)
        welcome_tv.typeface = luckiest_guy
        title_tv.typeface = luckiest_guy

        // Applying lexend_medium font to text views
        val username_tv: TextView = findViewById(R.id.username_tv)
        val password_tv: TextView = findViewById(R.id.pass_tv)
        username_tv.typeface = lexend_medium
        password_tv.typeface = lexend_medium


    }


}
