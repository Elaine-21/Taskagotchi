package com.mobdeve.s13.martin.elaine.taskagotchi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ItemCharacterBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterCollection
import com.mobdeve.s13.martin.elaine.taskagotchi.util.CharacterDrawableMapper

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
            val context = binding.root.context
            val imageResId = if (character.isUnlocked) {
                CharacterDrawableMapper.getDrawableResId(character.id)
            } else {
                R.drawable.black_egg_question_mark // Default locked drawable
            }

            Glide.with(context)
                .load(imageResId)
                .into(binding.characterImage)

            binding.root.setOnClickListener { onCharacterClick(character) }
        }
    }
}

