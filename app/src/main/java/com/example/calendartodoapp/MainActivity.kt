package com.example.calendartodoapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calendartodoapp.ui.theme.CalendarTodoAppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : FragmentActivity() {

    private lateinit var biometricHelper: BiometricHelper

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricHelper = BiometricHelper(this)

        setContent {
            CalendarTodoAppTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                var isAuthenticating by remember { mutableStateOf(false) } // Track authentication in progress

                LaunchedEffect(Unit) {
                    if (biometricHelper.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                        isAuthenticating = true
                        biometricHelper.authenticate(
                            activity = this@MainActivity,
                            onSuccess = {
                                isAuthenticated = true
                                isAuthenticating = false
                            },
                            onFailure = { _, errString ->
                                Toast.makeText(this@MainActivity, errString.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                isAuthenticating = false
                                // Handle failure (e.g., stay on a locked screen)
                            },
                            onError = { errorCode, errString ->
                                Toast.makeText(this@MainActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                                isAuthenticating = false
                                // Handle errors (e.g., show an error message, exit the app)
                                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                                    // No biometrics enrolled.  You might want to handle this
                                    // by showing a message and allowing the user to proceed
                                    // without biometric authentication, or exiting the app.
                                    isAuthenticated = true //For this example we go next
                                } else if (errorCode == BiometricPrompt.ERROR_HW_UNAVAILABLE || errorCode == BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL) {
                                    isAuthenticated = true //For this example we go next
                                    // Handle other errors appropriately
                                }
                            }
                        )
                    } else {
                        isAuthenticated = true
                    }
                }

                if (isAuthenticating) {
                    // Show a loading indicator or some UI while authenticating
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (isAuthenticated) {
                    // Show the main content of your app
                    MainContent()
                } else {
                    // Show a locked screen or some other UI indicating that authentication failed
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Authentication Failed")
                    }
                }
            }
        }
    }


    // Inside MainActivity's setContent
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent() {
        val viewModel: TodoViewModel = viewModel()
        var showAddBottomSheet by remember { mutableStateOf(false) }
        val addTodoBottomSheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        // State for calendar bottom sheet
        var showCalendarBottomSheet by remember { mutableStateOf(false) }
        val calendarBottomSheetState = rememberModalBottomSheetState()
        val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle() // Get selected date


        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddBottomSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Date selection bar
                DateSelectionBar(
                    selectedDate = selectedDate,
                    onDateClick = { showCalendarBottomSheet = true } // Show calendar sheet
                )

                TodoListScreen(viewModel = viewModel) // Pass viewModel to TodoListScreen
            }
        }

        // Add To-Do Bottom Sheet
        AnimatedVisibility(
            visible = showAddBottomSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            AddTodoBottomSheet(
                bottomSheetState = addTodoBottomSheetState,
                onAddTodo = { text ->
                    viewModel.addTodoItem(text)
                    scope.launch {
                        addTodoBottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!addTodoBottomSheetState.isVisible) {
                            showAddBottomSheet = false
                        }
                    }
                },
                onDismiss = {
                    scope.launch {
                        addTodoBottomSheetState.hide()
                    }.invokeOnCompletion {
                        if (!addTodoBottomSheetState.isVisible) {
                            showAddBottomSheet = false
                        }
                    }
                }
            )
        }

        // Calendar Bottom Sheet
        AnimatedVisibility(
            visible = showCalendarBottomSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            CalendarBottomSheet(
                bottomSheetState = calendarBottomSheetState,
                initialDate = selectedDate, // Pass the current selected date
                onDateSelected = { newDate ->
                    viewModel.selectDate(newDate) // Update ViewModel with selected date
                    scope.launch { calendarBottomSheetState.hide() } // Hide on selection
                        .invokeOnCompletion {
                            if (!calendarBottomSheetState.isVisible) {
                                showCalendarBottomSheet = false // Reset the show state
                            }

                        }
                },
                onDismiss = {
                    scope.launch { calendarBottomSheetState.hide() }.invokeOnCompletion {
                        if (!calendarBottomSheetState.isVisible) {
                            showCalendarBottomSheet = false
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun DateSelectionBar(selectedDate: LocalDate, onDateClick: () -> Unit) {
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
        val formattedDate = selectedDate.format(dateFormatter)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onDateClick) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select Date",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}