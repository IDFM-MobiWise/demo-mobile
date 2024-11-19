package com.idfm.hackathon.ui.features.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.idfm.hackathon.data.models.LineStatus
import com.idfm.hackathon.data.models.TransportationType
import com.idfm.hackathon.ui.components.TransportationTypeLineGrid

@Composable
fun HomeScreen(
    @Suppress("unused_parameter")
    navController: NavHostController = rememberNavController(),
    vm: HomeScreenViewModel
) {
    val homeState by vm.uiState().collectAsState()
    var sttResults by remember {
        mutableStateOf<HomeUiState.ResultStt?>(null)
    }

    if (homeState is HomeUiState.ResultStt) {
        sttResults = homeState as HomeUiState.ResultStt
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            val context = LocalContext.current

            ListeningProgression(sttResults)

            TransportationTypeLineGrid(modifier = Modifier,
                type = TransportationType.METRO,
                statusForLine = { line ->
                    when (line.line) {
                        "1" -> LineStatus.NORMAL
                        "2" -> LineStatus.INTERRUPTED
                        "3" -> LineStatus.CLOSED
                        else -> LineStatus.NORMAL
                    }
                }) {
                Toast.makeText(context, "Click on $it", Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.padding(12.dp))

            TransportationTypeLineGrid(Modifier, TransportationType.RER,
                statusForLine = { line ->
                    when (line.line) {
                        "a" -> LineStatus.NORMAL
                        "b" -> LineStatus.INTERRUPTED
                        "c" -> LineStatus.CLOSED
                        else -> LineStatus.NORMAL
                    }
                }) {
                Toast.makeText(context, "Click on $it", Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.padding(12.dp))

            TransportationTypeLineGrid(Modifier, TransportationType.TRAM,
                statusForLine = { line ->
                    when (line.line) {
                        "1" -> LineStatus.NORMAL
                        "2" -> LineStatus.INTERRUPTED
                        "3" -> LineStatus.CLOSED
                        else -> LineStatus.NORMAL
                    }
                }) {
                Toast.makeText(context, "Click on $it", Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.padding(12.dp))

            CallToAction(
                modifier = Modifier
            ) {
                vm.fetchStuff()
            }
        }

        Loader(homeState is HomeUiState.InProgress)
    }
}

@Composable
fun Loader(show: Boolean) {
    if (show) {
        // Show loader
    } else {
        // Hide loader
    }
}

@Composable
fun ListeningProgression(data: HomeUiState.ResultStt?) {
    data?.textList?.firstOrNull()?.let {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .alpha(if (data.partial) 0.4f else 1f),
            text = it,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CallToAction(modifier: Modifier, onClick: () -> Unit = {}) {
    // Call to action
    Button(modifier = modifier, onClick = onClick) {
        Text("Call Yohann's Data")
    }
}