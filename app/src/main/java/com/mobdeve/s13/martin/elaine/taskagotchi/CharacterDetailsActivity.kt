package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterDetailsBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.HomeData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData

//import com.mobdeve.s13.martin.elaine.taskagotchi.R.layout.activity_character_details

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
//    private lateinit var taskReference: DatabaseReference
//    private lateinit var taskArrayList: ArrayList<TaskData>
    private lateinit var taskIds: ArrayList<String>
    private lateinit var viewBinding: ActivityCharacterDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val characterId = this.intent.getStringExtra("characterId")
        val characterName = this.intent.getStringExtra("characterName")
        val characterPicURL = this.intent.getStringExtra("characterPicURL")
        val characterStatus = this.intent.getStringExtra("characterStatus")
        val characterStreak = this.intent.getIntExtra("characterStreak", 0)
        val characterEnergy = this.intent.getIntExtra("characterEnergy", 0)
        val characterDebuff = this.intent.getStringExtra("characterDebuff")
        taskIds = intent.getStringArrayListExtra("taskIds") ?: arrayListOf()

        Log.d("CharacterDetailsActivity", "Character ID: $characterId")
        Log.d("CharacterDetailsActivity", "Character Name: $characterName")
        Log.d("CharacterDetailsActivity", "Character Status: $characterStatus")
        Log.d("CharacterDetailsActivity", "Character Streak: $characterStreak")
        Log.d("CharacterDetailsActivity", "Character Energy: $characterEnergy")
        Log.d("CharacterDetailsActivity", "Character Debuff: $characterDebuff")
        Log.d("CharacterDetailsActivity", "Received Task IDs: $taskIds")

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


        firebaseDatabase = FirebaseDatabase.getInstance()
        if (taskIds.isNotEmpty()) {
            // Proceed to fetch and display tasks
            readTasksData()
        } else {
            // Handle the case where there are no task IDs
            Log.d("CharacterDetailsActivity", "No task IDs to fetch.")
            showToast("No tasks available for this character.")
        }
    }

    private fun readTasksData() {
        // Map to keep track of task references
        val taskReferences = taskIds.take(6).map { id -> firebaseDatabase.getReference("tasks").child(id) }

        // Read data for each task
        taskReferences.forEachIndexed { index, reference ->
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val taskData = snapshot.getValue(TaskData::class.java)
                        taskData?.let {
                            bindTaskData(index + 1, it)
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


    private fun bindTaskData(taskNumber: Int, taskData: TaskData) {
        when (taskNumber) {
            1 -> {
                viewBinding.task1CD.text = taskData.title ?: "No title available"
                viewBinding.task1Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task1Description.text = taskData.description ?: "No description available"
            }
            2 -> {
                viewBinding.task2CD.text = taskData.title ?: "No title available"
                viewBinding.task2Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task2Description.text = taskData.description ?: "No description available"
            }
            3 -> {
                viewBinding.task3CD.text = taskData.title ?: "No title available"
                viewBinding.task3Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task3Description.text = taskData.description ?: "No description available"
            }
            4 -> {
                viewBinding.task4CD.text = taskData.title ?: "No title available"
                viewBinding.task4Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task4Description.text = taskData.description ?: "No description available"
            }
            5 -> {
                viewBinding.task5CD.text = taskData.title ?: "No title available"
                viewBinding.task5Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task5Description.text = taskData.description ?: "No description available"
            }
            6 -> {
                viewBinding.task6CD.text = taskData.title ?: "No title available"
                viewBinding.task6Frequency.text = taskData.frequency ?: "No frequency available"
                viewBinding.task6Description.text = taskData.description ?: "No description available"
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@CharacterDetailsActivity, message, Toast.LENGTH_SHORT).show()
    }
}

// dont need to use the array list just bind it already to the element