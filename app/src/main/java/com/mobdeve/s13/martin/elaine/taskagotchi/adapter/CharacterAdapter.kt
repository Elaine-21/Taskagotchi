package com.mobdeve.s13.martin.elaine.taskagotchi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemCharacterBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterCollection

class CharacterAdapter(
    private val characters: List<CharacterCollection>,
    private val onCharacterClick: (CharacterCollection) -> Unit
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
        fun bind(character: CharacterCollection) {
            if (character.isUnlocked) {
                Glide.with(binding.root.context)
                    .load(character.imageResId) // Ensure this is a valid URL or resource
                    .into(binding.characterImage)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.baseline_logout_24) // Placeholder or locked image
                    .into(binding.characterImage)
            }

            binding.root.setOnClickListener { onCharacterClick(character) }
        }
    }
}
