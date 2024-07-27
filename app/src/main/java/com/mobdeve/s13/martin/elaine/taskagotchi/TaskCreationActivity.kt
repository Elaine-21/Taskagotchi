package com.mobdeve.s13.martin.elaine.taskagotchi

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityTaskCreationBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.CharacterDifficulty
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskagotchiData
import kotlin.math.log

class TaskCreationActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var taskaCharacterReference: DatabaseReference
    private lateinit var taskDatabaseReference: DatabaseReference

    private var taskTitle : String = ""
    private var taskDescription: String = ""
    private var frequency: String = ""
    private var taskAdded: Int = 0
    private var difficulty: String?= null
    private var charName:String? = null
    private var taskQuantity: Int = 0


    interface CharacterDataCallback {
        fun onDataFetched(charName: String?, difficulty: String?)
    }

    private fun getCharacter(charId: String?, callback: CharacterDataCallback) {
        taskaCharacterReference.orderByChild("id").equalTo(charId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val charName1 = child.child("name").getValue(String::class.java)
                        val difficulty1 = child.child("difficulty").getValue(String::class.java)

                        callback.onDataFetched(charName1, difficulty1)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TaskCreationActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityTaskCreationBinding = ActivityTaskCreationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //
        firebaseDatabase = FirebaseDatabase.getInstance()
        usersReference = firebaseDatabase.reference.child("users")
        taskaCharacterReference = firebaseDatabase.reference.child("taskagotchiCharacter")
        taskDatabaseReference = firebaseDatabase.reference.child("tasks")

        //
        val username = intent.getStringExtra("username")
        val userId = intent.getStringExtra("userId")
        val charId = intent.getStringExtra("charId")



        //
        getCharacter(charId, object : CharacterDataCallback {
            override fun onDataFetched(charName: String?, difficulty: String?) {
                this@TaskCreationActivity.charName = charName
                this@TaskCreationActivity.difficulty = difficulty

                getTaskQuantity(difficulty)
                Log.d("TaskCreation", "charName after fetch: ${charName} difficulty after fetch: ${difficulty}")
            }
        })


        viewBinding.addTaskBtn.setOnClickListener{
            if(taskAdded <= taskQuantity){
                showAddTaskDialog(taskQuantity, charId)
                Log.d("TaskCreation", "Task Added (Outside): ${taskAdded}")
            }else{
                Toast.makeText(this@TaskCreationActivity, "Task full", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.addTaskDoneBtn.setOnClickListener {
            if(taskAdded == taskQuantity){
                Toast.makeText(this@TaskCreationActivity, "Tasks added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@TaskCreationActivity, HomeActivity::class.java)
                // Put the username and password into the Intent
                intent.putExtra("username", username)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@TaskCreationActivity, "Please complete task quantity: ${taskQuantity}", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun saveTasks(charId: String?, title: String, description: String, frequency: String) {
        taskaCharacterReference.orderByChild("id").equalTo(charId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val characterDetails = child.getValue(TaskagotchiData::class.java)
                        if (characterDetails != null) {
                            val id = taskDatabaseReference.push().key
                            val task = TaskData(id, title, description, frequency)
                            val tasks = characterDetails.tasks?.toMutableList() ?: mutableListOf()
                            tasks.add(task)
                            characterDetails.tasks = tasks

                            taskaCharacterReference.child(charId!!).setValue(characterDetails)
                                .addOnSuccessListener {
                                    Toast.makeText(this@TaskCreationActivity, "Task saved successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@TaskCreationActivity, "Failed to save task", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TaskCreationActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //gets the number of task needed for the character difficulty
    private fun getTaskQuantity(difficulty: String?){

        if(difficulty == "BEGINNER"){
            taskQuantity = 2
        }else if(difficulty == "AMATEUR"){
            taskQuantity = 4
        }else if(difficulty == "EXPERT"){
            taskQuantity = 6
        }

    }

    //this shows the added task on the list
    private fun showAddedTask(){
        var task: TextView = findViewById(R.id.task1_tv)
        if(taskAdded == 1){
            task = findViewById(R.id.task1_tv)
        }else if(taskAdded == 2){
            task = findViewById(R.id.task2_tv)
        }else if(taskAdded == 3){
            task = findViewById(R.id.task3_tv)
        }else if(taskAdded == 4){
            task = findViewById(R.id.task4_tv)
        }else if(taskAdded == 5){
            task = findViewById(R.id.task5_tv)
        }else if(taskAdded == 6){
            task = findViewById(R.id.task6_tv)
        }

        task.setText("Task ${taskAdded} - ${frequency}\n${taskDescription}")

    }

    //creates a pop up for the task information
    private fun showAddTaskDialog(taskQuantity: Int, charId: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_task)

        //set size
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val exitBtn = dialog.findViewById<ImageView>(R.id.exit_btn)
        val addTaskBtn = dialog.findViewById<Button>(R.id.add_task_btn)
        val taskTitleEt = dialog.findViewById<EditText>(R.id.task_title_et)
        val taskDescriptionEt = dialog.findViewById<EditText>(R.id.task_description_et)
        val frequencyRg = dialog.findViewById<RadioGroup>(R.id.frequency_rg)

        exitBtn.setOnClickListener {
            dialog.dismiss()
        }

        addTaskBtn.setOnClickListener {
            taskTitle = taskTitleEt.text.toString()
            taskDescription = taskDescriptionEt.text.toString()
            val selectedFrequencyId = frequencyRg.checkedRadioButtonId
            frequency = when (selectedFrequencyId) {
                R.id.everyday_rb -> "Everyday"
                R.id.once_a_week_rb -> "Once a Week"
                R.id.every_other_day_rb -> "Every Other Day"
                else -> ""
            }

            Log.d("TaskCreation", "Title: $taskTitle")
            Log.d("TaskCreation", "Description: $taskDescription")
            Log.d("TaskCreation", "frequency: $frequency")
            Log.d("TaskCreation", "TaskQuantity: $taskQuantity")


            if(frequency != "" && taskTitle != "" && taskDescription != ""){
                Toast.makeText(this@TaskCreationActivity, "All fields complete", Toast.LENGTH_SHORT).show()
                taskAdded += 1
                Log.d("TaskCreation", "Task Added: ${taskAdded}")
                showAddedTask()
//                saveTasks(charId, taskTitle, taskDescription, frequency)
                dialog.dismiss()
            }else if (taskTitle == ""){
                Toast.makeText(this@TaskCreationActivity, "Please enter task title", Toast.LENGTH_SHORT).show()
            }else if (taskDescription == ""){
                Toast.makeText(this@TaskCreationActivity, "Please enter task description", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@TaskCreationActivity, "Please select task frequency", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()
    }
}