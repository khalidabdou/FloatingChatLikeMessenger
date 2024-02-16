package com.example.floatingchatlikemessenger.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Appearance : ViewModel() {


    val offsetX = Animatable(0f)
    val offsetY = Animatable(100f)
    val lastPosition = Offset(0f, 0f)


}
