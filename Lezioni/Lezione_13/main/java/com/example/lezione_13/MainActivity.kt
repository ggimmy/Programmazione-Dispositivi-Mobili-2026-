package com.example.lezione_13

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lezione_13.ui.theme.Lezione_13Theme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalConfiguration


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lezione_13Theme {
               ResourcesDemoScreen()
            }
        }
    }
}

@Composable
fun ResourcesDemoScreen() {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        DemoResScreenLandscape()
    } else {
        DemoResScreenPortrait()
    }
}

//Obiettvi: fare 2 grafiche differenti per layout verticale o orizzontale.
@Composable
fun DemoResScreenLandscape() {
    Scaffold() { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),  // Add this for scrolling
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = stringResource(id = R.string.title_label), style = MaterialTheme.typography.headlineLarge)
                Image(painter = painterResource(id = R.drawable.cane),
                    contentDescription = "Image")
                Text(text = stringResource(id = R.string.description_label))
            }
        }
    }
}

@Composable
fun DemoResScreenPortrait() {
    Scaffold() { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),  // Add this for scrolling
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = stringResource(id = R.string.title_label), style = MaterialTheme.typography.headlineLarge)
                Image(painter = painterResource(id = R.drawable.cane),
                    contentDescription = "Image")
                Text(text = stringResource(id = R.string.description_label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lezione_13Theme {
        ResourcesDemoScreen()
    }
}

