package com.example.fridgeapp1.data

import android.content.Context
import android.icu.text.DateFormat.getDateInstance
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.text.method.DateTimeKeyListener
import android.widget.DatePicker
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fridgeapp1.FridgeApplication
import java.time.Instant
import java.util.Calendar
import javax.inject.Inject

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


