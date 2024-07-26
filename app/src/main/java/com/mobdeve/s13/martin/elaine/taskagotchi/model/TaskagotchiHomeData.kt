package com.mobdeve.s13.martin.elaine.taskagotchi.model

data class TaskagotchiHomeData(
    val id: String? = null,
    val name: String? = null,
    val picURL: String? = null,
    val status: String? = null,
    val streak: Int? = null,
    var energy: Int? = null,
)
