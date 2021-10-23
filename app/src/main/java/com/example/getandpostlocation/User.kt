package com.example.getandpostlocation
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("location")
    val location: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pk")
    val pk: Int?=null
)