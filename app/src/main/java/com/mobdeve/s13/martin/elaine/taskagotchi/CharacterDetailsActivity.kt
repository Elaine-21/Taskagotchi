package com.mobdeve.s13.martin.elaine.taskagotchi

import android.os.Bundle
import android.util.Log
import android.view.View
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
        val title = taskData.title ?: "No title available"
        val frequency = taskData.frequency ?: "No frequency available"
        val description = taskData.description ?: "No description available"

        when (taskNumber) {
            1 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task1CD.text = title
                    viewBinding.task1Frequency.text = frequency
                    viewBinding.task1Description.text = description
                    viewBinding.task1CD.visibility = View.VISIBLE
                    viewBinding.task1Frequency.visibility = View.VISIBLE
                    viewBinding.task1Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask1.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task2CD.text = title
                    viewBinding.task2Frequency.text = frequency
                    viewBinding.task2Description.text = description
                    viewBinding.task2CD.visibility = View.VISIBLE
                    viewBinding.task2Frequency.visibility = View.VISIBLE
                    viewBinding.task2Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask2.visibility = View.VISIBLE
                }
            }
            3 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task3CD.text = title
                    viewBinding.task3Frequency.text = frequency
                    viewBinding.task3Description.text = description
                    viewBinding.task3CD.visibility = View.VISIBLE
                    viewBinding.task3Frequency.visibility = View.VISIBLE
                    viewBinding.task3Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask3.visibility = View.VISIBLE
                }
            }
            4 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task4CD.text = title
                    viewBinding.task4Frequency.text = frequency
                    viewBinding.task4Description.text = description
                    viewBinding.task4CD.visibility = View.VISIBLE
                    viewBinding.task4Frequency.visibility = View.VISIBLE
                    viewBinding.task4Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask4.visibility = View.VISIBLE
                }
            }
            5 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task5CD.text = title
                    viewBinding.task5Frequency.text = frequency
                    viewBinding.task5Description.text = description
                    viewBinding.task5CD.visibility = View.VISIBLE
                    viewBinding.task5Frequency.visibility = View.VISIBLE
                    viewBinding.task5Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask5.visibility = View.VISIBLE
                }
            }
            6 -> {
                if (taskData.title != null || taskData.frequency != null || taskData.description != null) {
                    viewBinding.task6CD.text = title
                    viewBinding.task6Frequency.text = frequency
                    viewBinding.task6Description.text = description
                    viewBinding.task6CD.visibility = View.VISIBLE
                    viewBinding.task6Frequency.visibility = View.VISIBLE
                    viewBinding.task6Description.visibility = View.VISIBLE
                    viewBinding.radioBtnTask6.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this@CharacterDetailsActivity, message, Toast.LENGTH_SHORT).show()
    }
}

// dont need to use the array list just bind it already to the element