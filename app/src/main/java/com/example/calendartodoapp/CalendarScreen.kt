package com.example.calendartodoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CalendarScreen(viewModel: TodoViewModel) {
    val dialogState = rememberMaterialDialogState()
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { dialogState.show() },
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Text(
                text = viewModel.selectedDate.value.format(dateFormatter),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    MaterialDialog(
        dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.surface, // Match background
        shape = RoundedCornerShape(12.dp), // Rounded corners
        buttons = {
            positiveButton(
                text = "Ok",
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
            negativeButton(
                text = "Cancel",
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary)
            )
        }
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp)) // Add some space
            datepicker(
                initialDate = viewModel.selectedDate.value,
                title = "Pick a date",
                colors = com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                    dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                    dateInactiveTextColor = MaterialTheme.colorScheme.onSurface
                ),
                yearRange = IntRange(1900, 2100) // Optional year range

            ) {
                viewModel.selectDate(it)
            }
        }
    }
}