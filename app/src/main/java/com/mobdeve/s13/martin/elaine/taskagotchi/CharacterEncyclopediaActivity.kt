package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterEncyclopediaBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData

class CharacterEncyclopediaActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCharacterEncyclopediaBinding
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCharacterEncyclopediaBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        val userId = intent.getStringExtra("userId")

        viewBinding.encyclopediaBackBtn.setOnClickListener {
            finish()
        }

//        viewBinding.babyBlueFemale.imageTintList = null // this works

        if (userId != null) {
            getUnlockedIdChar(userId)
        }
    }

    private fun getUnlockedIdChar(userId: String) {
        val databaseReference = firebaseDatabase.getReference("users").child(userId)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserData::class.java)
                val unlockedIDChar = userData?.unlockedIDChar

                if (unlockedIDChar != null) {
                    Log.d("Encyclopedia_check", "unlockedIDChar: $unlockedIDChar")
                    updateCharacterViews(unlockedIDChar)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }

    private fun updateCharacterViews(unlockedIDChar: List<String>) {
        val characterIdMap = mapOf(
            "babyBlueFemale" to "baby_blue_female",
            "babyGreenFemale" to "baby_green_female",
            "babyPinkFemale" to "baby_pink_female",
            "babyBlueMale" to "baby_blue_male",
            "babyGreenMale" to "baby_green_male",
            "babyPinkMale" to "baby_pink_male",

            "childFemaleChiroritchi" to "child_female_chiroritchi",
            "childFemaleMimitamatchi" to "child_female_mimitamatchi",
            "childFemaleTantotchi" to "child_female_tantotchi",
            "childMaleFuyofuyotchi" to "child_male_fuyofuyotchi",
            "childMaleMokumokutchi" to "child_male_mokumokutchi",
            "childMalePuchitomatchi" to "child_male_puchitomatchi",

            "teenFemaleHaretchi" to "teen_female_haretchi",
            "teenFemaleSoyofuwatchi" to "teen_female_soyofuwatchi",
            "teenFemaleTororitchi" to "teen_female_tororitchi",
            "teenMaleKurupoyotchi" to "teen_male_kurupoyotchi",
            "teenMaleMokokotchi" to "teen_male_mokokotchi",
            "teenMaleTerukerotchi" to "teen_male_terukerotchi",

            "adultBlueFemaleAwamokotchi" to "adult_blue_female_awamokotchi",
            "adultBlueFemaleHimetchi" to "adult_blue_female_himetchi",
            "adultBlueFemaleMimitchim" to "adult_blue_female_mimitchim",
            "adultBlueFemaleShinobinyatchimix" to "adult_blue_female_shinobinyatchimix",
            "adultBlueMaleGozarutchi" to "adult_blue_male_gozarutchi",
            "adultBlueMaleKikitchimix" to "adult_blue_male_kikitchimix",
            "adultBlueMaleKuromametchi" to "adult_blue_male_kuromametchi",
            "adultBlueMaleMametchimix" to "adult_blue_male_mametchimix",

            "adultGreenFemaleCofret" to "adult_green_female_cofret",
            "adultGreenFemaleMemetchimix" to "adult_green_female_memetchimix",
            "adultGreenFemaleNeliatchi" to "adult_green_female_neliatchi",
            "adultGreenFemaleVioletchi" to "adult_green_female_violetchi",
            "adultGreenMaleMurachakitchi" to "adult_green_male_murachakitchi",
            "adultGreenMalePaintotchi" to "adult_green_male_paintotchi",
            "adultGreenMaleShimagurutchimix" to "adult_green_male_shimagurutchimix",
            "adultGreenMaleWeeptchi" to "adult_green_male_weeptchi",

            "adultPinkFemaleChamametchi" to "adult_pink_female_chamametchi",
            "adultPinkFemaleLovelitchimix" to "adult_pink_female_lovelitchimix",
            "adultPinkFemaleMomotchimix" to "adult_pink_female_momotchimix",
            "adultPinkFemaleSebiretchi" to "adult_pink_female_sebiretchi",
            "adultPinkMaleCharatchi" to "adult_pink_male_charatchi",
            "adultPinkMaleGinjirotchi" to "adult_pink_male_ginjirotchi",
            "adultPinkMaleKuchipatchim" to "adult_pink_male_kuchipatchim",
            "adultPinkMaleOrenetchi" to "adult_pink_male_orenetchi"
        )

        val characterViews = listOf(
            viewBinding.babyBlueFemale,
            viewBinding.babyGreenFemale,
            viewBinding.babyPinkFemale,
            viewBinding.babyBlueMale,
            viewBinding.babyGreenMale,
            viewBinding.babyPinkMale,

            viewBinding.childFemaleChiroritchi,
            viewBinding.childFemaleMimitamatchi,
            viewBinding.childFemaleTantotchi,
            viewBinding.childMaleFuyofuyotchi,
            viewBinding.childMaleMokumokutchi,
            viewBinding.childMalePuchitomatchi,

            viewBinding.teenFemaleHaretchi,
            viewBinding.teenFemaleSoyofuwatchi,
            viewBinding.teenFemaleTororitchi,
            viewBinding.teenMaleKurupoyotchi,
            viewBinding.teenMaleMokokotchi,
            viewBinding.teenMaleTerukerotchi,

            viewBinding.adultBlueFemaleAwamokotchi,
            viewBinding.adultBlueFemaleHimetchi,
            viewBinding.adultBlueFemaleMimitchim,
            viewBinding.adultBlueFemaleShinobinyatchimix,
            viewBinding.adultBlueMaleGozarutchi,
            viewBinding.adultBlueMaleKikitchimix,
            viewBinding.adultBlueMaleKuromametchi,
            viewBinding.adultBlueMaleMametchimix,

            viewBinding.adultGreenFemaleCofret,
            viewBinding.adultGreenFemaleMemetchimix,
            viewBinding.adultGreenFemaleNeliatchi,
            viewBinding.adultGreenFemaleVioletchi,
            viewBinding.adultGreenMaleMurachakitchi,
            viewBinding.adultGreenMalePaintotchi,
            viewBinding.adultGreenMaleShimagurutchimix,
            viewBinding.adultGreenMaleWeeptchi,

            viewBinding.adultPinkFemaleChamametchi,
            viewBinding.adultPinkFemaleLovelitchimix,
            viewBinding.adultPinkFemaleMomotchimix,
            viewBinding.adultPinkFemaleSebiretchi,
            viewBinding.adultPinkMaleCharatchi,
            viewBinding.adultPinkMaleGinjirotchi,
            viewBinding.adultPinkMaleKuchipatchim,
            viewBinding.adultPinkMaleOrenetchi
        )


        for (characterView in characterViews) {
            val viewTag = characterView.tag?.toString()
            val characterId = characterIdMap[viewTag]
            Log.d("Encyclopedia_check", "characterId: $characterId")
            if (characterId == null) {
                continue
            }
            if (unlockedIDChar.contains(characterId)) {
                characterView.imageTintList = null  // Remove the tint
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@CharacterEncyclopediaActivity, message, Toast.LENGTH_SHORT).show()
    }
}
