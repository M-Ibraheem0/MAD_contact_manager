package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cs173.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button and set listener
        // Note: Assuming a button with id btnRegister exists in activity_main or we'll add it if it's missing
        // from the previous error it seems the user wants to navigate to RegistrationActivity
        
        // Let's check if we should actually be using com.cs173.myapplication.MainActivity if that's the one with the correct layout
    }
}
