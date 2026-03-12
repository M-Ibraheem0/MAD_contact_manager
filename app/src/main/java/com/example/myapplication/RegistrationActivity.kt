package com.cs173.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerEvent: Spinner
    private lateinit var btnDate: Button
    private lateinit var tvDate: TextView
    private lateinit var radioGender: RadioGroup
    private lateinit var checkTerms: CheckBox
    private lateinit var imgPreview: ImageView
    private lateinit var btnImage: Button
    private lateinit var btnSubmit: Button

    private var selectedDate = ""
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            imgPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        spinnerEvent = findViewById(R.id.spinnerEvent)
        btnDate = findViewById(R.id.btnDate)
        tvDate = findViewById(R.id.tvDate)
        radioGender = findViewById(R.id.radioGender)
        checkTerms = findViewById(R.id.checkTerms)
        imgPreview = findViewById(R.id.imgPreview)
        btnImage = findViewById(R.id.btnImage)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Spinner setup
        val events = arrayOf("Select Event", "Seminar", "Workshop", "Conference", "Webinar", "Cultural Event")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, events)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEvent.adapter = adapter

        // Date picker
        btnDate.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                selectedDate = "$day/${month + 1}/$year"
                tvDate.text = "Selected: $selectedDate"
            }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH)).show()
        }

        // Image picker
        btnImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        // Submit
        btnSubmit.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val eventType = spinnerEvent.selectedItem.toString()
        val selectedGenderId = radioGender.checkedRadioButtonId

        if (name.isEmpty()) { Toast.makeText(this, "Enter full name", Toast.LENGTH_SHORT).show(); return }
        if (phone.isEmpty() || phone.length < 10) { Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show(); return }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show(); return }
        if (eventType == "Select Event") { Toast.makeText(this, "Select an event type", Toast.LENGTH_SHORT).show(); return }
        if (selectedDate.isEmpty()) { Toast.makeText(this, "Select event date", Toast.LENGTH_SHORT).show(); return }
        if (selectedGenderId == -1) { Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show(); return }
        if (!checkTerms.isChecked) { Toast.makeText(this, "Accept terms and conditions", Toast.LENGTH_SHORT).show(); return }

        val gender = findViewById<RadioButton>(selectedGenderId).text.toString()

        AlertDialog.Builder(this)
            .setTitle("Confirm Registration")
            .setMessage("Are you sure you want to submit your registration?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(this, ConfirmationActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("phone", phone)
                intent.putExtra("email", email)
                intent.putExtra("event", eventType)
                intent.putExtra("date", selectedDate)
                intent.putExtra("gender", gender)
                selectedImageUri?.let { intent.putExtra("image", it.toString()) }
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
