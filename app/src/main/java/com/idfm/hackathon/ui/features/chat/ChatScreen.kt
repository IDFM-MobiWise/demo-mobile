package com.idfm.hackathon.ui.features.chat

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.idfm.hackathon.data.models.ChatMessage


@Composable
fun ChatScreen(
    @Suppress("unused_parameter")
    navController: NavHostController = rememberNavController(),
    vm: ChatScreenViewModel
) {
    val chatState by vm.uiState().collectAsState()
//    var chatResults by remember {
//        mutableStateOf(ChatUiState.Response(emptyList()))
//    }

//    if (chatState is ChatUiState.Response) {
//        chatResults = chatState as ChatUiState.Response
//    }

    Column(Modifier.fillMaxSize()) {
        ChatMessageList(Modifier, chatState.messages)

        UserInput {
            vm.postUSerRequest(it)
        }
    }
}

@Composable
fun ChatMessageList(modifier: Modifier, messages: List<ChatMessage>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        val idx = messages.lastIndex
        if (idx > 0) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        state = listState
    ) {
        // itemsIndexed(state.hubElements, key = { _, element -> element }) { _, element ->
        itemsIndexed(messages, key = { _, message -> message }) { _, message ->
            if (message is ChatMessage.FromBot) {
                ChatMessageFromBot(Modifier, message) {

                }
            } else if (message is ChatMessage.FromUser) {
                ChatMessageFromUser(Modifier, message)
            }
        }
    }
}

@Composable
fun UserInput(onSend: (String) -> Unit = {}) {
    Row(Modifier.fillMaxWidth()) {
        var userInput by remember { mutableStateOf("") }
        TextField(value = userInput, onValueChange = {
            userInput = it
        }, modifier = Modifier.weight(1.0f))

        Button(onClick = {
            onSend(userInput)
        }, enabled = userInput.isNotBlank()) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }
    }
}

@Composable
fun ChatMessageFromBot(
    modifier: Modifier,
    message: ChatMessage.FromBot,
    onOptionSelected: (String) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterEnd)) {
            val fullText = message.responseChunks.joinToString(" ")
            Text(fullText, modifier = Modifier.widthIn(120.dp))

            Row {
                message.options.forEach {
                    Button(onClick = {
                        onOptionSelected(it)
                    }, modifier = Modifier.weight(1.0f)) {
                        Text(it)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageFromUser(modifier: Modifier, message: ChatMessage.FromUser) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .then(modifier)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterEnd)) {
            Text(message.message, modifier = Modifier.widthIn(120.dp))
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ChatMessageFromBotPreview() {
    ChatMessageFromBot(
        modifier = Modifier,
        message = ChatMessage.FromBot(
            responseChunks = listOf("Hello", "How are you?"),
            options = listOf("Good", "Bad")
        ),
        onOptionSelected = {}
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserInputPreview() {
    UserInput() {}
}

