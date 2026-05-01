package com.example.lezione_14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lezione_14.ui.theme.Lezione_14Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lezione_14Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  Surface(modifier = Modifier.padding(innerPadding)) {
                        MyApp()
                  }
                }
            }
        }
    }
}

@Composable
fun MyApp(){
    val navcontroller = rememberNavController()
    //val nameHoisted = remember { mutableStateOf("") }
    val nameHoisted: NameViewModel = viewModel()

    NavHost(navController = navcontroller, startDestination = "main_screen"){
        composable("main_screen"){
            MainScreen(
                name = nameHoisted.name,
                onNameChange = { nameHoisted.updateName(it)},
                goToSecondScreen = { navcontroller.navigate("second_screen") }
            )
        }
        composable("second_screen"){
            //val name = it.arguments?.getString("name") ?: ""
            SecondScreen(name = nameHoisted.name, backToMain = {
                navcontroller.popBackStack()
            })
        }
    }
}