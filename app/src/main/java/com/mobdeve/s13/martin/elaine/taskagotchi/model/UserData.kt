package com.mobdeve.s13.martin.elaine.taskagotchi.model

data class UserData(
    val id: String? = null,
    val username: String? = null,
    val password: String? = null,
    var taskagotchiList: List<String>? = null,
    var unlockedChar: List<String>? = null
)
