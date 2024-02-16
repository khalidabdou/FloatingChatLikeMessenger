package com.example.floatingchatlikemessenger

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.floatingchatlikemessenger.Model.Message
import com.example.floatingchatlikemessenger.ui.Appearance
import com.example.floatingchatlikemessenger.ui.Messenger
import com.example.floatingchatlikemessenger.ui.theme.FloatingChatLikeMessengerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appearance by viewModels<Appearance>()
        setContent {
            FloatingChatLikeMessengerTheme {

                val coroutineScope = rememberCoroutineScope()
                val showButtonChat = remember {
                    mutableStateOf(false)
                }
                val messages = remember { mutableStateListOf<Message>() }
                var newMessage = remember {
                    mutableStateOf("")
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Messenger(
                            appearance = appearance,
                            showChatButton = messages.size != 0,
                            messages = messages,
                            onDismissButtonChat = {
                                messages.clear()
                            },
                            onClick = { comment ->

                            }) { comment ->
                        }

                        Row(modifier = Modifier.padding(12.dp)) {
                            TextField(value = newMessage.value, onValueChange = {
                                newMessage.value = it
                            })
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = {
                                messages.add(Message(0, newMessage.value, 0, "", true))
                                newMessage.value = ""
                            }) {
                                Icon(Icons.Default.Send, contentDescription =null)
                            }
                        }

                    }

                }
            }
        }
    }
}

fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') // Define the range of characters to include
    return (1..length)
        .map { allowedChars.random() } // Select a random character for each position
        .joinToString("")
}