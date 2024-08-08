package com.mobdeve.s13.martin.elaine.taskagotchi.model

data class CharacterCollection(
    val name: String = "",
    val imageResId: String = "", // This should be a URL if you are using Glide for remote images
    val isUnlocked: Boolean = false
)
