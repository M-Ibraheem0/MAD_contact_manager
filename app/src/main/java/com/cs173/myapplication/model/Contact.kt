package com.cs173.myapplication.model

import java.io.Serializable

data class Contact(
    var id: Long = System.currentTimeMillis(),
    var name: String,
    var phone: String,
    var email: String,
    var imageUri: String? = null
) : Serializable
