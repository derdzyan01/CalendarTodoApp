package com.example.calendartodoapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodoItemComposable(
    todoItem: TodoItem,
    onToggleCompletion: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isCompleted = todoItem.isCompleted
    val backgroundColor by animateColorAsState(
        targetValue = if (isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface,
        label = "Background color animation"
    )
    val textDecoration by animateFloatAsState(
        targetValue = if (isCompleted) 0.5f else 1f, label = "Text decoration animation"
    )

    val haptic = LocalHapticFeedback.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)

    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleCompletion(todoItem.id)
                },
                modifier = Modifier.padding(end = 2.dp)
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle, // Changed icon here
                    contentDescription = if (isCompleted) "Mark Incomplete" else "Mark Complete",
                    tint = if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)

                )
            }
            Text(
                text = todoItem.text,
                color = Color.White.copy(alpha = textDecoration),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(onClick = { onDelete(todoItem.id) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(0.dp)

                )
            }
        }
    }
}
