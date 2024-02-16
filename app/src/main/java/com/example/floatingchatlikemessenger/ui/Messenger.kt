package com.example.floatingchatlikemessenger.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.floatingchatlikemessenger.Model.Message
import com.example.floatingchatlikemessenger.R
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Messenger(
    appearance: Appearance,
    showChatButton: Boolean = true,
    messages: List<Message>,
    onClick: (Message) -> Unit,
    onDismissButtonChat: (Boolean) -> Unit,
    deleteComment: (Message) -> Unit
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val offsetX = appearance.offsetX
    val offsetY = appearance.offsetY
    val boxSize = remember { mutableStateOf(IntSize(0, 0)) }

    val density = LocalDensity.current
    val pxValue = 44.dp * density.density
    var isExpanded by remember { mutableStateOf(false) }
    val dismissColor = remember {
        mutableStateOf(Color.Black)
    }
    val dismissColorContainer = remember {
        mutableStateOf(Color.Red)
    }


    var sizeTopBar by remember { mutableStateOf(IntSize.Zero) }
    var positionInRootClearButton by remember { mutableStateOf(Offset.Zero) }

    val displayMetrics = context.resources.displayMetrics
    val screenWidthPx = displayMetrics.widthPixels
    val screenHeightPx = displayMetrics.heightPixels
    val dragEnd = remember {
        mutableStateOf(false)
    }
    val midScreenX = screenWidthPx.dp / 2
    val bottomScreenY = screenHeightPx.dp - 100.dp
    val brush = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(0.3f),
            Color.Black.copy(0.2f)
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, 200f)
    )

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            val targetX = boxSize.value.width.toFloat() - 44.dp.toPx(density)
            val anim1 = async { offsetX.animateTo(targetX) }
            val anim2 = async { offsetY.animateTo(100f) }
            anim1.await()
            anim2.await()
        }
        if (!messages.isNullOrEmpty())
            onDismissButtonChat(true)
        dragEnd.value = true
        //appearance.animateOffsets(boxSize.value.width.toFloat(), density)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords -> boxSize.value = coords.size }
    ) {
        //exit bottom container
        if (offsetY.value > screenHeightPx.dp.value / 2 && !dragEnd.value)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(brush = brush)
                    .align(
                        Alignment.BottomEnd
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    tint = dismissColor.value.copy(0.5f),
                    modifier = Modifier
                        .padding(9.dp)
                        .size(55.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = dismissColor.value,
                            shape = CircleShape
                        )
                        .background(dismissColorContainer.value)
                        .padding(4.dp)
                        .onGloballyPositioned { coordinates ->
                            sizeTopBar = coordinates.size

                            // global position (local also available)
                            positionInRootClearButton = coordinates.positionInRoot()
                        }
                )
            }

        //drag chat

        if (showChatButton == true)
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())
                    }
                    .onGloballyPositioned { coordinates ->
                    }
                    .pointerInput(CircleShape) {
                        detectDragGestures(
                            onDragEnd = {
                                Log.d("drage", sizeTopBar.width.toString())
                                if (isValueBetween(
                                        offsetX.value,
                                        positionInRootClearButton.x - 100,
                                        positionInRootClearButton.x + 100
                                    )
                                    && isValueBetween(
                                        offsetY.value,
                                        positionInRootClearButton.y - 100,
                                        positionInRootClearButton.y + 100
                                    )
                                    && !dragEnd.value
                                ) {
                                    onDismissButtonChat(false)
                                }
                                coroutineScope.launch {
                                    val targetX =
                                        if (offsetX.value > midScreenX.value) boxSize.value.width.toFloat() - 44.dp.toPx(
                                            density
                                        ) else 0f
                                    val anim1 = async { offsetX.animateTo(targetX) }
                                    anim1.await()
                                }
                                dragEnd.value = true
                            }
                        ) { change, dragAmount ->
                            dragEnd.value = false
                            change.consume()
                            dismissColor.value = Color.Black
                            dismissColorContainer.value = Color.Transparent
                            Log.d(
                                "dragge",
                                " offset X ${offsetX.value} | offset Y ${offsetY.value}"
                            )
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y)
                                isExpanded = false
                            }
                            if (isValueBetween(
                                    offsetX.value,
                                    positionInRootClearButton.x - 100,
                                    positionInRootClearButton.x + 100
                                )
                                && isValueBetween(
                                    offsetY.value,
                                    positionInRootClearButton.y - 100,
                                    positionInRootClearButton.y + 100
                                )
                                && !dragEnd.value
                            ) {
                                dismissColor.value = Color.Red
                                dismissColorContainer.value = Color.Red.copy(0.4f)
                                Log.d(
                                    "dragge",
                                    "x ${positionInRootClearButton.x} y  ${positionInRootClearButton.y}"
                                )
                            }
                        }
                    }
                    .clickable {
                        isExpanded = !isExpanded
                        coroutineScope.launch {
                            val targetX = boxSize.value.width.toFloat() - 44.dp.toPx(density)
                            val anim1 = async { offsetX.animateTo(targetX) }
                            val anim2 = async { offsetY.animateTo(0f) }
                            anim1.await()
                            anim2.await()
                        }
                    }
                    .size(44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = "FAB",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            isExpanded = !isExpanded
                            coroutineScope.launch {
                                val targetX = boxSize.value.width.toFloat() - 44.dp.toPx(density)
                                val anim1 = async { offsetX.animateTo(targetX) }
                                val anim2 = async { offsetY.animateTo(0f) }
                                anim1.await()
                                anim2.await()
                            }
                        }
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(12.dp)
                )
                Badge(
                    modifier = Modifier
                        .padding(2.dp),
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text(text = "${messages.size}")
                }
            }
    }


    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInHorizontally(initialOffsetX = { it }) + slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(44.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(messages.size) { index ->
                    Text(text = messages[0].text, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

fun isValueBetween(v: Float, lower: Float, upper: Float): Boolean {
    return v >= lower && v <= upper
}

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}
