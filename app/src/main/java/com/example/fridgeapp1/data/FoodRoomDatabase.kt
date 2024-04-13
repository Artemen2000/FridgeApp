package com.example.fridgeapp1.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodItem::class], version = 1, exportSchema = false)
abstract class FoodRoomDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDao

    companion object{
        @Volatile
        private var INSTANCE: FoodRoomDatabase? = null

        fun getDatabase(context: Context): FoodRoomDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodRoomDatabase::class.java,
                    "food_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}