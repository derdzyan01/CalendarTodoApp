package com.example.calendartodoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(onAddTodo: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text("Add To-Do", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(
                            "To-do item",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    colors = TextFieldDefaults.colors( // Corrected: Use TextFieldDefaults.colors
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onAddTodo(text)
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Add", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                Text("Cancel", color = MaterialTheme.colorScheme.secondary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface, // Set background color
        shape = RoundedCornerShape(16.dp), // Rounded corners
        modifier = Modifier.fillMaxWidth(0.85f)
    )
}