package com.mobdeve.s13.martin.elaine.taskagotchi.model

import androidx.compose.ui.graphics.Color

data class TaskagotchiData(
    val id: String? = null,
    val name: String? = null,
    val difficulty: Enum<CharacterDifficulty>? = null,
    val picURL: String? = null,
    val gender: Enum<CharacterGender>? = null,
    val color: String? = null,
    var status: String? = "Healthy",
    var streak: Int? = 0,
    var energy: Int? = 50,
    var debuff: String? = "none",
    var tasks: List<TaskData>? = null
)
