package com.example.gimapp.dependenciesInyection.room

import android.content.Context
import com.example.gimapp.data.database.daos.DaosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDaosDatabase(@ApplicationContext context: Context): DaosDatabase {
        return DatabaseProvider.getInstance(context)
    }

}