package com.mobdeve.s13.martin.elaine.taskagotchi.model

data class HomeData(
    val id: String? = null,
    val name: String? = null,
    val difficulty: String? = null,
    val picURL: String? = null,
    val gender: String? = null,
    val color: String? = null,
    var status: String? = null,
    var streak: Int? = null,
    var energy: Int? = null,
    var debuff: String? = null,
    var tasksIDList: List<String>? = null
)
