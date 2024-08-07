package com.mobdeve.s13.martin.elaine.taskagotchi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemCharacterBinding

class CharacterAdapter(
    private val characters: List<Character>,
    private val onCharacterClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    override fun getItemCount() = characters.size

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            // Set image or silhouette based on unlock status
            if (character.isUnlocked) {
                binding.characterImage.setImageResource(character.imageResId)
            } else {
                binding.characterImage.setImageResource(R.drawable.baseline_logout_24)
            }

            binding.root.setOnClickListener { onCharacterClick(character) }
        }
    }
}

data class Character(val id: Int, val imageResId: Int, val isUnlocked: Boolean)
