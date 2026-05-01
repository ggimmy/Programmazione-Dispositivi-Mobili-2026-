package com.example.lezione_14

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecondScreen(name: String, backToMain: () -> Unit){

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text(text = "Second Screen", fontSize = 30.sp)
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "Hello $name")
            Button(onClick = {
                backToMain()
            }){
                Text("Go back")
            }
    }

}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview(){
    SecondScreen(name = "John", backToMain = {})
}