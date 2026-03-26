package com.cs173.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cs173.myapplication.databinding.ActivityAddEditContactBinding
import com.cs173.myapplication.model.Contact

class AddEditContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditContactBinding
    private var selectedImageUri: Uri? = null
    private var contactToEdit: Contact? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Persist permission for the URI if possible, or just load it
            selectedImageUri = it
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.ivPhoto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactToEdit = intent.getSerializableExtra("CONTACT") as? Contact
        
        if (contactToEdit != null) {
            binding.tvTitle.text = "Edit Contact"
            binding.etName.setText(contactToEdit?.name)
            binding.etPhone.setText(contactToEdit?.phone)
            binding.etEmail.setText(contactToEdit?.email)
            contactToEdit?.imageUri?.let {
                selectedImageUri = Uri.parse(it)
                Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop()
                    .into(binding.ivPhoto)
            }
        }

        binding.btnPickImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please enter name and phone", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(
            id = contactToEdit?.id ?: System.currentTimeMillis(),
            name = name,
            phone = phone,
            email = email,
            imageUri = selectedImageUri?.toString()
        )

        val intent = Intent().apply {
            putExtra("CONTACT", contact)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}
