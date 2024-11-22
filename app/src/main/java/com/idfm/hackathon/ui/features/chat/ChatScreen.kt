package com.idfm.hackathon.ui.features.chat

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.idfm.hackathon.R
import com.idfm.hackathon.data.models.ChatMessage
import com.idfm.hackathon.data.models.ChatMessageFromBot
import com.idfm.hackathon.data.models.ChatMessageFromUser
import com.idfm.hackathon.data.models.LineStatus
import com.idfm.hackathon.data.models.TransportationLine
import com.idfm.hackathon.ui.components.TransportationLineIcon
import com.idfm.hackathon.utils.toTime
import java.util.Date

object Variables {
    val ColorsAccentMain: Color = Color(0xFF1976D3)
    val ColorsAccentDark: Color = Color(0xFF0050AA)
    val ColorsAccentGreen: Color = Color(0xFF118953)
    val cornerRadius = 8.dp
}

@Composable
fun ChatScreen(
    @Suppress("unused_parameter")
    navController: NavHostController = rememberNavController(),
    vm: ChatScreenViewModel
) {
    val chatState by vm.uiState().collectAsState()
    var sttResults by remember {
        mutableStateOf<ChatUiState.ResultStt?>(null)
    }

    if (chatState is ChatUiState.ResultStt) {
        sttResults = chatState as ChatUiState.ResultStt
    }

    Column(Modifier.fillMaxSize()) {
        ChatMessageList(Modifier.weight(1.0f), chatState.messages)

        UserInput(sttResults,
            onSend = {
                sttResults = null
                vm.postUSerRequest(it)
            }, onStt = {
                vm.startStt()
            })
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
        itemsIndexed(messages, key = { _, message -> message.id }) { _, message ->
            if (message is ChatMessageFromBot) {
                DisplayChatMessageFromBot(Modifier, message) {

                }
            } else if (message is ChatMessageFromUser) {
                DisplayChatMessageFromUser(Modifier, message)
            }
        }
    }
}

@Composable
fun UserInput(
    sttResults: ChatUiState.ResultStt?,
    onSend: (String) -> Unit = {},
    onStt: () -> Unit
) {
    val context = LocalContext.current

    var input by remember { mutableStateOf("") }

    sttResults?.textList?.getOrNull(0)?.let {
        input = it
    }

    Row(Modifier.fillMaxWidth()) {
        TextField(
            value = input, onValueChange = {
                input = it
            }, modifier = Modifier
                .weight(1.0f)
                .padding(end = 2.dp)
        )

        Button(
            onClick = {
                hideKeyboard(context)
                onSend(input)
                input = ""
            }, modifier = Modifier.padding(end = 2.dp),

            enabled = input.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send"
            )
        }

        Button(modifier = Modifier.padding(end = 2.dp),
            onClick = {
                onStt()
            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_mic_on_active),
                contentDescription = "Stt"
            )
        }
    }
}

@Composable
fun DisplayChatMessageFromBot(
    modifier: Modifier,
    message: ChatMessageFromBot,
    onOptionSelected: (String) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_surprise),
                contentDescription = "Icon from assets"
            )

            Text(
                text = "IDFM Copilote ${message.timeStamp.toTime()}",
                modifier = modifier.padding(start = 8.dp),
                fontSize = 10.sp
            )
        }

        Box(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .border(
                        width = 1.dp,
                        color = Variables.ColorsAccentMain,
                        shape = RoundedCornerShape(size = Variables.cornerRadius)
                    )
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(size = Variables.cornerRadius)
                    )
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
                    .widthIn(120.dp)
                    .then(modifier)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                    val fullText = message.responseChunks.joinToString(" ")
                    Text(fullText)

                    Row(
                        modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        message.options.forEach {
                            Button(
                                onClick = {
                                    onOptionSelected(it)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Variables.ColorsAccentMain)
                            ) {
                                Text(it, fontSize = 12.sp)
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        Journey(message)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayChatMessageFromUser(modifier: Modifier, message: ChatMessageFromUser) {
    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier
                .padding(bottom = 2.dp, end = 16.dp)
                .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_surprise),
                contentDescription = "Icon from assets"
            )

            Text(
                text = "Moi ${message.timeStamp.toTime()}",
                modifier = modifier.padding(start = 8.dp),
                fontSize = 10.sp
            )
        }

        Box(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.CenterEnd)
                    .clip(RoundedCornerShape(Variables.cornerRadius))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(size = Variables.cornerRadius)
                    )
                    .then(modifier)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                        .background(color = Variables.ColorsAccentMain)
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = message.message,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun Journey(message: ChatMessageFromBot) {
    if (message.transportationLines.isNotEmpty()) {
        Column {
            Row(modifier = Modifier.padding(8.dp)) {
                Column(Modifier.weight(1.0f)) {
                    Row(Modifier.height(50.dp), verticalAlignment = Alignment.CenterVertically) {
                        message.transportationLines.forEachIndexed { index, it ->
                            TransportationLineIcon(
                                Modifier.size(40.dp),
                                it,
                                LineStatus.NONE
                            ) {

                            }

                            if (index < message.transportationLines.size - 1) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_arrow_right),
                                    contentDescription = "Send"
                                )
                            }
                        }
                    }
                    JourneyHours(message.journeyFrom, message.journeyTo)
                }

                AdditionalJourneyDetails(
                    Modifier
                        .padding(end = 8.dp)
                        .fillMaxHeight(),
                    message.remainingTime,
                    message.co2
                )
            }

            JourneyHint(modifier = Modifier, message)
        }
    }
}

fun hideKeyboard(context: Context) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
}


@Composable
fun JourneyHours(from: String, to: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(from, modifier = Modifier.alpha(0.5f), color = Color.Black)
        Image(
            painter = painterResource(id = R.drawable.ic_journey_hours_from_to),
            contentDescription = "",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(to, color = Color.Black)
    }
}

@Composable
fun AdditionalJourneyDetails(modifier: Modifier, remaining: String, co2: String) {
    Column(horizontalAlignment = Alignment.End, modifier = Modifier.then(modifier)) {
        Box(Modifier.clip(RoundedCornerShape(Variables.cornerRadius))) {
            Text(
                text = "2.35€",
                color = Color.White,
                modifier = Modifier
                    .background(Variables.ColorsAccentDark)
                    .padding(top = 4.dp, bottom = 4.dp, start = 6.dp, end = 6.dp)
            )
        }
        Text(
            text = remaining,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )

        Text(
            text = co2,
            modifier = Modifier.alpha(0.5f),
            fontSize = 16.sp,
            color = Color.Black
        )

    }
}

@Composable
fun JourneyHint(modifier: Modifier, message: ChatMessageFromBot) {
    val type = message.type.first
    val backgroundColor = if (type) {
        Variables.ColorsAccentGreen
    } else {
        Variables.ColorsAccentDark
    }

    val icon = if (type) {
        R.drawable.ic_tick
    } else {
        R.drawable.ic_sparkles
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .background(backgroundColor)) {
        Row(modifier = Modifier.padding(start = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Icon from assets",
                modifier = Modifier.padding(8.dp)
            )
            Text(message.type.second, color = Color.White)
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun JourneyHintPreview() {
    JourneyHint(
        Modifier, ChatMessageFromBot(
            42,
            Date(),
            listOf(),
            listOf(),
            listOf(),
            "",
            "",
            "",
            "",
            Pair(true, "Journey is ok")
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AdditionalJourneyDetailsPreview() {
    AdditionalJourneyDetails(Modifier, "17 min", "15 gr (CO2)")
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun JourneyHoursPreview() {
    JourneyHours("12:37", "14:33")
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DisplayChatMessageFromBotPreview() {
    DisplayChatMessageFromBot(
        modifier = Modifier,
        message = ChatMessageFromBot(
            42,
            Date(),
            responseChunks = listOf("Hello", "How are you?"),
            options = listOf("Good", "Bad"),
            journeyFrom = "",
            journeyTo = "",
            remainingTime = "",
            co2 = "",
            type = Pair(true, "Journey is ok")
        ),
        onOptionSelected = {}
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DisplayChatMessageFromUserPreview() {
    DisplayChatMessageFromUser(
        modifier = Modifier,
        message = ChatMessageFromUser(
            42,
            Date(),
            "Hello, can you tell me if my journey is going to be ok?"
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserInputPreview() {
    UserInput(null) {}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ChatMessageListPreview() {
    val messages = listOf(
        ChatMessageFromBot(
            42,
            Date(),
            responseChunks = listOf("Hello", "How are you?"),
            options = listOf("Good", "Bad"),
            transportationLines = listOf(
                TransportationLine.METRO_4, TransportationLine.METRO_1
            ),
            journeyFrom = "14:32",
            journeyTo = "15:27",
            remainingTime = "17 min",
            co2 = "15 gr (CO2)",
            type = Pair(true, "Journey is ok")
        ),
        ChatMessageFromUser(43, Date(), "Hello")
    )

    ChatMessageList(Modifier, messages)
}

