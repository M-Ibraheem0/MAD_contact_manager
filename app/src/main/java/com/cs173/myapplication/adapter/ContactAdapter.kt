package com.cs173.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cs173.myapplication.databinding.ItemContactBinding
import com.cs173.myapplication.model.Contact

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onContactClick: (Contact) -> Unit,
    private val onImageClick: (String) -> Unit,
    private val onLongClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.binding.tvContactName.text = contact.name
        holder.binding.tvContactPhone.text = contact.phone

        if (!contact.imageUri.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Uri.parse(contact.imageUri))
                .circleCrop()
                .into(holder.binding.ivContactPhoto)
        } else {
            holder.binding.ivContactPhoto.setImageResource(android.R.drawable.ic_menu_camera)
        }

        holder.itemView.setOnClickListener { onContactClick(contact) }
        holder.binding.ivContactPhoto.setOnClickListener { 
            contact.imageUri?.let { uri -> onImageClick(uri) }
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(contact)
            true
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun updateList(newList: List<Contact>) {
        contacts = newList
        notifyDataSetChanged()
    }
}
