package com.mobdeve.s13.martin.elaine.taskagotchi.model

import java.util.Date

data class TaskData(
    val taskID: String? = null,
    var title: String? = null,
    var description: String? = null,
    val frequency: String? = null,
   // var startDate: Date? = null,
    //checks how many days it has been since the last completion of the task for the debuff
    var lastCompletedDate: Date? = null,
    var missCntr: Int? = null
)
