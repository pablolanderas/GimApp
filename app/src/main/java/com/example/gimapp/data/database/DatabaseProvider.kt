package com.example.gimapp.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var INSTANCE = null

    fun getDatabase(context: Context) {
        return INSTANCE ?: synchronized(this) {
        }
    }
}