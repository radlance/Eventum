package com.radlance.eventum.presentation.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.radlance.eventum.domain.event.EventCart

@Composable
fun CartItemContainer(
    event: EventCart,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    content: @Composable RowScope.(EventCart) -> Unit,
) {
    var showQuantityChanger by remember { mutableStateOf(false) }
    var showRemoveElement by remember { mutableStateOf(false) }

    val gestureModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { change, dragAmount ->
            if (dragAmount > 10f) {
                showQuantityChanger = true
                showRemoveElement = false
            } else if (dragAmount < -10f) {
                showRemoveElement = true
                showQuantityChanger = false
            }
            change.consume()
        }
    }

    Row(modifier = gestureModifier) {
        AnimatedVisibility(visible = showQuantityChanger) {
            ChangeQuantityComponent(
                quantity = event.quantity,
                onIncrementClick = onIncrement,
                onDecrementClick = onDecrement,
                modifier = Modifier.padding(end = 10.dp)
            )
        }

        content(event)
        AnimatedVisibility(visible = showRemoveElement) {
            RemoveComponent(
                onRemoveClick = onRemove,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
        }
    }
}