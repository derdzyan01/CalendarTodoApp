package com.example.calendartodoapp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoListScreen(viewModel: TodoViewModel) { //Removed padding parameter
    val todos by viewModel.todoItems.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (todos.isEmpty()) {
            EmptyState()
        } else {
            val state = rememberReorderableLazyListState(onMove = { from, to ->
                viewModel.moveTask(from.index, to.index)
            })
            LazyColumn(
                state = state.listState,
                modifier = Modifier.reorderable(state),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(todos, { item -> item.id }) { todo ->
                    ReorderableItem(state, key = todo.id) { isDragging ->
                        TodoItemComposable(
                            todoItem = todo,
                            onToggleCompletion = { id -> viewModel.toggleTodoItemCompletion(id) },
                            onDelete = { id -> viewModel.deleteTodoItem(id) },
                            modifier = Modifier.detectReorderAfterLongPress(state)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No to-dos here yet!", //Changed text
            style = MaterialTheme.typography.headlineSmall, //Changed style
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Tap the + button to add one.",
            style = MaterialTheme.typography.bodyLarge, //Changed Style
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}