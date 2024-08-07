package com.mobdeve.s13.martin.elaine.taskagotchi.model

import java.util.Date

data class TaskagotchiData(
    val id: String? = null,
    val name: String? = null,
    val difficulty: String? = null,
    val picURL: String? = null,
    val gender: String? = null,
    val color: String? = null,
    val age: String? = null,
    var status: String? = "Healthy",
    var streak: Int? = 0,
    var energy: Int? = 50,
    var debuff: String? = "none",
    var levelUpDate: Date? = null,
    var charEvolution: MutableList<String> = mutableListOf(),
    var tasksIDList: List<String>? = null,
    var energyRestorationDate: Date? = null
)
