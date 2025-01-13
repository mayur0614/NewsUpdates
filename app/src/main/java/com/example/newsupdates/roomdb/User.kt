package com.example.newsupdates.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username :String,
    val password :String,
    @PrimaryKey(autoGenerate = true)
    val userId :Int = 0
)