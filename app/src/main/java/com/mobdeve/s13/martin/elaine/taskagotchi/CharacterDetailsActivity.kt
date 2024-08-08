package com.mobdeve.s13.martin.elaine.taskagotchi

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterDetailsBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

//import com.mobdeve.s13.martin.elaine.taskagotchi.R.layout.activity_character_details

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var taskReference: DatabaseReference
//    private lateinit var taskArrayList: ArrayList<TaskData>
    private lateinit var taskIds: ArrayList<String>
    private lateinit var charIds: ArrayList<String>
    private lateinit var viewBinding: ActivityCharacterDetailsBinding
    private val missedDaysList = mutableListOf<Int>()
    private var characterId : String? = null
    private var userId: String? = null
    private var characterEnergy : Int? = null
    private var characterStreak: Int? = null
    private var characterStatus: String? = null
    private var characterPicURL: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()

        userId = this.intent.getStringExtra("userId")
        characterId = this.intent.getStringExtra("characterId")
        val characterName = this.intent.getStringExtra("characterName")
        characterPicURL = this.intent.getStringExtra("characterPicURL")
        characterStatus = this.intent.getStringExtra("characterStatus")
        characterStreak = this.intent.getIntExtra("characterStreak", 0)
        characterEnergy = this.intent.getIntExtra("characterEnergy", 0)
        var characterDebuff = this.intent.getStringExtra("characterDebuff")
        taskIds = intent.getStringArrayListExtra("taskIds") ?: arrayListOf()
//        val charEvolution = intent.getStringArrayListExtra("charEvolution")?.toMutableList()
        charIds = intent.getStringArrayListExtra("charIds") ?: arrayListOf()

        Log.d("CharacterDetailsActivity", "Character ID: $characterId")
        Log.d("CharacterDetailsActivity", "Character Name: $characterName")
        Log.d("CharacterDetailsActivity", "Character Status: $characterStatus")
        Log.d("CharacterDetailsActivity", "Character Streak: $characterStreak")
        Log.d("CharacterDetailsActivity", "Character Energy: $characterEnergy")
        Log.d("CharacterDetailsActivity", "Character Debuff: $characterDebuff")
        Log.d("CharacterDetailsActivity", "Received Task IDs: $taskIds")
        Log.d("CharacterDetailsActivity", "Received character IDs: $charIds")

        checkStreak()

        viewBinding.taskagotchiNameCD.text = characterName ?: "No name available"
        viewBinding.taskagotchiHealthCD.text = characterStatus ?: "No status available"
        viewBinding.taskagotchiStreakCD.text = characterStreak.toString()
        viewBinding.taskagotchiEnergyCD.text = characterEnergy.toString()
        viewBinding.taskagotchiDebuffCD.text = characterDebuff ?: "No debuff available"

        characterPicURL?.let {
            val resId = resources.getIdentifier(it, "drawable", packageName)
            if (resId != 0) {
                viewBinding.taskagotchiPictureCD.setImageResource(resId)
            } else {
                showToast("Image not found")
            }
        }

        //aditional task button
        viewBinding.additionalTaskBtn.setOnClickListener {val intent = Intent(this@CharacterDetailsActivity, AdditionalTaskActivity::class.java)
            intent.putExtra("characterId", characterId)
            intent.putExtra("taskIdsSize", taskIds.size)
            intent.putExtra("energy", characterEnergy)
            intent.putExtra("userId", userId)
            startActivity(intent)
            onPause()
        }

        //character evolution button
        viewBinding.characterEvolutionBtn.setOnClickListener {
            Log.d( "CharacterDetailsActivity", "charEvolutionBtn clicked")
            val intent = Intent(this@CharacterDetailsActivity, CharacterEvolutionHistoryActivity::class.java)

            intent.putStringArrayListExtra("charIds", charIds)
            startActivity(intent)
            onPause()
        }

        viewBinding.returnBtn.setOnClickListener{
            finish()
        }

        if (taskIds.isNotEmpty()) {
            // Proceed to fetch and display tasks
            readTasksData(characterId, userId)
        } else {
            // Handle the case where there are no task IDs
            Log.d("CharacterDetailsActivity", "No task IDs to fetch.")
            showToast("No tasks available for this character.")
        }


    }
    override fun onResume() {
        super.onResume()
        fetchCharacterData() // This method will fetch and update the character's data
    }

    private fun fetchCharacterData() {
        if (characterId != null) {
            // Assuming you have a reference to your Firebase database
            val characterRef = firebaseDatabase.getReference("taskagotchiCharacter/$userId/$characterId")
            Log.d("onResume", "characterID: ${characterId}")
            Log.d("onResume", "userId: ${userId}")
            characterRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        characterEnergy = snapshot.child("energy").getValue(Int::class.java) ?: 0
                        viewBinding.taskagotchiEnergyCD.text = characterEnergy.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Database Error: ${error.message}")
                }
            })
        }
    }

    private fun readTasksData(characterId: String?, userId: String?) {
        // Map to keep track of task references
        val taskReferences = taskIds.take(6).map { id -> firebaseDatabase.getReference("tasks/$characterId").child(id) }

        // Read data for each task
        taskReferences.forEachIndexed { index, reference ->
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val taskData = snapshot.getValue(TaskData::class.java)
                        taskData?.let {
                            bindTaskData(index + 1, it, characterId, userId)
                        }
                    } else {
                        Log.d("CharacterDetailsActivity", "Task data for ID not found: ${reference.key}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Database Error: ${error.message}")
                }
            })
        }
    }

    private fun updateCharacterData(userId: String?, charID: String?, characterDebuff: String?, characterStatus: String?){
        val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter/$userId")

        // Create a map or data class to hold the character details
        val characterData = mapOf(
            "debuff" to characterDebuff,
            "status" to characterStatus
        )

        // Save the character data under the given charID
        taskagotchiData.child(charID!!).updateChildren(characterData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Details", "Character updated")
                } else {
                    Log.d("Character Details", "Character failed to update")
                }
            }

    }
    private fun saveTaskData(taskData: TaskData) {
        var taskDatabaseReference: DatabaseReference = firebaseDatabase.reference.child("tasks/$characterId")

        taskDatabaseReference.child(taskData.taskID!!).setValue(taskData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Details", "Task Succesfully saved")

                } else {
                    Log.d("Character Details", "Task Failed to save")
                }

            }
    }

    //Checks if the current date is
    private fun isDone(taskData: TaskData): Boolean {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if(taskData.lastCompletedDate == null){
            return true
        }
        taskData.lastCompletedDate?.let { lastDate ->
            calendar.time = lastDate
            when (taskData.frequency) {
                "Everyday" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                "Once a Week" -> calendar.add(Calendar.DAY_OF_YEAR, 7)
                "Every Other Day" -> calendar.add(Calendar.DAY_OF_YEAR, 2)
            }
            Log.d("Character Details", "Last completed Date: ${taskData.lastCompletedDate} -- calendar.time: ${calendar.time}")
            Log.d("Character Details", "${taskData.title} isDone true or false: ${Date().after(calendar.time)}")
            return Date().after(calendar.time)
        }
        return false
    }

    //This updates the startDate of the Task and sets the taskData.isDone to true
    private fun updateTaskStatus(taskData: TaskData){
        missedDaysList.remove(taskData.missCntr)
        taskData.missCntr = 0

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d("Character Details", "Date accomplished ${calendar.time}")

        taskData.lastCompletedDate = calendar.time

        saveTaskData(taskData)

    }

    private fun calculateMissedDays(taskData: TaskData) {
        if(taskData.lastCompletedDate == null){
            return
        }

        val calendar = Calendar.getInstance()
        //Date today
        val currentDate = calendar.time
        //Stores the date when the task is last completed
        calendar.time = taskData.lastCompletedDate

        if(calendar.time.before(currentDate)) {
            when (taskData.frequency) {
                "Everyday" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                "Once a Week" -> calendar.add(Calendar.DAY_OF_YEAR, 7)
                "Every Other Day" -> calendar.add(Calendar.DAY_OF_YEAR, 2)
            }

            var missedCount = (currentDate.time - calendar.time.time)

            Log.d("Debuff Calculation", "currentDate: ${currentDate.time} --- dateToday: ${calendar.time.time}")
            Log.d("Debuff Calculation", "Missed ${TimeUnit.MILLISECONDS.toDays(missedCount).toInt()} times since last completion.")
            var days = TimeUnit.MILLISECONDS.toDays(missedCount)

            taskData.missCntr = days.toInt()
            saveTaskData(taskData)

            missedDaysList.add(days.toInt())

        }
    }

    private fun bindTaskData(taskNumber: Int, taskData: TaskData, characterId: String?, userId: String?) {

        val title = taskData.title ?: "No title available"
        val frequency = taskData.frequency ?: "No frequency available"
        val description = taskData.description ?: "No description available"

        when (taskNumber) {
            1 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task1CD.text = title
                    viewBinding.task1Frequency.text = frequency
                    viewBinding.task1Description.text = description
                    viewBinding.task1CD.visibility = View.VISIBLE
                    viewBinding.task1Frequency.visibility = View.VISIBLE
                    viewBinding.task1Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask1.visibility = View.VISIBLE

                    viewBinding.radioBtnTask1.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask1.isChecked) {

                        viewBinding.radioBtnTask1.setOnClickListener {

                            viewBinding.radioBtnTask1.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }
                }
            }
            2 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task2CD.text = title
                    viewBinding.task2Frequency.text = frequency
                    viewBinding.task2Description.text = description
                    viewBinding.task2CD.visibility = View.VISIBLE
                    viewBinding.task2Frequency.visibility = View.VISIBLE
                    viewBinding.task2Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask2.visibility = View.VISIBLE

                    viewBinding.radioBtnTask2.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask2.isChecked) {

                        viewBinding.radioBtnTask2.setOnClickListener {

                            viewBinding.radioBtnTask2.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }

                }
            }
            3 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task3CD.text = title
                    viewBinding.task3Frequency.text = frequency
                    viewBinding.task3Description.text = description
                    viewBinding.task3CD.visibility = View.VISIBLE
                    viewBinding.task3Frequency.visibility = View.VISIBLE
                    viewBinding.task3Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask3.visibility = View.VISIBLE

                    viewBinding.radioBtnTask3.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask3.isChecked) {

                        viewBinding.radioBtnTask3.setOnClickListener {

                            viewBinding.radioBtnTask3.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }

                }
            }
            4 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task4CD.text = title
                    viewBinding.task4Frequency.text = frequency
                    viewBinding.task4Description.text = description
                    viewBinding.task4CD.visibility = View.VISIBLE
                    viewBinding.task4Frequency.visibility = View.VISIBLE
                    viewBinding.task4Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask4.visibility = View.VISIBLE

                    viewBinding.radioBtnTask4.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask4.isChecked) {

                        viewBinding.radioBtnTask4.setOnClickListener {

                            viewBinding.radioBtnTask4.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }
                }
            }
            5 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task5CD.text = title
                    viewBinding.task5Frequency.text = frequency
                    viewBinding.task5Description.text = description
                    viewBinding.task5CD.visibility = View.VISIBLE
                    viewBinding.task5Frequency.visibility = View.VISIBLE
                    viewBinding.task5Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask5.visibility = View.VISIBLE

                    viewBinding.radioBtnTask5.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask5.isChecked) {

                        viewBinding.radioBtnTask5.setOnClickListener {

                            viewBinding.radioBtnTask5.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }
                }
            }
            6 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    calculateMissedDays(taskData)
                    viewBinding.task6CD.text = title
                    viewBinding.task6Frequency.text = frequency
                    viewBinding.task6Description.text = description
                    viewBinding.task6CD.visibility = View.VISIBLE
                    viewBinding.task6Frequency.visibility = View.VISIBLE
                    viewBinding.task6Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask6.visibility = View.VISIBLE

                    viewBinding.radioBtnTask6.isChecked = isDone(taskData) == false

                    if (!viewBinding.radioBtnTask6.isChecked) {

                        viewBinding.radioBtnTask6.setOnClickListener {

                            viewBinding.radioBtnTask6.isChecked = true
                            updateTaskStatus(taskData)
                            updateMissedDays(userId, characterId)
                        }
                    }
                }
            }
        }

        updateMissedDays(userId, characterId)


    }


    private fun updateMissedDays(userId: String?, characterId:String?){
        var characterDebuff: String? = null
        var characterStatus: String? = null
        val maxMissedDays = missedDaysList.maxOrNull() ?: 0
        Log.d("Debuff Calculation", "Highest number of missed days: $maxMissedDays")

        if(maxMissedDays == 0){
            characterDebuff = "None"
            characterStatus = "Healthy"
        }else if(maxMissedDays == 1){
            characterDebuff = "x0.30"
            characterStatus = "Weak"
        }else if (maxMissedDays == 2){
            characterDebuff = "x0.70"
            characterStatus = "Sick"
        }else if(maxMissedDays >= 3){
            characterDebuff = "x1.00"
            characterStatus = "Debuffed"
        }
        viewBinding.taskagotchiDebuffCD.text = characterDebuff ?: "None"
        viewBinding.taskagotchiHealthCD.text = characterStatus ?: "Healthy"

        updateCharacterData(userId,characterId, characterDebuff, characterStatus)
    }
    private fun showToast(message: String) {
        Toast.makeText(this@CharacterDetailsActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun checkStreak(){
        if(characterStatus == "Healthy"){
            updateStreak(1)
        }else{
            updateStreak(2)
        }
    }
    private fun updateStreak(option: Int){
        if(option == 1) {
            val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter/$userId/$characterId")
            taskagotchiData.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val creationDate =
                        dataSnapshot.child("creationDate").getValue(Date::class.java)
                        if (creationDate != null) {
                            val currentDate = Calendar.getInstance().time
                            val diffInMillis = currentDate.time - creationDate.time
                            val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                            Log.d("Streak", "diffInDays: ${diffInDays}")
                            Log.d("Streak", "creationDate: ${creationDate}")

                            if (diffInDays >= 7) {
                                characterStreak = characterStreak?.plus(1)
                                Log.d("Streak", "CharacterStreak: ${characterStreak}")
                                updateStreak()
                                updateCreationDate()
                                showStreakDialogBox()
                            }
                        }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Database Error: ${databaseError.message}")
                }
            })
        }else{
            updateCreationDate()
        }

    }
    private fun updateCreationDate(){
        val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter/$userId")
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.MILLISECOND, 0)


        val characterData = mapOf(
            "creationDate" to date.time
        )

        // Save the character data under the given charID
        taskagotchiData.child(characterId!!).updateChildren(characterData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Details", "CreationDate updated")
                } else {
                    Log.d("Character Details", "CreationDate failed to update")
                }
            }
    }

    private fun updateStreak(){
        viewBinding.taskagotchiStreakCD.text = characterStreak.toString()

        val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter/$userId")

        val characterData = mapOf(
            "streak" to characterStreak
        )

        // Save the character data under the given charID
        taskagotchiData.child(characterId!!).updateChildren(characterData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Details", "CreationDate updated")
                } else {
                    Log.d("Character Details", "CreationDate failed to update")
                }
            }
    }

    private fun showStreakDialogBox(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_share_streak)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val exitBtn = dialog.findViewById<ImageButton>(R.id.exitBtn)
        val text = dialog.findViewById<TextView>(R.id.text)
        val shareBtn = dialog.findViewById<Button>(R.id.shareBtn)
        text.setText("Congratulations on reaching ${characterStreak} streaks on task completion! Keep up the good work!")

        characterPicURL?.let {
            val resId = resources.getIdentifier(it, "drawable", packageName)
            if (resId != 0) {
                dialog.findViewById<ImageView>(R.id.charImage).setImageResource(resId)
            } else {
                showToast("Image not found")
            }
        }

        exitBtn.setOnClickListener {
            dialog.dismiss()
        }

        shareBtn.setOnClickListener {

        }

        dialog.show()

    }
}
