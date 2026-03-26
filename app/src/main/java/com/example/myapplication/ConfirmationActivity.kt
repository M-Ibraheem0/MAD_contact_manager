package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cs173.myapplication.R

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvEvent = findViewById<TextView>(R.id.tvEvent)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val imgConfirm = findViewById<ImageView>(R.id.imgConfirm)

        tvName.text = "Name: " + (intent.getStringExtra("name") ?: "")
        tvPhone.text = "Phone: " + (intent.getStringExtra("phone") ?: "")
        tvEmail.text = "Email: " + (intent.getStringExtra("email") ?: "")
        tvEvent.text = "Event Type: " + (intent.getStringExtra("event") ?: "")
        tvDate.text = "Date: " + (intent.getStringExtra("date") ?: "")
        tvGender.text = "Gender: " + (intent.getStringExtra("gender") ?: "")

        val imageUriStr = intent.getStringExtra("image")
        if (!imageUriStr.isNullOrEmpty()) {
            imgConfirm.setImageURI(Uri.parse(imageUriStr))
        }
    }
}
