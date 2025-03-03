package com.example.calendartodoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items WHERE date = :date ORDER BY `order` ASC") // Corrected: Order by 'order'
    fun getTodosForDate(date: Long): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoItem: TodoItem)

    @Update
    suspend fun updateTodo(todoItem: TodoItem)

    @Delete
    suspend fun deleteTodo(todoItem: TodoItem)

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoById(id: Int)


    @Transaction // VERY IMPORTANT: Use @Transaction for atomic updates
    suspend fun updateTodoOrder(todos: List<TodoItem>) {
        todos.forEachIndexed { index, todoItem ->
            updateTodo(todoItem.copy(order = index)) // Update the 'order' column
        }
    }
}