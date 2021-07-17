package com.joule.tokobarang.data

data class RegisterResponse(val success : Boolean, val message: String, val data : Data)

data class Data(val email: String, val updated_at : String, val created_at: String, val id: Int)
