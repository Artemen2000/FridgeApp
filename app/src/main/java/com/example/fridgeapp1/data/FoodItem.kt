package com.example.fridgeapp1.data

import android.icu.text.DateFormat.getDateInstance
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "expiresAt")
    var expiresAt: Long,
) {
    fun getFormattedDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = expiresAt
        return getDateInstance().format(calendar.time)
    }
}


