package com.example.lezione_14

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NameViewModel: ViewModel() {
    var name by mutableStateOf("")
        private set

    fun updateName(newName: String){
        name = newName
    }
}