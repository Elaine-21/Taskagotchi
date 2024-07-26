package com.mobdeve.s13.martin.elaine.taskagotchi

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s13.martin.elaine.taskagotchi.adapter.HomeAdapter
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityHomeBinding
import com.mobdeve.s13.martin.elaine.taskagotchi.databinding.ActivityRegisterBinding

class HomeActivity : AppCompatActivity() {
//    private lateinit var dbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewBinding: ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val button1: Button = findViewById(R.id.encyclopediaBtn)

        setButtonBackground(button1, R.drawable.encyclopedia_icon)

        //recyclerview part
//        viewBinding.homeRecyclerView.adapter = HomeAdapter(DataGenerator.loadData())
//
//        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
//
//        viewBinding.homeRecyclerView.layoutManager = linearLayoutManager

    }

    private fun setButtonBackground(button: Button, imageResId: Int) {
        val borderDrawable = ContextCompat.getDrawable(this, R.drawable.circle_border)
        val imageDrawable = ContextCompat.getDrawable(this, imageResId)
        val resizedImageDrawable = resizeDrawable(imageDrawable!!, 10, 10) // Adjust the size as needed
        val layers = arrayOf<Drawable>(borderDrawable!!, resizedImageDrawable)
        val layerDrawable = LayerDrawable(layers)
        button.background = layerDrawable
    }

    private fun resizeDrawable(drawable: Drawable, width: Int, height: Int): Drawable {
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
            return BitmapDrawable(resources, resizedBitmap)
        }
        return drawable
    }
}