package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterEvolutionBinding

class CharacterEvolutionActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCharacterEvolutionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterEvolutionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}