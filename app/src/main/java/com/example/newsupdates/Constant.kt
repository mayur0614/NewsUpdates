package com.example.newsupdates

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.room.Room

import com.example.newsupdates.roomdb.UserDatabase
import com.example.newsupdates.viewmodel.Repository
import com.example.newsupdates.viewmodel.UserViewModel

object Constant {
    //val apiKey = "12a127a13b1d4735aa058e0de7ba0e3a"
    val apiKey = "0887aa19c1244194900c5aa4f5a7f846"

    private lateinit var applicationContext: Context
    fun initialize(context: Context) {

        applicationContext = context
    }


    val db: UserDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            name = "user.db"
        ).build()
    }


    val viewModel: UserViewModel by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(Repository(db)) as T
            }
        }.let { factory ->
            ViewModelProvider(ViewModelStore(), factory).get(UserViewModel::class.java)
        }
    }

}