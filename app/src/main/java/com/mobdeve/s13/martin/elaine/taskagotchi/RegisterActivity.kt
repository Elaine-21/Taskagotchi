package com.mobdeve.s13.martin.elaine.taskagotchi

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityRegisterBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.model.UserData

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding: ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        viewBinding.registerBtn.setOnClickListener {
            val registerUsername = viewBinding.registerUsernameInput.text.toString()
            val registerPass1 = viewBinding.registerPass1Input.text.toString()
            val registerPass2 = viewBinding.registerPass2Input.text.toString()

            if(registerUsername.isNotEmpty() && registerPass1.isNotEmpty() && registerPass2.isNotEmpty()){
                if(registerPass2 == registerPass1){
                    registerUser(registerUsername, registerPass1)
                }else{
                    Toast.makeText(this@RegisterActivity, "Password DO NOT match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@RegisterActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.loginNowTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }


    private fun registerUser(username: String, password: String){
        // Looks for a child node where the username field matches the given username
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //if the data does not exist it will create a new UserData
                if(!snapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@RegisterActivity, "User successfully registered!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else{
                    //else if will display a toast message indicating that the user already exist
                    Toast.makeText(this@RegisterActivity, "User already exist", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }



}