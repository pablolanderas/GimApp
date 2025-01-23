package com.example.gimapp.dependenciesInyection.room

import android.content.Context
import androidx.room.Room
import com.example.gimapp.data.database.daos.DaosDatabase

object DatabaseProvider {

    private var instance: DaosDatabase? = null

    fun getInstance(context: Context): DaosDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                DaosDatabase::class.java,
                "fitness_db"
            ).build()
        }
        return instance!!
    }

}