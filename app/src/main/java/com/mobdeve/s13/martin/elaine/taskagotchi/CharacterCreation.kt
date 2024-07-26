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
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterDifficulty
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterGender
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskagotchiData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData

class CharacterCreation : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var taskaCharacterReference: DatabaseReference


    private var selectedGender: String = "male"
    private var selectedColor: String = "blue"
    private var charDifficulty: String? = null
    private var charURLPic: String? = null
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityCharacterCreationBinding = ActivityCharacterCreationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        usersReference = firebaseDatabase.reference.child("users")
        taskaCharacterReference = firebaseDatabase.reference.child("taskagotchiCharacter")

        updateBabytaskagotchi()

        // Retrieve data from the Intent
        val username = intent.getStringExtra("username")
        val userId = intent.getStringExtra("userId")

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)


        viewBinding.createBtn.setOnClickListener{
            //Gett all the needed value
            var charDifficulty = getSelectedRadioButtonText(radioGroup)
            var charUsername = viewBinding.charUsernameInput.text.toString()
            var charURLPic = "baby_${selectedColor}_${selectedGender}"


            // Logging the values to check if they are correct
            Log.d("CharacterCreation", "Username: $charUsername")
            Log.d("CharacterCreation", "Difficulty: $charDifficulty")
            Log.d("CharacterCreation", "Character Image: $charURLPic")


            if(charUsername != "Character Username" && charDifficulty != null && userId != null && username != null){
                createCharacter(username, userId, charUsername, charDifficulty, charURLPic)
                Toast.makeText(this@CharacterCreation, "Fields are complete", Toast.LENGTH_SHORT).show()
            }else if (charUsername == "Character Username" ){
                Toast.makeText(this@CharacterCreation, "Please enter character username", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@CharacterCreation, "Please enter character difficulty", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun createCharacter(username: String, userId: String, name: String, difficulty: String, picURL: String){
        val charDifficulty: Enum<CharacterDifficulty>
        val charGender: Enum<CharacterGender>
        if(difficulty == "Beginner"){
            charDifficulty = CharacterDifficulty.BEGINNER
        }else if(difficulty == "Amateur"){
            charDifficulty = CharacterDifficulty.AMATEUR
        }else{
            charDifficulty = CharacterDifficulty.EXPERT
        }

        if(selectedGender == "male"){
            charGender = CharacterGender.MALE
        }else{
            charGender = CharacterGender.FEMALE
        }
        taskaCharacterReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                id = taskaCharacterReference.push().key
                val taskagotchiData = TaskagotchiData(id, name, charDifficulty, picURL, charGender, selectedColor)
                taskaCharacterReference.child(id!!).setValue(taskagotchiData)
                Toast.makeText(this@CharacterCreation, "Character created successfully", Toast.LENGTH_SHORT).show()
                addChartoList(userId)
                val intent = Intent(this@CharacterCreation, HomeActivity::class.java)
                // Put the username and password into the Intent
                intent.putExtra("username", username)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CharacterCreation, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun addChartoList(userId: String){
        if (userId == null || id == null) {
            // Handle error: userId or id is null
            return
        }

        // Retrieve UserData from Firebase
        usersReference.child(userId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserData::class.java)
                if (userData != null) {
                    // Update charactersIdList
                    val updatedList = userData.charactersIdList?.toMutableList() ?: mutableListOf()
                    if (!updatedList.contains(id)) {
                        updatedList.add(id!!)
                        userData.charactersIdList = updatedList

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