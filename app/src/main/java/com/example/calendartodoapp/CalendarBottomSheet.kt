package com.example.calendartodoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarBottomSheet(
    bottomSheetState: SheetState,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val dialogState = rememberMaterialDialogState() // State for the MaterialDialog
    var selectedDate by remember { mutableStateOf(initialDate) } // Use remember for selectedDate

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(
                onClick = { dialogState.show() },
                modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally) // Center the button
            ) {
                Text("Select Date", color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))

        }

        // MaterialDialog to hold the datepicker
        MaterialDialog(
            dialogState = dialogState,
            backgroundColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            buttons = {
                positiveButton(
                    text = "OK",
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                    onClick = {
                        onDateSelected(selectedDate)
                        dialogState.hide()  // Hide dialog after selection

                    }
                )
                negativeButton(
                    text = "Cancel",
                    textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.secondary),
                    onClick = {
                        dialogState.hide() //hide dialog
                    }
                )
            }
        ) {
            datepicker(
                initialDate = initialDate,
                title = "Pick a date",
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                    dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateInactiveBackgroundColor = MaterialTheme.colorScheme.surface,
                    dateInactiveTextColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                selectedDate = it // Update selectedDate when a date is picked

            }
        }
    }
}