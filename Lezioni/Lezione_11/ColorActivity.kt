package com.example.lezione_11

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lezione_11.ui.theme.Lezione_11Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.content.Intent

/**
 * ColorActivity - Una Activity secondaria che consente di modificare il colore
 * 
 * CICLO DI VITA DELL'ACTIVITY (dalla slide 3):
 * 1. onCreate() - Inizializzazione della Activity (viene eseguita UNA SOLA VOLTA)
 * 2. onStart() - La Activity diventa visibile
 * 3. onResume() - La Activity riceve il focus e diventa interattiva (stato "visible" in slide 3)
 * 4. onPause() - Un'altra Activity prende il focus (stato "partially visible")
 * 5. onStop() - La Activity non è più visibile (stato "hidden")
 * 6. onDestroy() - La Activity viene distrutta
 * 
 * Quando chiudiamo questa Activity con finish(), il sistema chiama:
 * onPause() → onStop() → onDestroy()
 */
class ColorActivity : ComponentActivity() {
    companion object {
        /**
         * COSTANTE per passare dati tramite Intent Extra
         * 
         * INTENT - Messaggio per comunicare tra componenti (slide 4-5)
         * Gli Intent possono contenere:
         * 1. DATI (Data) - URI della risorsa
         * 2. EXTRA - Coppie chiave-valore (come putExtra e getStringExtra)
         * 3. ACTION - Azione richiesta (es. Intent.ACTION_VIEW)
         * 4. CATEGORY - Contesto dell'intent (slide 13)
         * 
         * Segue la convenzione di mettere le costanti in un companion object
         * e di usare nomi MAIUSCOLI per le costanti
         */
        const val EXTRA_CURRENT_COLOR = "EXTRA_CURRENT_COLOR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Recuperare i dati passati tramite Intent
         * 
         * INTENT ESPLICITO (dalla slide 6):
         * "Un Intent esplicito indica in modo diretto il componente da avviare"
         * Sintassi: Intent(Context, Class) 
         * Es. Intent(this, ColorActivity::class.java)
         * 
         * I dati vengono passati usando putExtra() in MainActivity:
         *   intent.putExtra(ColorActivity.EXTRA_CURRENT_COLOR, "colore")
         * 
         * E recuperati qui con getStringExtra():
         *   intent.getStringExtra(EXTRA_CURRENT_COLOR)
         * 
         * Elvis operator (?:) fornisce un valore di default se il valore è null
         * Se currentColor è null, allora verrà usato "#FFFFFF" (bianco)
         */
        val currentColor = intent.getStringExtra(EXTRA_CURRENT_COLOR) ?: "#FFFFFF"
        Log.d("ColorActivity", "Received color: $currentColor")

        enableEdgeToEdge()
        
        /**
         * setContent è il metodo Compose che imposta l'interfaccia utente
         * 
         * Al posto dei file layout XML, Compose usa funzioni @Composable
         * che descrivono l'UI in modo dichiarativo (come il codice Kotlin)
         * 
         * Il parametro è una lambda che contiene la gerarchia di composable
         */
        setContent {
            Lezione_11Theme {
                /**
                 * ColorPickerScreen è una Composable function
                 * Parametri:
                 * - initialColor: il colore ricevuto via Intent
                 * - onSave: callback quando l'utente salva il colore
                 *   Ritorna il nuovo colore e chiude l'Activity con RESULT_OK
                 * - onCancel: callback quando l'utente annulla
                 *   Chiude l'Activity con RESULT_CANCELED
                 */
                ColorPickerScreen(
                    initialColor = currentColor,
                    onSave = { newColor ->
                        /**
                         * RITORNO DEL RISULTATO (dalla slide 23-24)
                         * 
                         * Quando una Activity vuole ritornare dati alla Activity che l'ha lanciata:
                         * 1. Crea un Intent con i dati (Intent extras)
                         * 2. Chiama setResult(resultCode, resultIntent)
                         *    - resultCode: RESULT_OK se successo, RESULT_CANCELED se annullato
                         *    - resultIntent: Intent con i dati da ritornare
                         * 3. Chiama finish() per chiudere l'Activity
                         * 
                         * L'Activity genitrice riceve il risultato in onActivityResult()
                         * (o tramite ActivityResultLauncher in questo caso)
                         */
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_CURRENT_COLOR, newColor)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    },
                    onCancel = {
                        /**
                         * Se l'utente annulla, segnaliamo RESULT_CANCELED
                         * senza ritornare nessun dato
                         */
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun ColorPickerScreen(
    initialColor: String,
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    /**
     * STATE MANAGEMENT IN COMPOSE
     * 
     * remember { mutableStateOf(...) } crea uno stato composable che:
     * 1. Persiste durante ricomposizioni (quando l'UI si aggiorna)
     * 2. Quando cambia, causa una ricomposizione dell'intera funzione Composable
     * 3. Se il parametro onValueChange viene chiamato, lo stato cambia e la UI si aggiorna
     * 
     * by è syntactic sugar per il delegato (delegation pattern di Kotlin)
     * Equivalente a:
     *   val colorText = remember { mutableStateOf(initialColor) }
     *   val colorTextValue = colorText.value
     * 
     * IMPORTANTE: rememberSaveable (vedi MainActivity) salva lo stato anche
     * attraverso rotazioni dello schermo. remember no!
     */
    var colorText by remember { mutableStateOf(initialColor) }

    /**
     * PARSING COLORE E ERROR HANDLING
     * 
     * Tentiamo di convertire la stringa hex in un Color di Compose
     * Se il formato non è valido (es. non è un hex color valido),
     * usiamo Color.White come fallback
     * 
     * try-catch: Android Colors.parseColor() può lanciare eccezioni
     * se il formato non è riconosciuto (es. "#GGGGGG", "rosso", ecc)
     */
    val textFieldBackgroundColor = try {
        Color(android.graphics.Color.parseColor(colorText))
    } catch (_: Exception) {
        Color.White
    }

    /**
     * SCAFFOLD - Componente Compose standard
     * 
     * Scaffold è un componente che fornisce una struttura standard per l'Activity:
     * - AppBar (in alto)
     * - FloatingActionButton
     * - SnackBar
     * - Content (il resto dello schermo)
     * 
     * Modifier è il pattern builder per configurare proprietà:
     * .fillMaxSize() = occupa tutto lo spazio disponibile (sia larghezza che altezza)
     * .padding() = aggiunge spazi interni
     */
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        /**
         * COLUMN - Contenitore verticale
         * 
         * Dispone i componenti uno sotto l'altro in verticale
         * (equivalente a LinearLayout orientation="vertical" in XML)
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // Rispetta lo spazio dello Scaffold
                .padding(16.dp)  // Padding aggiuntivo
        ) {
            Text(text = "Modifica il colore")

            /**
             * TEXTFIELD - Componente Input testuale
             * 
             * value: lo stato corrente (coloreText)
             * onValueChange: callback quando l'utente digita (aggiorna lo stato)
             *   Quando l'utente cambia testo, viene chiamata questa lambda
             *   che aggiorna colorText, che a sua volta aggiorna lo sfondo
             * 
             * colors: configurazione del colore del componente
             * - unfocusedContainerColor: colore dello sfondo quando NON ha focus dell'utente
             * - focusedContainerColor: colore dello sfondo quando l'utente lo ha selezionato
             * 
             * In questo caso, entrambi usano il colore della stringa hex che hai inserito
             * (quindi se digiti "#FF0000", lo sfondo diventa rosso in tempo reale)
             */
            TextField(
                value = colorText,
                onValueChange = { colorText = it },
                label = { Text("Colore (es: #FF0000)") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = textFieldBackgroundColor,
                    focusedContainerColor = textFieldBackgroundColor
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            /**
             * BOX - Contenitore semplice per posizionare elementi
             * 
             * .weight(1f) = occupa tutto lo spazio disponibile in verticale
             *   (utile per spingere gli altri elementi verso il basso)
             * .fillMaxWidth() = occupa tutta la larghezza
             * .background() = colora lo sfondo con il colore hex inserito
             * 
             * Questo Box serve per visualizzare il colore in tempo reale
             * mentre l'utente lo modifica nel TextField
             */
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(textFieldBackgroundColor)
                    .padding(16.dp)
            )

            /**
             * ROW - Contenitore orizzontale
             * 
             * Dispone i componenti uno accanto all'altro in orizzontale
             * (equivalente a LinearLayout orientation="horizontal" in XML)
             */
            Row(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
                /**
                 * BUTTON - Pulsante di salvataggio
                 * 
                 * onClick = { onSave(colorText) }
                 *   Quando l'utente clicca, chiama il callback onSave
                 *   passando il colore corrente (colorText)
                 *   Questo callback chiuderà l'Activity e ritornerà il risultato
                 * 
                 * .weight(1f) = occupa metà dello spazio (insieme all'altro button)
                 * .padding(end = 8.dp) = aggiunge spazio a destra (gap tra i button)
                 */
                Button(
                    onClick = { onSave(colorText) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Save")
                }
                /**
                 * BUTTON - Pulsante di annullamento
                 * 
                 * onClick = { onCancel() }
                 *   Chiama il callback onCancel senza passare dati
                 *   L'Activity si chiude con RESULT_CANCELED
                 */
                Button(
                    onClick = { onCancel() },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    Lezione_11Theme {
        ColorPickerScreen(
            initialColor = "#FF0000",
            onSave = {},
            onCancel = {}
        )
    }
}
