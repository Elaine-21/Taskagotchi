package com.mobdeve.s13.martin.elaine.taskagotchi.model

data class TaskagotchiData(
    val id: String? = null,
    val name: String? = null,
    val difficulty: Enum<CharacterDifficulty>? = null,
    val picURL: String? = null,
    val gender: Enum<CharacterGender>? = null,
    val status: String? = null,
    val streak: Int? = null,
    var energy: Int? = null,
    var debuff: String? = null,
    var tasks: List<TaskData>? = null
)
