package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterEvolutionBinding
import java.util.Calendar
import java.util.Date

class CharacterEvolutionActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCharacterEvolutionBinding
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var characterStreak: Int? = null
    private var userId: String? = null
    private var characterId: String? = null
    private var characterAge: String? = null
    private var updatedAge: String? = null
    private var characterGender: String? = null
    private var charPicURL: String? = null
    private var charName: String? = null
    private var eggGroup: String? = null
    private var selectedImageName: String? = null
    private var selectedImageView: ImageView? = null
    private var charEvolution: MutableList<String?> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterEvolutionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        userId = this.intent.getStringExtra("userID")
        characterId = this.intent.getStringExtra("characterID")
        characterStreak = this.intent.getIntExtra("characterStreak", 0)
        charPicURL = this.intent.getStringExtra("charPicURL")
        charName = this.intent.getStringExtra("characterName")

        charPicURL?.let {
            val resId = resources.getIdentifier(it, "drawable", packageName)
            if (resId != 0) {
                viewBinding.currentCharacterImage.setImageResource(resId)
            } else {
                showToast("Image not found")
            }
        }

        viewBinding.currentCharacterName.text = charName?: "No name available"

        getCharDetails()

        viewBinding.evolveButton.setOnClickListener {
            updateCharacter()
            updateUnlockedChar()
            finish()
        }
    }

    private fun checkStreak(){
        if(characterAge =="baby"){
            if(characterStreak!! == 3){
                updateView()
            }else{
                updateViewWithoutStreak()
                generateRandomChar()
            }
        }else if(characterAge =="child"){
            if(characterStreak!! >= 5){
                updateView()
            }else{
                updateViewWithoutStreak()
                generateRandomChar()
            }
        }else if(characterAge =="teen"){
            if(characterStreak!! >= 8){
                updateView()
            }else{
                updateViewWithoutStreak()
                generateRandomChar()
            }
        }
    }

    private fun generateRandomChar(){
        val randomCharBabyChild = (1 until 4).random()
        val randomCharTeen = (1 until 5).random()

        Log.d("Random Char", "randomCharBabyChild: ${randomCharBabyChild}")
        Log.d("Random Char", "randomCharTeen: ${randomCharTeen}")
        if(characterAge == "baby" && characterGender =="female"){
            if(randomCharBabyChild== 1){
                selectedImageName = "child_female_tantotchi"
            }else if(randomCharBabyChild == 2){
                selectedImageName = "child_female_chiroritchi"
            }else if(randomCharBabyChild == 3){
                selectedImageName = "child_female_mimitamatchi"
            }
        }else if(characterAge == "child" && characterGender =="female"){
            viewBinding.char1Imv.setImageResource(R.drawable.teen_female_haretchi)
            viewBinding.char2Imv.setImageResource(R.drawable.teen_female_tororitchi)
            viewBinding.char3Imv.setImageResource(R.drawable.teen_female_soyofuwatchi)
            if(randomCharBabyChild== 1){
                selectedImageName = "teen_female_haretchi"
            }else if(randomCharBabyChild == 2){
                selectedImageName = "teen_female_tororitchi"
            }else if(randomCharBabyChild == 3){
                selectedImageName = "teen_female_soyofuwatchi"
            }
        }else if(characterAge == "teen" && characterGender =="female"){
            if(eggGroup == "blue"){
                if(randomCharTeen== 1){
                    selectedImageName = "adult_blue_female_himetchi"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_blue_female_mimitchim"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_blue_female_awamokotchi"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_blue_female_shinobinyatchimix"
                }
            }else if(eggGroup == "green"){
                if(randomCharTeen== 1){
                    selectedImageName = "adult_green_female_cofret"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_green_female_neliatchi"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_green_female_violetchi"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_green_female_memetchimix"
                }
            }else if(eggGroup == "pink"){
                if(randomCharTeen== 1){
                    selectedImageName = "adult_pink_female_sebiretchi"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_pink_female_chamametchi"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_pink_female_momotchimix"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_pink_female_lovelitchimix"
                }
            }
        }else if(characterAge == "baby" && characterGender =="male"){
            if(randomCharBabyChild== 1){
                selectedImageName = "child_male_fuyofuyotchi"
            }else if(randomCharBabyChild == 2){
                selectedImageName = "child_male_mokumokutchi"
            }else if(randomCharBabyChild == 3){
                selectedImageName = "child_male_puchitomatchi"
            }
        }else if(characterAge == "child" && characterGender =="male"){
            if(randomCharBabyChild== 1){
                selectedImageName = "teen_male_mokokotchi"
            }else if(randomCharBabyChild == 2){
                selectedImageName = "teen_male_kurupoyotchi"
            }else if(randomCharBabyChild == 3){
                selectedImageName = "teen_male_terukerotchi"
            }
        }else if(characterAge == "teen" && characterGender =="male"){
            if(eggGroup == "blue"){
                if(randomCharTeen== 1){
                    selectedImageName = "adult_blue_male_gozarutchi"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_blue_male_kikitchimix"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_blue_male_mametchimix"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_blue_male_kuromametchi"
                }
            }else if(eggGroup == "green"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_green_male_weeptchi)
                viewBinding.char2Imv.setImageResource(R.drawable.adult_green_male_paintotchi)
                viewBinding.char3Imv.setImageResource(R.drawable.adult_green_male_murachakitchi)
                viewBinding.char4Imv.setImageResource(R.drawable.adult_green_male_shimagurutchimix)
                if(randomCharTeen== 1){
                    selectedImageName = "adult_green_male_weeptchi"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_green_male_paintotchi"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_green_male_murachakitchi"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_green_male_shimagurutchimix"
                }
            }else if(eggGroup == "pink"){
                if(randomCharTeen== 1){
                    selectedImageName = "adult_pink_male_charatchi"
                }else if(randomCharTeen == 2){
                    selectedImageName = "adult_pink_male_orenetchi"
                }else if(randomCharTeen == 3){
                    selectedImageName = "adult_pink_male_ginjirotchi"
                }else if(randomCharTeen ==4){
                    selectedImageName = "adult_pink_male_kuchipatchim"
                }
            }
        }
    }
    private fun updateViewWithoutStreak(){
        viewBinding.char1Tv.visibility = View.INVISIBLE
        viewBinding.char2Tv.visibility = View.INVISIBLE
        viewBinding.char3Tv.visibility = View.INVISIBLE
        viewBinding.char4Tv.visibility = View.INVISIBLE

        if(characterAge == "baby" && characterGender =="female"){
            viewBinding.char1Imv.visibility = View.INVISIBLE
            viewBinding.char2Imv.visibility = View.INVISIBLE
            viewBinding.char3Imv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE
            updatedAge = "child"
        }else if(characterAge == "child" && characterGender =="female"){
            viewBinding.char1Imv.visibility = View.INVISIBLE
            viewBinding.char2Imv.visibility = View.INVISIBLE
            viewBinding.char3Imv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            updatedAge = "teen"
        }else if(characterAge == "teen" && characterGender =="female"){
            if(eggGroup == "blue"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE

            }else if(eggGroup == "green"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE


            }else if(eggGroup == "pink"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE


            }
            updatedAge = "adult"
        }else if(characterAge == "baby" && characterGender =="male"){
            viewBinding.char1Imv.visibility = View.INVISIBLE
            viewBinding.char2Imv.visibility = View.INVISIBLE
            viewBinding.char3Imv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            updatedAge = "child"
        }else if(characterAge == "child" && characterGender =="male"){
            viewBinding.char1Imv.visibility = View.INVISIBLE
            viewBinding.char2Imv.visibility = View.INVISIBLE
            viewBinding.char3Imv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            updatedAge = "teen"
        }else if(characterAge == "teen" && characterGender =="male"){
            if(eggGroup == "blue"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE

            }else if(eggGroup == "green"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE


            }else if(eggGroup == "pink"){
                viewBinding.char1Imv.visibility = View.INVISIBLE
                viewBinding.char2Imv.visibility = View.INVISIBLE
                viewBinding.char3Imv.visibility = View.INVISIBLE
                viewBinding.char4Imv.visibility = View.INVISIBLE

            }
            updatedAge = "adult"
        }
    }

    private fun getCharDetails(){
        val taskagotchiDataReference: DatabaseReference =
            firebaseDatabase.reference.child("taskagotchiCharacter/$userId/$characterId")

        taskagotchiDataReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                characterAge = dataSnapshot.child("age").getValue(String::class.java)
                characterGender = dataSnapshot.child("gender").getValue(String::class.java)
                eggGroup = dataSnapshot.child("color").getValue(String::class.java)
                charEvolution = dataSnapshot.child("charEvolution")
                    .getValue(object : GenericTypeIndicator<MutableList<String?>>() {}) ?: mutableListOf()

                checkStreak()
            }
            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })

    }

    private fun updateCharacter(){
        val updatedLevelUpDate = Calendar.getInstance()
        updatedLevelUpDate.set(Calendar.HOUR_OF_DAY, 0)
        updatedLevelUpDate.set(Calendar.MINUTE, 0)
        updatedLevelUpDate.set(Calendar.SECOND, 0)
        updatedLevelUpDate.set(Calendar.MILLISECOND, 0)

        val taskagotchiDataReference: DatabaseReference =
            firebaseDatabase.reference.child("taskagotchiCharacter/$userId")

        if(characterAge == "baby"){
            updatedLevelUpDate.add(Calendar.DAY_OF_YEAR, 14)
        }else if(characterAge == "child"){
            updatedLevelUpDate.add(Calendar.DAY_OF_YEAR, 21)
        }else if(characterAge == "teen"){
            updatedLevelUpDate.add(Calendar.DAY_OF_YEAR, 56)
        }

        charEvolution.add(selectedImageName)
        val updateChar = mapOf(
            "age" to updatedAge,
            "picURL" to selectedImageName,
            "levelUpDate" to updatedLevelUpDate.time,
            "charEvolution" to charEvolution
        )

        Log.d("Character Evolution Update", "Age: ${updatedAge}\nPicURL: ${selectedImageName}\n" +
                "levelUpDate: ${updatedLevelUpDate.time}\n charEvolution ${charEvolution}")
        taskagotchiDataReference.child(characterId!!).updateChildren(updateChar)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Evolution", "Character updated")
                } else {
                    Log.d("Character Evolution", "Character failed to update")
                }
            }


    }

    private fun updateUnlockedChar() {
        val userReference: DatabaseReference =
            firebaseDatabase.reference.child("users/$userId")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var unlockedIDChar = dataSnapshot.child("unlockedIDChar").getValue(object : GenericTypeIndicator<MutableList<String>>() {}) ?: mutableListOf()

                if (!unlockedIDChar!!.contains(selectedImageName)) {
                    unlockedIDChar!!.add(selectedImageName!!)

                    val updateUser = mapOf(
                        "unlockedIDChar" to unlockedIDChar
                    )
                    userReference.updateChildren(updateUser)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Unlocked Char Update", "unlockedIDChar updated")
                            } else {
                                Log.d("Unlocked Char Update", "unlockedIDChar failed to update")
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }
    //updates the view if the user has complete streaks
    private fun updateView(){
        Log.d("Character Evolution", "Update View is called")
        Log.d("Character Evolution", "Age: ${characterAge}\nGender:${characterGender}")

        if(characterAge == "baby" && characterGender =="female"){
            viewBinding.char1Imv.setImageResource(R.drawable.child_female_tantotchi)
            getCharName("child_female_tantotchi", viewBinding.char1Tv)

            viewBinding.char2Imv.setImageResource(R.drawable.child_female_chiroritchi)
            getCharName("child_female_chiroritchi", viewBinding.char2Tv)

            viewBinding.char3Imv.setImageResource(R.drawable.child_female_mimitamatchi)
            getCharName("child_female_mimitamatchi", viewBinding.char3Tv)

            viewBinding.char4Tv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            setClickListener(viewBinding.char1Imv, "child_female_tantotchi")
            setClickListener(viewBinding.char2Imv, "child_female_chiroritchi")
            setClickListener(viewBinding.char3Imv, "child_female_mimitamatchi")
            updatedAge = "child"
        }else if(characterAge == "child" && characterGender =="female"){
            viewBinding.char1Imv.setImageResource(R.drawable.teen_female_haretchi)
            getCharName("teen_female_haretchi", viewBinding.char1Tv)

            viewBinding.char2Imv.setImageResource(R.drawable.teen_female_tororitchi)
            getCharName("teen_female_tororitchi", viewBinding.char2Tv)

            viewBinding.char3Imv.setImageResource(R.drawable.teen_female_soyofuwatchi)
            getCharName("teen_female_soyofuwatchi", viewBinding.char3Tv)

            viewBinding.char4Tv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            setClickListener(viewBinding.char1Imv, "teen_female_haretchi")
            setClickListener(viewBinding.char2Imv, "teen_female_tororitchi")
            setClickListener(viewBinding.char3Imv, "teen_female_soyofuwatchi")
            updatedAge = "teen"
        }else if(characterAge == "teen" && characterGender =="female"){
            if(eggGroup == "blue"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_blue_female_himetchi)
                getCharName("adult_blue_female_himetchi", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_blue_female_mimitchim)
                getCharName("adult_blue_female_mimitchim", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_blue_female_awamokotchi)
                getCharName("adult_blue_female_awamokotchi", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_blue_female_shinobinyatchimix)
                getCharName("adult_blue_female_shinobinyatchimix", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_blue_female_himetchi")
                setClickListener(viewBinding.char2Imv, "adult_blue_female_mimitchim")
                setClickListener(viewBinding.char3Imv, "adult_blue_female_awamokotchi")
                setClickListener(viewBinding.char4Imv, "adult_blue_female_shinobinyatchimix")

            }else if(eggGroup == "green"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_green_female_cofret)
                getCharName("adult_green_female_cofret", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_green_female_neliatchi)
                getCharName("adult_green_female_neliatchi", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_green_female_violetchi)
                getCharName("adult_green_female_violetchi", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_green_female_memetchimix)
                getCharName("adult_green_female_memetchimix", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_green_female_cofret")
                setClickListener(viewBinding.char2Imv, "adult_green_female_neliatchi")
                setClickListener(viewBinding.char3Imv, "adult_green_female_violetchi")
                setClickListener(viewBinding.char4Imv, "adult_green_female_memetchimix")

            }else if(eggGroup == "pink"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_pink_female_sebiretchi)
                getCharName("adult_pink_female_sebiretchi", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_pink_female_chamametchi)
                getCharName("adult_pink_female_chamametchi", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_pink_female_momotchimix)
                getCharName("adult_pink_female_momotchimix", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_pink_female_lovelitchimix)
                getCharName("adult_pink_female_lovelitchimix", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_pink_female_sebiretchi")
                setClickListener(viewBinding.char2Imv, "adult_pink_female_chamametchi")
                setClickListener(viewBinding.char3Imv, "adult_pink_female_momotchimix")
                setClickListener(viewBinding.char4Imv, "adult_pink_female_lovelitchimix")

            }
            updatedAge = "adult"
        }else if(characterAge == "baby" && characterGender =="male"){
            viewBinding.char1Imv.setImageResource(R.drawable.child_male_fuyofuyotchi)
            getCharName("child_male_fuyofuyotchi", viewBinding.char1Tv)

            viewBinding.char2Imv.setImageResource(R.drawable.child_male_mokumokutchi)
            getCharName("child_male_mokumokutchi", viewBinding.char2Tv)

            viewBinding.char3Imv.setImageResource(R.drawable.child_male_puchitomatchi)
            getCharName("child_male_puchitomatchi", viewBinding.char3Tv)

            viewBinding.char4Tv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            setClickListener(viewBinding.char1Imv, "child_male_fuyofuyotchi")
            setClickListener(viewBinding.char2Imv, "child_male_mokumokutchi")
            setClickListener(viewBinding.char3Imv, "child_male_puchitomatchi")
            updatedAge = "child"
        }else if(characterAge == "child" && characterGender =="male"){
            viewBinding.char1Imv.setImageResource(R.drawable.teen_male_mokokotchi)
            getCharName("teen_male_mokokotchi", viewBinding.char1Tv)

            viewBinding.char2Imv.setImageResource(R.drawable.teen_male_kurupoyotchi)
            getCharName("teen_male_kurupoyotchi", viewBinding.char2Tv)

            viewBinding.char3Imv.setImageResource(R.drawable.teen_male_terukerotchi)
            getCharName("teen_male_terukerotchi", viewBinding.char3Tv)

            viewBinding.char4Tv.visibility = View.INVISIBLE
            viewBinding.char4Imv.visibility = View.INVISIBLE

            setClickListener(viewBinding.char1Imv, "teen_male_mokokotchi")
            setClickListener(viewBinding.char2Imv, "teen_male_kurupoyotchi")
            setClickListener(viewBinding.char3Imv, "teen_male_terukerotchi")
            updatedAge = "teen"
        }else if(characterAge == "teen" && characterGender =="male"){
            if(eggGroup == "blue"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_blue_male_gozarutchi)
                getCharName("adult_blue_male_gozarutchi", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_blue_male_kikitchimix)
                getCharName("adult_blue_male_kikitchimix", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_blue_male_mametchimix)
                getCharName("adult_blue_male_mametchimix", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_blue_male_kuromametchi)
                getCharName("adult_blue_male_kuromametchi", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_blue_male_gozarutchi")
                setClickListener(viewBinding.char2Imv, "adult_blue_male_kikitchimix")
                setClickListener(viewBinding.char3Imv, "adult_blue_male_mametchimix")
                setClickListener(viewBinding.char4Imv, "adult_blue_male_kuromametchi")

            }else if(eggGroup == "green"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_green_male_weeptchi)
                getCharName("adult_green_male_weeptchi", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_green_male_paintotchi)
                getCharName("adult_green_male_paintotchi", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_green_male_murachakitchi)
                getCharName("adult_green_male_murachakitchi", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_green_male_shimagurutchimix)
                getCharName("adult_green_male_shimagurutchimix", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_green_male_weeptchi")
                setClickListener(viewBinding.char2Imv, "adult_green_male_paintotchi")
                setClickListener(viewBinding.char3Imv, "adult_green_male_murachakitchi")
                setClickListener(viewBinding.char4Imv, "adult_green_male_shimagurutchimix")

            }else if(eggGroup == "pink"){
                viewBinding.char1Imv.setImageResource(R.drawable.adult_pink_male_charatchi)
                getCharName("adult_pink_male_charatchi", viewBinding.char1Tv)

                viewBinding.char2Imv.setImageResource(R.drawable.adult_pink_male_orenetchi)
                getCharName("adult_pink_male_orenetchi", viewBinding.char2Tv)

                viewBinding.char3Imv.setImageResource(R.drawable.adult_pink_male_ginjirotchi)
                getCharName("adult_pink_male_ginjirotchi", viewBinding.char3Tv)

                viewBinding.char4Imv.setImageResource(R.drawable.adult_pink_male_kuchipatchim)
                getCharName("adult_pink_male_kuchipatchim", viewBinding.char4Tv)

                setClickListener(viewBinding.char1Imv, "adult_pink_male_charatchi")
                setClickListener(viewBinding.char2Imv, "adult_pink_male_orenetchi")
                setClickListener(viewBinding.char3Imv, "adult_pink_male_ginjirotchi")
                setClickListener(viewBinding.char4Imv, "adult_pink_male_kuchipatchim")
            }
            updatedAge = "adult"
        }
    }


    private fun getCharName(picURL: String?, textView: TextView){
        val character: DatabaseReference =
            firebaseDatabase.reference.child("characters/$picURL")

        character.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").getValue(String::class.java)
                textView.text = name?: "No name available"
            }
            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })

    }
    private fun setClickListener(imageView: ImageView, imageName: String) {
        imageView.setOnClickListener {
            selectedImageName = imageName
            selectedImageView?.background = null
            imageView.setBackgroundResource(R.drawable.highlight)
            selectedImageView = imageView
        }
    }



    private fun showToast(message: String) {
        Toast.makeText(this@CharacterEvolutionActivity, message, Toast.LENGTH_SHORT).show()
    }
}