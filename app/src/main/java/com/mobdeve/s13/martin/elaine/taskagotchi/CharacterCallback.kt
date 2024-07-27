package com.mobdeve.s13.martin.elaine.taskagotchi

import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskagotchiData

interface CharacterCallback {
    fun onDataFetched(charName: String?, difficulty: String?)
}