package com.mobdeve.s13.martin.elaine.taskagotchi

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityAdditionalTaskBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityCharacterDetailsBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityTaskCreationBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData
import java.util.Calendar
import java.util.Date

class AdditionalTaskActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var usersReference: DatabaseReference
    private lateinit var additionalTask: DatabaseReference
    private lateinit var viewBinding: ActivityAdditionalTaskBinding

    private var taskTitle : String = ""
    private var taskDescription: String = ""
    private var energy: Int = 0
    private var taskAdded: Int = 0
    private var taskQuantity: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAdditionalTaskBinding.inflate(layoutInflater) // Use the class-level property
        setContentView(viewBinding.root) // Set the content view
        //
        firebaseDatabase = FirebaseDatabase.getInstance()
        usersReference = firebaseDatabase.reference.child("users")
        additionalTask = firebaseDatabase.reference.child("additionalTasks")

        val charId = intent.getStringExtra("characterId")
        taskQuantity = intent.getIntExtra("taskIdsSize", 0)
        energy = intent.getIntExtra("energy", 0)

        checkAllTask(charId)
        viewBinding.addTaskBtn.setOnClickListener{
            if(taskAdded < taskQuantity){
                showAddTaskDialog(taskQuantity, charId)
                Log.d("TaskCreation", "Task Added (Outside): ${taskAdded}")
            }else{
                Toast.makeText(this@AdditionalTaskActivity, "Task full", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.exitAddtnTaskBtn.setOnClickListener{
            updateEnergy(charId)
            finish()
        }

    }

    private fun updateEnergy(charID: String?){
        val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter")

        // Create a map or data class to hold the character details
        val characterData = mapOf(
            "energy" to energy
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

    private fun checkAllTask(charId: String?){
        if (charId == null) return

        // Reference to the specific character's tasks
        val tasksReference = firebaseDatabase.getReference("additionalTasks/$charId")

        tasksReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve all tasks under the character's ID
                    val tasks = snapshot.children.mapNotNull { it.getValue(TaskData::class.java) }
                    tasks.forEachIndexed { index, taskData ->
                        bindTaskData(index + 1, taskData, charId)
                    }
                } else {
                    Log.d("Retrieving Tasks", "Retrieving Additional Tasks failed ")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }

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
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            Log.d("AdditionalTask", "Last completed Date: ${taskData.lastCompletedDate} -- calendar.time: ${calendar.time}")
            Log.d("AdditionalTask", "${taskData.title} isDone true or false: ${Date().after(calendar.time)}")
            return Date().after(calendar.time)
        }
        return false
    }

    private fun bindTaskData(taskNumber: Int, taskData: TaskData, charId: String?) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var task: TextView = findViewById(R.id.task1_tv)

        when (taskNumber) {
            1 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task1_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton1.visibility = View.VISIBLE

                    viewBinding.radioButton1.isChecked = isDone(taskData) == false
                    taskAdded = 1
                    if (!viewBinding.radioButton1.isChecked && energy >= 0) {

                        viewBinding.radioButton1.setOnClickListener {
                            viewBinding.radioButton1.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }

                }
            }
            2 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task2_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton2.visibility = View.VISIBLE

                    viewBinding.radioButton2.isChecked = isDone(taskData) == false
                    taskAdded = 2

                    if (!viewBinding.radioButton2.isChecked && energy >= 0) {

                        viewBinding.radioButton2.setOnClickListener {
                            viewBinding.radioButton2.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }
                }
            }
            3 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task3_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton3.visibility = View.VISIBLE

                    viewBinding.radioButton3.isChecked = isDone(taskData) == false
                    taskAdded = 3

                    if (!viewBinding.radioButton3.isChecked && energy >= 0) {

                        viewBinding.radioButton3.setOnClickListener {
                            viewBinding.radioButton3.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }
                }
            }
            4 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task4_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton4.visibility = View.VISIBLE

                    viewBinding.radioButton4.isChecked = isDone(taskData) == false
                    taskAdded = 4

                    if (!viewBinding.radioButton4.isChecked && energy >= 0) {

                        viewBinding.radioButton4.setOnClickListener {
                            viewBinding.radioButton4.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }
                }
            }
            5 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task5_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton5.visibility = View.VISIBLE

                    viewBinding.radioButton5.isChecked = isDone(taskData) == false
                    taskAdded = 5

                    if (!viewBinding.radioButton5.isChecked && energy >= 0) {

                        viewBinding.radioButton5.setOnClickListener {
                            viewBinding.radioButton5.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }
                }
            }
            6 -> {
                if (taskData.title != null || taskData.description != null) {
                    task = findViewById(R.id.task6_tv)
                    task.setText("Task ${taskNumber} \n${taskData.description}")
                    viewBinding.radioButton6.visibility = View.VISIBLE

                    viewBinding.radioButton6.isChecked = isDone(taskData) == false
                    taskAdded = 6

                    if (!viewBinding.radioButton6.isChecked && energy >= 0) {

                        viewBinding.radioButton6.setOnClickListener {
                            viewBinding.radioButton6.isChecked = true
                            taskData.lastCompletedDate = calendar.time
                            Log.d("Additional Activity", "Completion date: ${taskData.lastCompletedDate}")
                            updateTaskStatus(charId, taskData)
                            energy -= 10
                        }
                    }
                }
            }
        }

        Log.d("Additional Tasks", "Task Quantity: ${taskQuantity}")
        Log.d("Additional Tasks", "Task Added: ${taskAdded}")

    }
    private fun updateTaskStatus(charId: String?, taskData: TaskData){
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val taskDatabaseReference: DatabaseReference =
            firebaseDatabase.reference.child("additionalTasks/$charId/${taskData.taskID}")

        val taskDataUpdate = mapOf(
            "lastCompletedDate" to calendar.time
        )

        taskDatabaseReference.updateChildren(taskDataUpdate)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Character Details", "Task updated")
                } else {
                    Log.d("Character Details", "Task failed to update")
                }
            }
    }
    private fun saveTasks(charId: String?, title: String, description: String) {
        if (charId == null) {
            Toast.makeText(this, "Character ID is null.", Toast.LENGTH_SHORT).show()
            return
        }

        val characterTasksRef = firebaseDatabase.getReference("additionalTasks/$charId")
        val newTaskRef = characterTasksRef.push() // Create a unique key for the task under the character

        val taskData = TaskData(taskID = newTaskRef.key, title = title, description = description)

        newTaskRef.setValue(taskData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Task added successfully in database", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed Adding Task in database", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddedTask(){
        var task: TextView = findViewById(R.id.task1_tv)
        if(taskAdded == 1){
            task = findViewById(R.id.task1_tv)
            viewBinding.radioButton1.visibility = View.VISIBLE
        }else if(taskAdded == 2){
            task = findViewById(R.id.task2_tv)
            viewBinding.radioButton2.visibility = View.VISIBLE
        }else if(taskAdded == 3){
            task = findViewById(R.id.task3_tv)
            viewBinding.radioButton3.visibility = View.VISIBLE
        }else if(taskAdded == 4){
            task = findViewById(R.id.task4_tv)
            viewBinding.radioButton4.visibility = View.VISIBLE
        }else if(taskAdded == 5){
            task = findViewById(R.id.task5_tv)
            viewBinding.radioButton5.visibility = View.VISIBLE
        }else if(taskAdded == 6){
            task = findViewById(R.id.task6_tv)
            viewBinding.radioButton6.visibility = View.VISIBLE
        }

        task.setText("Task ${taskAdded} \n${taskDescription}")

    }

    private fun showAddTaskDialog(taskQuantity: Int, charId: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_additional_task)

        //set size
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val exitBtn = dialog.findViewById<ImageView>(R.id.exit_btn)
        val addTaskBtn = dialog.findViewById<Button>(R.id.add_task_btn)
        val taskTitleEt = dialog.findViewById<EditText>(R.id.task_title_et)
        val taskDescriptionEt = dialog.findViewById<EditText>(R.id.task_description_et)

        exitBtn.setOnClickListener {
            dialog.dismiss()
        }

        addTaskBtn.setOnClickListener {
            taskTitle = taskTitleEt.text.toString()
            taskDescription = taskDescriptionEt.text.toString()

            Log.d("TaskCreation", "TaskQuantity: $taskQuantity")

            if(taskTitle != "" && taskDescription != ""){
                Toast.makeText(this@AdditionalTaskActivity, "All fields complete", Toast.LENGTH_SHORT).show()
                taskAdded += 1
                Log.d("TaskCreation", "Task Added: ${taskAdded}")
                showAddedTask()
                saveTasks(charId, taskTitle, taskDescription)
                dialog.dismiss()
            }else if (taskTitle == ""){
                Toast.makeText(this@AdditionalTaskActivity, "Please enter task title", Toast.LENGTH_SHORT).show()
            }else if (taskDescription == ""){
                Toast.makeText(this@AdditionalTaskActivity, "Please enter task description", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()
    }
    private fun showToast(message: String) {
        Toast.makeText(this@AdditionalTaskActivity, message, Toast.LENGTH_SHORT).show()
    }
}