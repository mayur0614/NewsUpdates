package com.example.newsupdates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsupdates.roomdb.User
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository):ViewModel() {
    fun getUser()=repository.getAllUser().asLiveData(
        viewModelScope.coroutineContext
    )

    fun upsertUser(user: User){
        viewModelScope.launch {
            repository.upsertUser(user)
        }
    }


    fun deleteUser(user:User){
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}