package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.mobdeve.s13.martin.elaine.taskagotchi.adapter.HomeAdapter
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.HomeData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.TaskData
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var characterReference: DatabaseReference
    private lateinit var taskagotchiReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var homeArrayList: ArrayList<HomeData>
    private lateinit var viewBinding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private val missedDaysList = mutableListOf<Int>()
//    private var userId : String? = null
//    private var energy: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        applyFont()

        // Initialize RecyclerView
        viewBinding.homeRecyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.homeRecyclerView.setHasFixedSize(true)
        // Retrieve data from the Intent
        val username = intent.getStringExtra("username")
        val userId = intent.getStringExtra("userId")

        //adapter
        homeArrayList = arrayListOf()
        homeAdapter = HomeAdapter(homeArrayList)
        var adapter = homeAdapter
        viewBinding.homeRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: HomeAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val selectedCharacter = homeArrayList[position]
//                Toast.makeText(this@HomeActivity, "You Clicked on item no. $position", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@HomeActivity, CharacterDetailsActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("characterId", selectedCharacter.id)
                intent.putExtra("characterName", selectedCharacter.name)
                intent.putExtra("characterPicURL", selectedCharacter.picURL)
                intent.putExtra("characterStatus", selectedCharacter.status)
                intent.putExtra("characterStreak", selectedCharacter.streak ?: 0)
                intent.putExtra("characterEnergy", selectedCharacter.energy ?: 0)
                intent.putExtra("characterDebuff", selectedCharacter.debuff)
                val taskIds = selectedCharacter.tasksIDList?: emptyList()
                intent.putStringArrayListExtra("taskIds", ArrayList(taskIds))
//                val charIds = selectedCharacter.charEvolution?: emptyList()
//                intent.putStringArrayListExtra("charEvolution", ArrayList(selectedCharacter.charEvolution))
//                intent.putStringArrayListExtra("charEvolution", ArrayList(selectedCharacter.charEvolution))
                val charIds = selectedCharacter.charEvolution?: emptyList()
                intent.putStringArrayListExtra("charIds", ArrayList(charIds))

                taskIds.forEachIndexed { index, taskId ->
                    Log.d("HomeActivity", "Task ID[$index]: $taskId")
                }
                Log.d("HomeActivity", "Selected Character ID: ${selectedCharacter.streak}")
                Log.d("HomeActivity", "Selected Character ID: ${selectedCharacter.energy}")
                //delete log later
                //Log.d("HomeActivity", "Selected Character ID: ${selectedCharacter.id}")
                startActivity(intent)
                onPause()
            }
        })


        viewBinding.addCharacterBtn.setOnClickListener {
            val intent = Intent(this@HomeActivity, CharacterCreation::class.java)
            intent.putExtra("username", username)
            intent.putExtra("userId", userId)
            startActivity(intent)
            onPause()//possible cause of problem when return button on the phone i clicked as it destroys this activity
        }

        viewBinding.signoutBtn.setOnClickListener {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewBinding.encyclopediaBtn.setOnClickListener {
            val intent = Intent(this@HomeActivity, CharacterEncyclopediaActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            onPause()
        }

        //fetching of data from the firebase users
        firebaseDatabase = FirebaseDatabase.getInstance()
        if (userId != null) {
            databaseReference = firebaseDatabase.getReference("users").child(userId)
            taskagotchiReference = firebaseDatabase.getReference("taskagotchiCharacter").child(userId)
//            homeArrayList.clear()
//            getCharacterId()
        } else {
            showToast("User ID is null.")
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("HomeActivity", "Return button clicked")
                moveTaskToBack(true)
            }
        })

        //backend update of debuff and status
        Log.d("HomeActivity_DEB", "user ID: $userId")
        getCharacterIdForDebuffStatusEnergyStreak(userId)
    }

    override fun onResume() {
        super.onResume()
        getCharacterId()
    }

    private fun updateEnergy(charID: String?, charData: HomeData){
        Log.d("HomeActivity_UE", "charID: $charID")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val today = calendar.time

        charData.energyRestorationDate?.let { restorationDate ->
            if (!restorationDate.after(today)) {
                if (charID != null) {
                    taskagotchiReference.child(charID).child("energy").setValue(50)
                    Log.d("HomeActivity_UE", "Energy restored to 50 for character ID: $charID")
                }
            }
        }
    }

    //    private fun checkStreak(userId: String?, characterId: String?, charData:HomeData){
//        if(charData.status == "Healthy"){
//            updateStreak(1, userId, characterId, charData)
//        }else{
//            updateStreak(2, userId, characterId, charData)
//        }
//    }
    private fun updateStreak(/*option: Int,*/ userId: String?, characterId: String?, charData: HomeData){
//        if(option == 1) {
        val dateCreation = charData.creationDate
        if(charData.status == "Healthy") {
            if (dateCreation != null) {
                val currentDate = Calendar.getInstance().time
                val diffInMillis = currentDate.time - dateCreation.time
                val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
                Log.d("Streak", "diffInDays: $diffInDays")
                Log.d("Streak", "creationDate: $dateCreation")

                var charStreak =charData.streak//added

                if (diffInDays >= 7) {
                    charStreak = charStreak?.plus(1)
                    Log.d("Streak", "CharacterStreak: $charStreak")


                    if (characterId != null) {
                        taskagotchiReference.child(characterId).child("streak").setValue(charStreak)
                    }

                    updateCreationDate(userId, characterId)
                }
            }
        }else{
            updateCreationDate(userId, characterId)
        }

    }
    private fun updateCreationDate(userId: String?, characterId: String?){
        val taskagotchiData: DatabaseReference = firebaseDatabase.reference.child("taskagotchiCharacter/$userId")//cause for problems if more than 1 item in the recyclerView
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

    private fun getCharacterIdForDebuffStatusEnergyStreak(userId: String?){
        Log.d("HomeActivity_DEB", "user ID: $userId")
        taskagotchiReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (characterSnapshot in snapshot.children) {
                        val characterId = characterSnapshot.key // e.g., "112" or "113"
                        val characterData = characterSnapshot.getValue(HomeData::class.java)
                        Log.d("HomeActivity_DEB", "Character ID: $characterId, Data: $characterData")
                        if (characterId != null && characterData != null) {
                            Log.d("HomeActivity_UE", "updateEnergy")
                            updateEnergy(characterId, characterData)
//                            checkStreak(userId, characterId, characterData)
                            updateStreak(userId, characterId, characterData)
                            val tasksIDList = characterData.tasksIDList
                            Log.d("HomeActivity_DEB", "tasksIDList: $tasksIDList")

                            if (!tasksIDList.isNullOrEmpty()) {
                                missedDaysList.clear()

                                for (taskId in tasksIDList) {
                                    val taskReference = firebaseDatabase.getReference("tasks/$characterId").child(taskId)
                                    taskReference.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                val taskData = snapshot.getValue(TaskData::class.java)
                                                taskData?.let {
                                                    calculateMissedDays(it)
                                                }
                                            } else {
                                                showToast("Task data does not exist for task ID: $taskId")
                                            }

                                            // Once all tasks are processed, update the character debuff and status
                                            if (taskId == tasksIDList.last()) {
                                                updateMissedDays(characterId, userId)
                                                missedDaysList.clear()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            showToast("Database Error: ${error.message}")
                                        }
                                    })
                                }
                            } else {
                                showToast("No tasks found for this character.")
                            }
                        } else {
                            showToast("Character ID or data is null.")
                        }
                    }
                } else {
                    showToast("No characters found.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }

    private fun calculateMissedDays(taskData: TaskData) {
        Log.d("HomeActivity_DEBB", "calculateMissedDays")
        if (taskData.lastCompletedDate == null) {
            return
        }

        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        calendar.time = taskData.lastCompletedDate

        if (calendar.time.before(currentDate)) {
            when (taskData.frequency) {
                "Everyday" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                "Once a Week" -> calendar.add(Calendar.DAY_OF_YEAR, 7)
                "Every Other Day" -> calendar.add(Calendar.DAY_OF_YEAR, 2)
            }

            val missedCount = currentDate.time - calendar.time.time
            val days = TimeUnit.MILLISECONDS.toDays(missedCount)
            Log.d("HomeActivity", "MissedCOunt: $days")

            Log.d("Debuff Calculation", "currentDate: ${currentDate.time} --- dateToday: ${calendar.time.time}")
            Log.d("Debuff Calculation", "Missed ${days.toInt()} times since last completion.")

            taskData.missCntr = days.toInt()
            missedDaysList.add(days.toInt())
        }
    }

    private fun updateMissedDays(characterId: String?, userId: String?) {
        Log.d("HomeActivity_DEBB", "updateMissedDays")
        var characterDebuff: String? = null
        var characterStatus: String? = null
        val maxMissedDays = missedDaysList.maxOrNull() ?: 0
        Log.d("HomeActivity", "CharacterId uMD: $characterId")
        Log.d("Debuff Calculation", "Highest number of missed days: $maxMissedDays")

        when (maxMissedDays) {
            0 -> {
                characterDebuff = "None"
                characterStatus = "Healthy"
            }
            1 -> {
                characterDebuff = "x0.30"
                characterStatus = "Weak"
            }
            2 -> {
                characterDebuff = "x0.70"
                characterStatus = "Sick"
            }
            else -> {
                characterDebuff = "x1.00"
                characterStatus = "Debuffed"
            }
        }

        updateCharacterData(characterId, userId, characterDebuff, characterStatus)
    }

    private fun updateCharacterData(charID: String?, userId: String?, characterDebuff: String?, characterStatus: String?) {
        Log.d("HomeActivity_DEB", "updateCharacterData, CharacterId CD: $charID")
        if (userId == null || charID == null) {
            Log.d("Character Details", "userId or charID is null")
            return
        }

        taskagotchiReference.child(charID).child("debuff").setValue(characterDebuff)
        taskagotchiReference.child(charID).child("status").setValue(characterStatus)
    }

    //getting the data inside the user charactersIdList then passing it
    private fun getCharacterId() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    val charactersIdList = userData?.charactersIdList

                    charactersIdList?.let { ids ->
                        if (ids.isNotEmpty()) {
                            // Fetch each character's data
                            homeArrayList.clear()
                            for (characterId in ids) {
                                getCharacterData(userData.id, characterId)
                            }
                        } else {
                            showToast("No characters found for this user.")
                        }
                    } ?: showToast("Characters ID list is null.")
                } else {
                    showToast("User data does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }

    //fetching the data that is the same in the taskagotchiCharacter
    private fun getCharacterData(userID: String?, characterId: String) {
        characterReference = firebaseDatabase.getReference("taskagotchiCharacter/$userID").child(characterId)
        characterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val homeData = snapshot.getValue(HomeData::class.java)
                    homeData?.let {
//                        debuffStatus(it)
                        homeArrayList.add(it)//i think i need to change it here
//                        debuffStatus(it)//it is passing more than one
                        Log.d("HomeActivity", "it = $it")
                        homeAdapter.notifyDataSetChanged() // signals change to the adapter
                    } ?: showToast("Taskagotchi data is null.")
                } else {
//                    showToast("Character data does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Database Error: ${error.message}")
            }
        })
    }


    //what if i update it first then do the homeadapter stuff

    //font
    private fun applyFont() {
        val luckiest_guy: Typeface? = ResourcesCompat.getFont(this, R.font.luckiest_guy)

        // Applying luckiest_guy font to text views
        val home_title_tv: TextView = findViewById(R.id.home_title_tv)
        home_title_tv.typeface = luckiest_guy
    }
    //function to display toast
    private fun showToast(message: String) {
        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
    }
}
