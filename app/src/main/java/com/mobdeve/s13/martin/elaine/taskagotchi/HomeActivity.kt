package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityRegisterBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding: ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Retrieve the TextView from the layout
        val welcomeTextView: TextView = findViewById(R.id.textView)

        // Retrieve data from the Intent
        val username = intent.getStringExtra("username")
        val userId = intent.getStringExtra("userId")

        // Create a welcome message with the retrieved data
        val welcomeMessage = "WELCOME $username\nUser ID: $userId"

        // Set the welcome message to the TextView
        welcomeTextView.text = welcomeMessage

        viewBinding.addCharacterBtn.setOnClickListener{
            val intent = Intent(this@HomeActivity, CharacterCreation::class.java)

            intent.putExtra("username", username)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
    }



}