package com.example.calendartodoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoBottomSheet(
    bottomSheetState: SheetState,
    onAddTodo: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current // Get keyboard controller


    // Show keyboard when the bottom sheet appears
    LaunchedEffect(bottomSheetState) {
        if (bottomSheetState.isVisible) {
            focusRequester.requestFocus()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Add To-Do",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
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
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surface, // Match background of sheet
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface, // Match background of sheet
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester), // Attach FocusRequester
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.isNotBlank()) {
                            onAddTodo(text)
                            text = "" // Clear the text field after adding
                        } else{
                            keyboardController?.hide() //hide keyboard
                            onDismiss()
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp).navigationBarsPadding())
        }
    }
}