package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterCollectionBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterCollection

class CharacterCollection : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a list of characters with drawable resource IDs
        val characters = listOf(
            Character(id = 1, imageResId = R.drawable.baby_blue_male, isUnlocked = true),
            Character(id = 2, imageResId = R.drawable.baby_pink_female, isUnlocked = false),
            // Add more characters here
        )

        val adapter = CharacterAdapter(characters) { character ->
            // Handle item click if needed
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3) // Adjust the span count as needed
        binding.recyclerView.adapter = adapter

        binding.encyclopediaBackBtn.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

    }
}
