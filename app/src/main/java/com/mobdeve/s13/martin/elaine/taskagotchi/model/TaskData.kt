package com.mobdeve.s13.martin.elaine.taskagotchi.model

import java.util.Date

data class TaskData(
    val taskID: String? = null,
    var title: String? = null,
    var description: String? = null,
    val frequency: String? = null,
    var startDate: Date? = null,
    var lastCompletedDate: Date? = null
)
