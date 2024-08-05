package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterEvolutionHistoryBinding

class CharacterEvolutionHistoryActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCharacterEvolutionHistoryBinding
    private lateinit var charIds: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterEvolutionHistoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Log.d("CharacterEvolutionHistoryActivity", "here in char evo history page")

        charIds = intent.getStringArrayListExtra("charIds") ?: arrayListOf()
        Log.d("CharacterEvolutionHistoryActivity", "charEvolution: $charIds")

    }
}