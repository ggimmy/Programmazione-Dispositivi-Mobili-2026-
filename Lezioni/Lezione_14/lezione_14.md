# Navigation
*Per utilizzarle va inserita delle dipendenze gradle*
Le navigation permettono di cambiare schermata senza cambiare Activity.
`NavController`, gestisce spostamenti fra schermate con il metodo 
``` kotlin 
navController.navigate("screenname")
```
Con il navController possiamo anche gestire il **backstack**.
`NavHost`, contiene il grafo di navigazione
`composable()` dichiara una schermata raggiungibile
## Navigation graph
``` kotlin
NavHost(
  navController = navController
){
  composable("firstscreen"){
    FirstScreen()
  }
}
```
## Route tipizzate
In Navigation Compose una destinazione puo essere tappresentata da un tipo kotlin invece che una stringa.

# State in Compose
In Compose la UI è una funzione dello stato. 
Quando un valore cambia, Compose riesegue la composable che leggono quel valore. Questo processo si chiama recomposition.
Per "estrarlo" dalla Compose utilizziamo lo **State Hoisting**, per quando lo stato deve essere condiviso, controllato o spostato fuori dalla UI.
## ViewModel
Un ViewModel è uno state holder per una schermata. Serve a conservare lo stato della UI fuori dalla composable e gestire il ciclo di vita delle variabili
