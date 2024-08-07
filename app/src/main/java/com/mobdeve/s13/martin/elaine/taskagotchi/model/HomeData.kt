package com.mobdeve.s13.martin.elaine.taskagotchi.model

import java.util.Date

data class HomeData(
    val id: String? = null,
    val name: String? = null,
    val difficulty: String? = null,
    val picURL: String? = null,
    val gender: String? = null,
    val color: String? = null,
    val age: String? = null,
    var status: String? = null,
    var streak: Int? = null,
    var energy: Int? = null,
    var debuff: String? = null,
    var levelUpDate: Date? = null,
    var charEvolution: List<String>? = null,//var charEvolution: MutableList<String> = mutableListOf(),
    var tasksIDList: List<String>? = null,
    var energyRestorationDate: Date? = null
)
