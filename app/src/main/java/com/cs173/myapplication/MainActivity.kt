package com.cs173.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cs173.myapplication.adapter.ContactAdapter
import com.cs173.myapplication.databinding.ActivityMainBinding
import com.cs173.myapplication.model.Contact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contactList = ArrayList<Contact>()
    private var filteredList = ArrayList<Contact>()
    private lateinit var adapter: ContactAdapter

    private val addContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val contact = result.data?.getSerializableExtra("CONTACT") as? Contact
            contact?.let {
                contactList.add(it)
                saveContacts() // Persistent Save
                sortAndFilter()
            }
        }
    }

    private val editContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedContact = result.data?.getSerializableExtra("CONTACT") as? Contact
            updatedContact?.let { updated ->
                val index = contactList.indexOfFirst { it.id == updated.id }
                if (index != -1) {
                    contactList[index] = updated
                    saveContacts() // Persistent Save
                    sortAndFilter()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        loadContacts() // Load from storage

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditContactActivity::class.java)
            addContactLauncher.launch(intent)
        }
    }

    private fun saveContacts() {
        val sharedPreferences = getSharedPreferences("ContactPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(contactList)
        editor.putString("contacts", json)
        editor.apply()
    }

    private fun loadContacts() {
        val sharedPreferences = getSharedPreferences("ContactPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("contacts", null)
        
        if (json != null) {
            val type = object : TypeToken<ArrayList<Contact>>() {}.type
            val savedList: ArrayList<Contact> = gson.fromJson(json, type)
            contactList.clear()
            contactList.addAll(savedList)
        } else {
            injectInitialContacts()
        }
        sortAndFilter()
    }

    private fun injectInitialContacts() {
        val mocks = arrayListOf(
            Contact(name = "Muhammad Ali", phone = "03001234567", email = "ali@example.pk"),
            Contact(name = "Saba Qamar", phone = "03217654321", email = "saba@pro.com"),
            Contact(name = "Zaid Ahmed", phone = "03459876543", email = "zaid.a@mail.pk"),
            Contact(name = "Fatima Jinnah", phone = "03123456789", email = "fatima@history.pk")
        )
        contactList.addAll(mocks)
        saveContacts()
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter(
            filteredList,
            onContactClick = { contact ->
                val intent = Intent(this, AddEditContactActivity::class.java).apply {
                    putExtra("CONTACT", contact)
                }
                editContactLauncher.launch(intent)
            },
            onImageClick = { uri ->
                val intent = Intent(this, FullImageActivity::class.java).apply {
                    putExtra("IMAGE_URI", uri)
                }
                startActivity(intent)
            },
            onLongClick = { contact ->
                showDeleteDialog(contact)
            }
        )

        binding.rvContacts.layoutManager = GridLayoutManager(this, 2)
        binding.rvContacts.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sortAndFilter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun sortAndFilter(query: String = binding.etSearch.text.toString()) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(contactList)
        } else {
            val lowerQuery = query.lowercase()
            contactList.filter {
                it.name.lowercase().contains(lowerQuery) || it.phone.contains(lowerQuery)
            }.forEach { filteredList.add(it) }
        }
        filteredList.sortBy { it.name.lowercase() }
        adapter.updateList(filteredList)
    }

    private fun showDeleteDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Delete") { _, _ ->
                contactList.remove(contact)
                saveContacts() // Persistent Save
                sortAndFilter()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
