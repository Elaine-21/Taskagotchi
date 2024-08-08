package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterCollectionBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterCollection

class CharacterCollection : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterCollectionBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("characters")

        fetchCharacters()

        binding.encyclopediaBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun fetchCharacters() {
        database.get().addOnSuccessListener { dataSnapshot ->
            val characters = mutableListOf<CharacterCollection>()
            for (snapshot in dataSnapshot.children) {
                val character = snapshot.getValue(CharacterCollection::class.java)
                if (character != null) {
                    characters.add(character)
                }
            }

            val adapter = CharacterAdapter(characters) { character ->
                // Handle item click if needed
            }

            binding.recyclerView.layoutManager = GridLayoutManager(this, 3) // Adjust span count if needed
            binding.recyclerView.adapter = adapter
        }.addOnFailureListener {
            // Handle possible errors
        }
    }
}
