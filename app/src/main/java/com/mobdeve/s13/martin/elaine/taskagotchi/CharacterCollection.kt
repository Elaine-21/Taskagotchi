package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterCollectionBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterCollection

class CharacterCollection : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var taskaCharacterReference: DatabaseReference
    private lateinit var binding: ActivityCharacterCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance()

        // Set up reference
        taskaCharacterReference = firebaseDatabase.getReference("taska_characters")

        // Fetch characters
        fetchCharacters()

        binding.encyclopediaBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun fetchCharacters() {
        taskaCharacterReference.get().addOnSuccessListener { dataSnapshot ->
            val characters = mutableListOf<CharacterCollection>()
            for (snapshot in dataSnapshot.children) {
                val character = snapshot.getValue(CharacterCollection::class.java)
                if (character != null) {
                    characters.add(character)
                }
            }

            // Check if no characters, add default locked ones
            if (characters.isEmpty()) {
                characters.addAll(generateDefaultCharacters())
            }

            val adapter = CharacterAdapter(characters) { character ->
                // Handle item click if needed
            }

            binding.recyclerView.layoutManager = GridLayoutManager(this, 3) // Adjust span count if needed
            binding.recyclerView.adapter = adapter
        }.addOnFailureListener {
            Log.e("CharacterCollection", "Failed to fetch characters", it)
        }
    }

    private fun generateDefaultCharacters(): List<CharacterCollection> {
        // Generate default characters if no characters are present
        val defaultCharacters = mutableListOf<CharacterCollection>()
        // Assuming you want to create a fixed number of default characters
        for (i in 1..10) {
            defaultCharacters.add(CharacterCollection(id = "default_$i", isUnlocked = false))
        }
        return defaultCharacters
    }
}

