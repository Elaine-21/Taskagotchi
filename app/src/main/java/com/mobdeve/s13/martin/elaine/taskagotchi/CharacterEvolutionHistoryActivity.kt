package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterEvolutionHistoryBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterData

class CharacterEvolutionHistoryActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCharacterEvolutionHistoryBinding
    private lateinit var charIds: ArrayList<String>
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var characterArrayList: ArrayList<CharacterData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterEvolutionHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        characterArrayList = arrayListOf()

        charIds = intent.getStringArrayListExtra("charIds") ?: arrayListOf()
        Log.d("CharacterEvolutionHistoryActivity", "charEvolution: $charIds")

        //return button
        viewBinding.charEvoHistReturnBtn.setOnClickListener {
            finish()
        }

        viewBinding.charEvoName1.text = "???"
        viewBinding.charEvoName2.text = "???"
        viewBinding.charEvoName3.text = "???"
        viewBinding.charEvoName4.text = "???"

        firebaseDatabase = FirebaseDatabase.getInstance()
        if(charIds.isNotEmpty()){
            readCharactersData()
        }else{
            Log.d("CharacterEvolutionHistoryActivity", "No characters to fetch.")
            Log.d("CharacterEvolutionHistoryActivity", "No tasks available for this character.")
        }
    }

    private fun readCharactersData() {
        for ((index, charId) in charIds.withIndex()) {
            val charRef = firebaseDatabase.getReference("characters").child(charId)

            charRef.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val character = dataSnapshot.getValue(CharacterData::class.java)
                    character?.let {
                        characterArrayList.add(it)
                        Log.d("CharacterEvolutionHistoryActivity", "Character fetched: ${it.name}")
                        updateUI(it.name, it.picURL, index)
                    }
                } else {
                    Log.d("CharacterEvolutionHistoryActivity", "No character found for ID: $charId")
                }
            }.addOnFailureListener {
                Log.e("CharacterEvolutionHistoryActivity", "Failed to fetch character data", it)
            }
        }
    }

    private fun updateUI(characterName: String?, characterPicURL: String?, index: Int) {
        when (index) {
            0 -> {
                viewBinding.charEvoName1.text = characterName ?: "???"
                characterPicURL?.let {
                    val resId = resources.getIdentifier(it, "drawable", packageName)
                    if(resId != 0){
                        viewBinding.charEvoPic1.setImageResource(resId)
                    }
                }
            }

            1 -> {
                viewBinding.charEvoName2.text = characterName ?: "???"
                characterPicURL?.let {
                    val resId = resources.getIdentifier(it, "drawable", packageName)
                    if(resId != 0){
                        viewBinding.charEvoPic2.setImageResource(resId)
                    }
                }
            }
            2 -> {
                viewBinding.charEvoName3.text = characterName ?: "???"
                characterPicURL?.let {
                    val resId = resources.getIdentifier(it, "drawable", packageName)
                    if(resId != 0){
                        viewBinding.charEvoPic3.setImageResource(resId)
                    }
                }
            }
            3 -> {
                viewBinding.charEvoName4.text = characterName ?: "???"
                characterPicURL?.let {
                    val resId = resources.getIdentifier(it, "drawable", packageName)
                    if(resId != 0){
                        viewBinding.charEvoPic4.setImageResource(resId)
                    }
                }
            }
        }
    }


//    private fun showToast(message: String) {
//        Toast.makeText(this@CharacterEvolutionHistoryActivity, message, Toast.LENGTH_SHORT).show()
//    }
}