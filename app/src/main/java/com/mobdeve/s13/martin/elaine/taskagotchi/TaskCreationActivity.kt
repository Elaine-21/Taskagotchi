package com.mobdeve.s13.martin.elaine.taskagotchi

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
        val userId = intent.getStringExtra("userId")
        val charId = intent.getStringExtra("charId")


        getCharacter(charId)
        //
        getTaskQuantity(difficulty)

        Log.d("TaskCreation", "charName outside: ${charName} difficulty outside: ${difficulty}")

        viewBinding.addTaskBtn.setOnClickListener{
            if(taskAdded <= taskQuantity){
                showAddTaskDialog(taskQuantity)
            }else{
                Toast.makeText(this@TaskCreationActivity, "Task full", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.addTaskDoneBtn.setOnClickListener {
            if(taskAdded == taskQuantity){
                Toast.makeText(this@TaskCreationActivity, "Tasks added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@TaskCreationActivity, "Please complete task quantity: ${taskQuantity} charName ${charName}", Toast.LENGTH_SHORT).show()
                Log.d("TaskCreation", "charName ${charName}")
            }
        }

    }

    private fun saveTasks(){

    }


    private fun getCharacter(charId: String?) {
        taskaCharacterReference.orderByChild("id").equalTo(charId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (child in snapshot.children) {
                        val charName1 = child.child("name").getValue(String::class.java)
                        val difficulty1 = child.child("difficulty").getValue(String::class.java)


                        Log.d("TaskCreation", "charName1 ${charName1} difficulty1 ${difficulty1}")

                        charName = charName1
                        difficulty = difficulty1

                        Log.d("TaskCreation", "charName inside: ${charName} difficulty inside: ${difficulty}")


                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TaskCreationActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getTaskQuantity(difficulty: String?){

        if(difficulty == "BEGINNER"){
            taskQuantity = 2
        }else if(difficulty == "AMATEUR"){
            taskQuantity = 4
        }else if(difficulty == "EXPERT"){
            taskQuantity = 6
        }

    }

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
    private fun showAddTaskDialog(taskQuantity: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_task)

        //set size
        dialog.window?.setLayout(
            500,
            700
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
                showAddedTask()
                taskAdded += 1
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