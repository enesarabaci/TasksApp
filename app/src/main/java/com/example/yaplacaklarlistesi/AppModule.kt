package com.example.yaplacaklarlistesi

import android.content.Context
import androidx.room.Room
import com.example.yaplacaklarlistesi.Room.Dao
import com.example.yaplacaklarlistesi.Room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDatabase(@ApplicationContext context: Context) : Database =
        Room.databaseBuilder(
            context,
            Database::class.java,
            "Events_Database"
        ).build()

    @Singleton
    @Provides
    fun getDao(database: Database) : Dao = database.dao()

}