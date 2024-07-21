package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mobdeve.s13.martin.elaine.taskagotchi.ui.theme.TaskagotchiTheme
import java.util.logging.Handler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Creating splash screen or loading screen
        Thread.sleep(3000)
        installSplashScreen()

        setContentView(R.layout.activity_main)

        // Applying luckiest_guy font to text views
        val welcome_tv: TextView = findViewById(R.id.welcome_tv)
        val title_tv: TextView = findViewById(R.id.title_tv)
        val luckiest_guy: Typeface? = ResourcesCompat.getFont(this, R.font.luckiest_guy)
        welcome_tv.typeface = luckiest_guy
        title_tv.typeface = luckiest_guy

        // Applying lexend_medium font to text views
        val username_tv: TextView = findViewById(R.id.username_tv)
        val lexend_medium: Typeface? = ResourcesCompat.getFont(this, R.font.lexend_medium)
        username_tv.typeface = lexend_medium


    }
}
