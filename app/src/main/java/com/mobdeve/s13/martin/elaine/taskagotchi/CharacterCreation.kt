package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterCreationBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskagotchiData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData
import java.util.Calendar
import java.util.Date

class CharacterCreation : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var taskaCharacterReference: DatabaseReference


    private var selectedGender: String = "male"
    private var selectedColor: String = "blue"
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityCharacterCreationBinding = ActivityCharacterCreationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        usersReference = firebaseDatabase.reference.child("users")

        updateBabytaskagotchi()

        // Retrieve data from the Intent
        val username = intent.getStringExtra("username")
        val userId = intent.getStringExtra("userId")

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        taskaCharacterReference = firebaseDatabase.getReference("taskagotchiCharacter/$userId")


        viewBinding.createBtn.setOnClickListener{
            //Gett all the needed value
            var charDifficulty = getSelectedRadioButtonText(radioGroup)
            var charUsername = viewBinding.charUsernameInput.text.toString()
            var charURLPic = "baby_${selectedColor}_${selectedGender}"



            if(charUsername != "Character Username" && charDifficulty != null && userId != null && username != null){
                createCharacter(username, userId, charUsername, charDifficulty, charURLPic)
//                Toast.makeText(this@CharacterCreation, "Fields are complete", Toast.LENGTH_SHORT).show()
            }else if (charUsername == "Character Username" ){
                Toast.makeText(this@CharacterCreation, "Please enter character username", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@CharacterCreation, "Please enter character difficulty", Toast.LENGTH_SHORT).show()
            }

        }


    }


    private fun charData(name: String, difficulty: String, picURL: String, creationDate: Date): TaskagotchiData {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 21)
        var levelUpDate = calendar.time

        val char = TaskagotchiData(
            id = id,
            name = name,
            difficulty = difficulty,
            picURL = picURL,
            gender = selectedGender,
            color = selectedColor,
            age = "baby",
            levelUpDate = levelUpDate,
            charEvolution = mutableListOf(),
            creationDate = creationDate
        )
        // Add picURL to the charEvolution list
        char.charEvolution?.add(picURL)
        return char
    }
    private fun createCharacter(username: String, userId: String, name: String, difficulty: String, picURL: String){

        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.MILLISECOND, 0)

        id = taskaCharacterReference.push().key
        taskaCharacterReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskagotchiData = charData(name, difficulty, picURL, date.time)
                taskaCharacterReference.child(id!!).setValue(taskagotchiData)
                Toast.makeText(this@CharacterCreation, "Character created successfully", Toast.LENGTH_SHORT).show()
                addChartoList(userId, picURL)
                val intent = Intent(this@CharacterCreation, TaskCreationActivity::class.java)
                // Put the username and password into the Intent
                intent.putExtra("username", username)
                intent.putExtra("userId", userId)
                intent.putExtra("charId", id)
                startActivity(intent)
                finish()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CharacterCreation, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addChartoList(userId: String, picURL: String){
        if (userId == null || id == null) {
            return
        }

        usersReference.child(userId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("CharacterCreation", "DataSnapshot: ${snapshot.value}")
                val userData = snapshot.getValue(UserData::class.java)
                if (userData != null) {

                    val updatedList = userData.charactersIdList?.toMutableList() ?: mutableListOf()
                    val unlockedCharList = userData.unlockedIDChar?.toMutableList()?: mutableListOf()
                    if (!updatedList.contains(id)) {
                        updatedList.add(id!!)
                        unlockedCharList.add(picURL)
                        userData.charactersIdList = updatedList
                        userData.unlockedIDChar = unlockedCharList

                        // Write updated UserData back to Firebase
                        usersReference.child(userId!!).setValue(userData)

                    }
                } else {
                    Toast.makeText(this@CharacterCreation, "User Error", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CharacterCreation, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }


    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String? {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        if (checkedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
            return selectedRadioButton.text.toString()
        }
        return null
    }


    //This makes the buttons functional
    private fun updateBabytaskagotchi(){

        val characterImageView: ImageView = findViewById(R.id.characterUrl_imv)
        val femaleButton: Button = findViewById(R.id.female_btn)
        val maleButton: Button = findViewById(R.id.male_btn)
        val greenButton: Button = findViewById(R.id.green_btn)
        val pinkButton: Button = findViewById(R.id.pink_btn)
        val blueButton: Button = findViewById(R.id.blue_btn)

        femaleButton.setOnClickListener {
            selectedGender = "female"
            updateCharacterImage(characterImageView)
        }

        maleButton.setOnClickListener {
            selectedGender = "male"
            updateCharacterImage(characterImageView)
        }

        greenButton.setOnClickListener {
            selectedColor = "green"
            updateCharacterImage(characterImageView)
        }

        pinkButton.setOnClickListener {
            selectedColor = "pink"
            updateCharacterImage(characterImageView)
        }

        blueButton.setOnClickListener {
            selectedColor = "blue"
            updateCharacterImage(characterImageView)
        }

        // Initialize with default image
        updateCharacterImage(characterImageView)
    }

    //This change the image of the baby tamagotchi in the character creation page
    private fun updateCharacterImage(imageView: ImageView) {
        val drawableName = "baby_${selectedColor}_${selectedGender}"
        val resId = resources.getIdentifier(drawableName, "drawable", packageName)
        if (resId != 0) {
            imageView.setImageDrawable(ContextCompat.getDrawable(this, resId))
        } else {
            // Fallback to a default image or handle the error
            imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baby_blue_male))
        }
    }



}