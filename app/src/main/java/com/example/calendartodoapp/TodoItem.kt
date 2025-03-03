package com.example.calendartodoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "todo_items") // Define the table name
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Auto-generated primary key
    val text: String,
    val date: Long, // Store date as Long (milliseconds since epoch)
    val isCompleted: Boolean = false,
    val order: Int = 0 //New order column
)