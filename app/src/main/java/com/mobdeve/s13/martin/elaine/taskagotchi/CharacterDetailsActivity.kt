package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s13.martin.elaine.taskagotchi.R.layout.activity_character_details

class CharacterDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_character_details)
        val characterId = this.intent.getStringExtra("characterId")

        Log.d("CharacterDetailsActivity", "Selected Character ID: $characterId")
        Toast.makeText(this@CharacterDetailsActivity, "Character ID = $characterId", Toast.LENGTH_SHORT).show()
    }
}