package com.example.newsupdates.viewmodel

import com.example.newsupdates.roomdb.User
import com.example.newsupdates.roomdb.UserDatabase

class Repository(private val db:UserDatabase) {

    suspend fun upsertUser(user: User){
        db.dao.upsertUser(user)
    }

    suspend fun deleteUser(user: User){
        db.dao.deleteUser(user)
    }
    fun getAllUser()  = db.dao.getAllUsers()

}