package com.example.fridgeapp1.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * from fooditem ORDER BY name ASC")
    fun getItems(): Flow<List<FoodItem>>

    @Query("SELECT * from fooditem WHERE id = :id")
    fun getItem(id: Int): Flow<FoodItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: FoodItem)

    @Update
    suspend fun update(item: FoodItem)

    @Delete
    suspend fun delete(item: FoodItem)
}