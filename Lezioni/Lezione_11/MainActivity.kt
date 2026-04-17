package com.example.lezione_11

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
/**
 * ActivityResultLauncher - Nuovo sistema (Android 11+) per ricevere risultati da altre Activity
 * 
 * PRIMA (deprecated):
 *   startActivityForResult(intent, REQUEST_CODE)
 *   e poi override onActivityResult()
 * 
 * ORA (moderno):
 *   registerForActivityResult() restituisce un launcher
 *   launcher.launch(intent) per avviare l'Activity
 *   il ResultContract gestisce automaticamente il ritorno
 */
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lezione_11.ui.theme.Lezione_11Theme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * MainActivity - Activity principale dell'applicazione
 * 
 * Nel layout degli Activity di Android:
 * - MainActivity è la Activity GENITRICE (parent)
 * - ColorActivity è la Activity FIGLIA (child)
 * 
 * TASK (slide 25-26):
 * Un Task è uno stack di Activity. Quando MainActivity lancia ColorActivity,
 * quest'ultima viene messa sopra a MainActivity nello stack del Task.
 * 
 * BACK BUTTON:
 * Quando l'utente preme BACK da ColorActivity:
 * - ColorActivity viene tolta dallo stack
 * - Si torna a MainActivity
 * - Ricordiamo che onStop() → onDestroy() vengono chiamati
 */
class MainActivity : ComponentActivity() {

    /**
     * currentColorState - Stato della Activity principale
     * 
     * Questa è una variabile di istanza, non uno stato Compose
     * Viene usata dal colorResultLauncher per comunicare il nuovo colore
     * al componente Composable MainScreen
     * 
     * LATEINIT: la variabile viene inizializzata nel onCreate()
     * e non prima (lazy initialization)
     */
    private lateinit var currentColorState: androidx.compose.runtime.MutableState<String>

    /**
     * colorResultLauncher - Launcher per la ColorActivity
     * 
     * registerForActivityResult crea un launcher che:
     * 1. Avvia un'Activity (StartActivityForResult)
     * 2. Riceve il risultato quando l'Activity si chiude
     * 3. Chiama il callback con il risultato
     * 
     * ActivityResultContracts.StartActivityForResult():
     *   - Accetta qualsiasi Intent come input
     *   - Ritorna ActivityResult(resultCode, data)
     *   - resultCode può essere RESULT_OK o RESULT_CANCELED
     *   - data è l'Intent ritornato da setResult() in ColorActivity
     */
    private val colorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        /**
         * CALLBACK DI RISULTATO (slide 23-24)
         * 
         * Quando ColorActivity chiama:
         *   setResult(RESULT_OK, resultIntent)
         *   finish()
         * 
         * Il sistema chiama questo callback con il risultato
         */
        if (result.resultCode == RESULT_OK) {
            /**
             * Se il risultato è RESULT_OK (utente ha cliccato "Save")
             * Estraiamo il colore nuovo da result.data
             */
            val newColor = result.data?.getStringExtra(ColorActivity.EXTRA_CURRENT_COLOR)
            if (newColor != null) {
                /**
                 * Aggiorniamo lo stato della Activity principale
                 * Quando lo stato cambia, Compose ricompone l'UI automaticamente
                 */
                currentColorState.value = newColor
                Log.d("MainActivity", "Color updated to: $newColor")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SingleNode", "onCreate called")
        enableEdgeToEdge()
        
        /**
         * setContent è il metodo che imposta l'UI in Compose
         * Il parametro è una lambda (Composable)
         * 
         * Dentro setContent, creiamo lo stato della Activity usando rememberSaveable
         */
        setContent {
            Lezione_11Theme {
                /**
                 * rememberSaveable - Stato Compose che persiste anche attraverso
                 * rotazioni dello schermo e ricreazioni dell'Activity
                 * 
                 * DIFFERENZA tra remember e rememberSaveable:
                 * - remember: lo stato si perde quando l'Activity viene ricreata
                 *   (es. quando ruoti il telefono)
                 * - rememberSaveable: lo stato viene salvato in un Bundle
                 *   e ristabilito dopo ricreazioni
                 * 
                 * IMPORTANTE: rememberSaveable è usato per dati che l'utente
                 * potrebbe aver inserito e non vogliamo che perdano
                 */
                var currentColor by rememberSaveable { mutableStateOf("#FFFFFF") }
                
                /**
                 * Creiamo anche un remember per mantenere un riferimento allo stato
                 * che il colorResultLauncher può usare per aggiornare il colore
                 */
                currentColorState = remember { mutableStateOf(currentColor) }

                MainScreen(
                    currentColor = currentColorState.value,
                    onColorChange = {
                        /**
                         * LAUNCH DEL RESULT LAUNCHER
                         * 
                         * Quando l'utente clicca il pulsante "Colore":
                         * 1. Creiamo un Intent esplicito per ColorActivity
                         * 2. Passiamo il colore corrente via putExtra()
                         * 3. Lanciamo l'Activity con colorResultLauncher.launch(intent)
                         * 
                         * ColorActivity si aprirà, e quando chiama finish(),
                         * il callback del launcher verrà eseguito
                         */
                        val intent = Intent(this@MainActivity, ColorActivity::class.java)
                        intent.putExtra(ColorActivity.EXTRA_CURRENT_COLOR, currentColorState.value)
                        colorResultLauncher.launch(intent)
                    }
                )
            }
        }
    }

    /**
     * onStart() - Metodo del ciclo di vita (slide 3)
     * 
     * Viene chiamato dopo onCreate() quando:
     * 1. L'Activity è appena stata creata (primo avvio)
     * 2. L'utente ritorna all'Activity (tornando da un'altra Activity)
     * 
     * In questo momento:
     * - L'Activity diventa visibile
     * - Ma NON ha ancora il focus (l'utente non può interagire)
     * - Subito dopo verrà chiamato onResume()
     */
    override fun onStart() {
        super.onStart()
        Log.d("SingleNode", "onStart called")
    }

    /**
     * onResume() - Metodo del ciclo di vita (slide 3)
     * 
     * Viene chiamato quando:
     * 1. L'Activity è completamente visibile e pronta per l'interazione
     * 2. L'utente potrebbe iniziare a interagire (cliccare bottoni, scrivere, ecc)
     * 
     * Questo è lo stato "visible" nella slide 3
     * 
     * USO: Possiamo inizializzare risorse costose qui
     * (es. camera, sensori, ecc)
     */
    override fun onResume() {
        super.onResume()
        Log.d("SingleNode", "onResume called")
    }

    /**
     * onPause() - Metodo del ciclo di vita (slide 3)
     * 
     * Viene chiamato quando:
     * 1. Un'altra Activity prende il focus
     * 2. Un dialog viene aperto
     * 3. L'utente apre il menu app-switcher
     * 
     * In questo momento:
     * - L'Activity è ancora visibile (parzialmente, slide 3 = "partially visible")
     * - Ma l'utente non può più interagire
     * 
     * USO: Possiamo mettere in pausa animazioni, suoni, ecc
     * IMPORTANTE: Non fare operazioni lunghe! Il sistema potrebbe uccidere l'Activity
     */
    override fun onPause() {
        super.onPause()
        Log.d("SingleNode", "onPause called")
    }

    /**
     * onStop() - Metodo del ciclo di vita (slide 3)
     * 
     * Viene chiamato quando:
     * 1. L'Activity non è più visibile
     * 2. Un'altra Activity è andata in foreground (è visibile all'utente)
     * 
     * In questo momento, l'Activity è nello stato "hidden" (slide 3)
     * 
     * USO: Possiamo salvare dati persistenti qui
     * (es. salvare su database, SharedPreferences, file, ecc)
     */
    override fun onStop() {
        super.onStop()
        Log.d("SingleNode", "onStop called")
    }

    /**
     * onDestroy() - Metodo del ciclo di vita (slide 3)
     * 
     * Viene chiamato quando:
     * 1. L'utente preme il pulsante BACK per chiudere l'Activity
     * 2. Il sistema uccide l'Activity (per libera memoria)
     * 3. La Activity termina per un'eccezione non gestita
     * 
     * In questo momento, l'Activity è nello stato "destroyed" (slide 3)
     * 
     * USO: Possiamo fare pulizia finale
     * (chiudere database, liberare risorse, annullare callback, ecc)
     * 
     * DOPO onDestroy(), l'Activity non esiste più e non può essere riutilizzata
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("SingleNode", "onDestroy called")
    }

}



}



/**
 * LAYOUT A COLONNA - MainScreen è la UI principale
 * 
 * @Composable: questo decorator indica che questa è una funzione Composable
 * che può essere inclusa in un'altra Composable o in setContent()
 * 
 * Parametri:
 * - currentColor: il colore attuale (viene aggiornato quando l'utente lo cambia in ColorActivity)
 * - onColorChange: callback che viene chiamato quando l'utente clicca il pulsante "Colore"
 *   Questo callback lanciapera la ColorActivity
 */
@Composable
fun MainScreen(
    currentColor: String = "#FFFFFF",
    onColorChange: () -> Unit = {}
) {

    /**
     * GESTIONE DELLO STATO DELLA NOTA
     * 
     * rememberSaveable { mutableStateOf("") } crea uno stato che:
     * 1. Viene mantenuto durante le ricomposizioni (remember)
     * 2. Viene salvato quando l'Activity viene ricreata, es. per rotazione (Saveable)
     * 
     * IMPORTANTE: Senza rememberSaveable, se l'utente:
     * - Scrive una nota lunghissima
     * - Ruota il telefono
     * Tutto il testo verrebbe perso perché l'Activity si ricrea!
     * 
     * Con rememberSaveable, il testo viene salvato e ripristinato automaticamente
     * 
     * by è syntactic sugar del delegato (delegation pattern):
     *   Senza by: val noteState = rememberSaveable { mutableStateOf("") }
     *   Con by: var nota_text by rememberSaveable { mutableStateOf("") }
     *   Si può usare direttamente nota_text anziché noteState.value
     */
    var nota_text by rememberSaveable() { mutableStateOf("")}

    /**
     * PARSING COLORE HEXADECIMALE
     * 
     * Il colore ricevuto è una stringa hex (es. "#FF0000" per rosso)
     * Tentiamo di convertirlo in un Color di Compose per usarlo come sfondo
     * 
     * Se il formato è invalido, usiamo Color.White come fallback
     */
    val notaBackgroundColor = try {
        Color(android.graphics.Color.parseColor(currentColor))
    } catch (_: Exception) {
        Color.White
    }

     /**
      * SCAFFOLD - Struttura standard dell'UI
      * 
      * Fornisce uno scheletro standard con:
      * - AppBar (in alto)
      * - Content (il resto dello schermo)
      * - FloatingActionButton
      * - SnackBar
      * 
      * innerPadding contiene lo spazio che lo Scaffold riserva (es. per AppBar)
      */
     Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
         /**
          * COLUMN - Contenitore verticale
          * 
          * Dispone i componenti uno sotto l'altro
          * .padding(innerPadding) rispetta lo spazio dello Scaffold
          * .fillMaxSize() occupa tutto lo spazio disponibile
          */
         Column(modifier = Modifier
             .padding(innerPadding)
             .fillMaxSize()) {
             
            Text(text = "Scrivi una nota",
                    modifier = Modifier.padding(16.dp))
            
            /**
             * TEXTFIELD - Area dove l'utente scrive la nota
             * 
             * value = nota_text: il testo corrente
             * onValueChange = { nota_text = it }: quando l'utente digita,
             *   aggiorna lo stato nota_text
             *   Questo causa una ricomposizione che aggiorna il background
             * 
             * colors: il background cambia al colore corrente (notaBackgroundColor)
             * 
             * .weight(1f): occupa tutto lo spazio verticale disponibile
             *   (spinge i bottoni verso il basso)
             */
            TextField(value = nota_text,
                    onValueChange = { nota_text = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = notaBackgroundColor,
                        focusedContainerColor = notaBackgroundColor
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .fillMaxWidth())
             
            /**
             * ROW - Contenitore orizzontale per i button
             * 
             * Dispone i button uno accanto all'altro (Colore e Share)
             */
             Row(
                 modifier = Modifier.padding(16.dp)
             ) {
                 /**
                  * BUTTON "Colore" - Apre ColorActivity
                  * 
                  * onClick = { onColorChange() }:
                  *   Chiama il callback che fa partire la ColorActivity
                  *   (che viene definito nel MainActivity)
                  * 
                  * modifier Modifier.weight(1f): occupa metà dello spazio
                  */
                 Button(onClick = {
                     onColorChange()
                 }, modifier = Modifier.weight(1f)) {
                     /**
                      * MOSTRA IL COLORE CORRENTE NEL BUTTON
                      * 
                      * $currentColor stampa il valore della stringa hex
                      * Es: se currentColor = "#FF0000", il button dirà "Colore - #FF0000"
                      */
                     Text(text = "Colore - $currentColor")
                 }
                 /**
                  * BUTTON "Share" - Non implementato
                  * 
                  * onClick = {}: callback vuoto (non fa nulla)
                  * Potrebbe essere usato in futuro per condividere la nota
                  */
                 Button(onClick = {}, modifier = Modifier.weight(1f)) {
                     Text(text = "Share")
                 }
             }

         }

     }
}

/**
 * PREVIEW - Anteprima di sviluppo
 * 
 * @Preview permette di vedere l'anteprima della UI nel IDE senza lanciare l'app
 * showBackground = true: mostra uno sfondo per veder meglio i confini
 * 
 * Questa è SOLO per sviluppo, NON viene usata nell'app finale
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lezione_11Theme {
        MainScreen()
    }
}
