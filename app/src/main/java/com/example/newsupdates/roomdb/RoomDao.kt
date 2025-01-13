package com.example.newsupdates.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Upsert
    suspend fun upsertUser(user:User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select * from user")
    fun getAllUsers(): Flow<List<User>>


}