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
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData

class HomeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var characterReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var homeArrayList: ArrayList<HomeData>
    private lateinit var viewBinding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter

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
            val intent = Intent(this@HomeActivity, CharacterCollection::class.java)
            startActivity(intent)
            onPause()
        }

        //fetching of data from the firebase users
        firebaseDatabase = FirebaseDatabase.getInstance()
        if (userId != null) {
            databaseReference = firebaseDatabase.getReference("users").child(userId)
            getCharacterId()
        } else {
            showToast("User ID is null.")
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("HomeActivity", "Return button clicked")
                moveTaskToBack(true)
            }
        })
    }


    //fonts
    private fun applyFont() {
        val luckiest_guy: Typeface? = ResourcesCompat.getFont(this, R.font.luckiest_guy)

        // Applying luckiest_guy font to text views
        val home_title_tv: TextView = findViewById(R.id.home_title_tv)
        home_title_tv.typeface = luckiest_guy
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
                        homeArrayList.add(it)
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

    //function to display toast
    private fun showToast(message: String) {
        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
    }
}
