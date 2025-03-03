package com.example.calendartodoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Collections

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoItemDao()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val todoItems: StateFlow<List<TodoItem>> = _selectedDate.flatMapLatest { date ->
        val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        todoDao.getTodosForDate(dateMillis)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTodoItem(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val dateMillis = selectedDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val currentItemsForDate = todoDao.getTodosForDate(dateMillis).stateIn(viewModelScope).value // Get current items for the date
            val newTodo = TodoItem(
                text = text,
                date = dateMillis,
                order = currentItemsForDate.size // Set order based on current list size
            )
            todoDao.insertTodo(newTodo)
        }
    }


    fun toggleTodoItemCompletion(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item =
                todoDao.getTodosForDate(selectedDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .map {
                        it.firstOrNull { item -> item.id == id }
                    }.stateIn(viewModelScope).value

            item?.let {
                val updatedItem = it.copy(isCompleted = !it.isCompleted)
                todoDao.updateTodo(updatedItem)
            }
        }
    }

    fun deleteTodoItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deleteTodoById(id)
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun moveTask(from: Int, to: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentItems =
                todoItems.value.toMutableList() // Get the current list, make mutable
            if (from in currentItems.indices && to in currentItems.indices) {
                Collections.swap(currentItems, from, to)
                todoDao.updateTodoOrder(currentItems)
            }
        }
    }
}