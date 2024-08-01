package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterDetailsBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.HomeData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData

//import com.mobdeve.s13.martin.elaine.taskagotchi.R.layout.activity_character_details

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var characterReference: DatabaseReference
    private lateinit var taskReference: DatabaseReference
    private lateinit var taskArrayList: ArrayList<TaskData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityCharacterDetailsBinding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val characterId = this.intent.getStringExtra("characterId")
        val characterName = this.intent.getStringExtra("characterName")
        val characterPicURL = this.intent.getStringExtra("characterPicURL")
        val characterStatus = this.intent.getStringExtra("characterStatus")
        val characterStreak = this.intent.getIntExtra("characterStreak", 0)
        val characterEnergy = this.intent.getIntExtra("characterEnergy", 0)
        val characterDebuff = this.intent.getStringExtra("characterDebuff")

        Log.d("CharacterDetailsActivity", "Character ID: $characterId")
        Log.d("CharacterDetailsActivity", "Character Name: $characterName")
        Log.d("CharacterDetailsActivity", "Character Status: $characterStatus")
        Log.d("CharacterDetailsActivity", "Character Streak: $characterStreak")
        Log.d("CharacterDetailsActivity", "Character Energy: $characterEnergy")
        Log.d("CharacterDetailsActivity", "Character Debuff: $characterDebuff")

        viewBinding.taskagotchiNameCD.text = characterName ?: "No name available"
        viewBinding.taskagotchiHealthCD.text = characterStatus ?: "No status available"
        viewBinding.taskagotchiStreakCD.text = characterStreak.toString()
        viewBinding.taskagotchiEnergyCD.text = characterEnergy.toString()
        viewBinding.taskagotchiDebuffCD.text = characterDebuff ?: "No debuff available"
        characterPicURL?.let {
            val resId = resources.getIdentifier(it, "drawable", packageName)
            if (resId != 0) {
                viewBinding.taskagotchiPictureCD.setImageResource(resId)
            } else {
                showToast("Image not found")
            }
        }


//        taskArrayList = arrayListOf()
//        var taskIdCount = 0;
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        if (characterId != null) {
//            characterReference = firebaseDatabase.getReference("taskagotchiCharacter").child(characterId)
//            readData()
//        } else {
//            showToast("Character ID is null.")
//        }
    }

//    private fun readData() {
//        TODO("Not yet implemented")
//    }


    private fun showToast(message: String) {
        Toast.makeText(this@CharacterDetailsActivity, message, Toast.LENGTH_SHORT).show()
    }
}

// dont need to use the array list just bind it already to the element