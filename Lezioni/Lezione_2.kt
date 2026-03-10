/**
 * Funzione di supporto per dimostrare l'esecuzione delle espressioni booleane.
 * Ritorna sempre false e stampa un messaggio per tracciare se è stata chiamata.
 */
fun leftFalse(): Boolean{
    println("LeftFalse falso!")
    return false
}

/**
 * Funzione di supporto per dimostrare l'esecuzione delle espressioni booleane.
 * Ritorna sempre true e stampa un messaggio per tracciare se è stata chiamata.
 */
fun rightTrue(): Boolean{
    println("righttrue true")
    return true
}

/**
 * Funzione che accetta una funzione lambda come parametro.
 * Dimostra il concetto di "funzioni di ordine superiore" (higher-order functions).
 * @param op: una funzione che prende un Int e ritorna un Int
 */
fun foo(op: (Int) -> Int) = println(op(3))

/**
 * Funzione che ritorna il massimo tra due numeri interi.
 * Usa un'espressione if-else come "single expression function" 
 * (una riga, senza necessità di return esplicito).
 */
fun maxOf(a: Int, b: Int) = if (a > b) a else b

fun main() {

    /* ============ DIFFERENZA TRA OPERATORE 'and' E '&&' ============
       
       CONCETTO CHIAVE: Short-circuit evaluation
       
       L'operatore && (AND logico):
       - Esegue short-circuit: se il primo operando è false, NON valuta il secondo
       - Questo evita side-effects non desiderati
       - USO CONSIGLIATO: quando gli operandi sono espressioni o funzioni
       
       L'operatore 'and':
       - NON esegue short-circuit: valuta SEMPRE entrambi gli operandi
       - Utile solo per controllare semplici valori booleani
       - EVITARE quando gli operandi sono funzioni (causerebbe side-effects inaspettati)
       
       ESEMPIO CON TEST:
    */
    
    // Con && (short-circuit): rightTrue() NON viene chiamata
    // perché leftFalse() è falso, il ramo if non viene eseguito
    if (leftFalse() && rightTrue()) {
        println("1"); // Questo non viene stampato
    }

    // Con 'and': ENTRAMBE le funzioni vengono sempre chiamate
    // anche se il risultato finale è lo stesso (false && true = false)
    if (leftFalse() and rightTrue()) {
        println("2"); // Questo non viene stampato
    }
    // ===================================================================


    /* ============ NULL SAFETY: Safe Call (?.) e let ============
       
       Kotlin forza la gestione esplicita dei valori null per evitare errori.
       Ci sono diversi modi per interagire con variabili nullable:
    */
    
    val name: String? = "Giacomo" // String? = può essere una stringa O null
    
    // ERRORE: println(name) 
    // Il compilatore non permette questa operazione perché 'name' potrebbe essere null
    
    // ERRORE: println(name.length)
    // Non possiamo accedere direttamente ai metodi di un tipo nullable
    
    // SOLUZIONE 1: Safe Call Operator (?.)
    // Esegue l'operazione SOLO se name non è null, altrimenti ignora tutto
    name?.let { println(it.length) }
    // 'it' rappresenta il valore di name all'interno del blocco lambda
    
    // SOLUZIONE ALTERNATIVA: Operator run
    // Simile a let, ma con sintassi diversa. Ha più senso con oggetti complessi
    // name?.run { println(this.length) }
    
    /* ================================================================= */

    /* ============ ESERCIZIO 4.5: Lettura sicura di un intero ============
       
       Dimostra come:
       1. Leggere un input da console in modo sicuro
       2. Usare l'Elvis Operator (?:) per fornire un valore di fallback
       
       FLOW:
       - readLine() ritorna String? (può essere null)
       - toIntOrNull() converte in Int? (ritorna null se la conversione fallisce)
       - ?: (Elvis Operator) se il lato sinistro è null, esegue il lato destro
       - Nel caso fallisca, stampiamo un messaggio e usciamo dalla funzione con 'return'
    */
    
    val n = readLine()?.toIntOrNull() ?: run { 
        println("Non è un numero"); 
        return  // Esce dalla funzione main se l'input non è valido
    }
    
    // Se arriviamo a questa riga, sappiamo che 'n' contiene un numero valido
    println(n * n) // Stampiamo il quadrato del numero
    
    /* ================================================================= */


    /* ============ ESERCIZIO 4.6: Stampa condizionale ============
       
       Usa 'let' per stampare un nome SOLO se non è null.
       Se name2 è null, il blocco let non viene eseguito affatto.
    */
    
    val name2: String? = readLine()
    // Se name2 non è null, stampiamo il messaggio con interpolazione di stringhe
    name2?.let { println("Name is $it") }
    
    /* ============================================================ */

    /* ============ FUNZIONE TODO() ============
       
       La funzione TODO() è uno strumento di development che permette di:
       - Compilare il codice anche se non hai implementato certe funzioni
       - Lasciare placeholder che lanciano eccezioni a runtime
       - Utile per pianificare la struttura prima di implementare i dettagli
       
       Esempio (commentato):
       fun findItemOrNull(id: String) : Item? = TODO("Item ancora da implementare!")
    */
    // Se decommentassi la funzione, il compilatore non protesta,
    // ma lancerebbe una NotImplementedError se venisse chiamata
    // findItemOrNull("test")
    
    /* ========================================= */

    /* ============ STRING TEMPLATES: Interpolazione di stringhe ============
       
       Kotlin semplifica la concatenazione di stringhe usando il dollaro ($)
       direttamente dentro i letterali di stringa.
    */
    
    // METODO 1: Template string semplice (Consigliato - Conciso)
    val i = 10
    println("i = $i")  // Sostituisce $i con il valore di i
    
    // METODO 2: StringBuilder (Per stringhe lunghe o complesse)
    // Utilizzato quando devi costruire stringhe complesse in più passaggi
    val s = "ciao"
    val sb = StringBuilder()
    sb.append(s)
    sb.append("come stai?")
    println(sb)  // Output: "ciacome stai?"
    
    /* ================================================================= */

    /* ============ LAMBDA EXPRESSIONS: Funzioni Anonime ============
       
       Una lambda è una funzione anonima che puoi passare come valore.
       Sintassi: { parametri -> corpo }
       
       LE LAMBDA SONO VALORI: puoi assegnarle a variabili come qualsiasi altro dato.
    */
    
    // METODO 1: Dichiarazione esplicita del tipo della lambda
    val sum: (Int, Int) -> Int = { x, y -> x + y }
    // Il tipo dice: accetta 2 Int, ritorna 1 Int
    
    // METODO 2: Type inference (il compilatore deduce il tipo dai parametri)
    val sum2 = { x: Int, y: Int -> x + y }
    // Qui specifichiamo i tipi dei parametri, il compilatore deduce il tipo di ritorno
    
    println(sum2(2, 1))  // Output: 3
    
    // ============ fold() : Accumulazione di elementi ============
    // fold() applica una lambda a ogni elemento, accumulando un risultato
    
    val values = intArrayOf(1, 2, 3, 4, 5)
    val product = values.fold(1) { acc, e -> acc * e }
    // 1 * 1 = 1
    // 1 * 2 = 2
    // 2 * 3 = 6
    // 6 * 4 = 24
    // 24 * 5 = 120
    // acc = accumulatore (valore "in costruzione")
    // e = elemento corrente dell'array
    
    // ============ Higher-order functions ============
    // Una funzione che accetta una lambda come parametro
    foo { x: Int -> x * 2 }  // Passiamo una lambda che raddoppia il numero
    // Syntactic sugar: le graffe {} sono la lambda, passata come parametro
    
    /* ================================================================= */

    /* ============ ESERCIZIO: Trovare il massimo usando fold ============
       
       Dimostra come usare la funzione maxOf() insieme a fold()
       per trovare l'elemento massimo in un array.
    */
    
    // Metodo 1: Usando fold() con la funzione maxOf()
    println(maxOf(2, 3))  // Output: 3
    
    // Metodo 2: fold() per trovare il massimo in un array
    val valuees2 = intArrayOf(1, 2, 3, 4, 5)
    println(valuees2.fold(1) { acc, i -> maxOf(acc, i) })
    // Accumulatore inizia da 1
    // Compara 1 con 1 → max = 1
    // Compara 1 con 2 → max = 2
    // Compara 2 con 3 → max = 3
    // Compara 3 con 4 → max = 4
    // Compara 4 con 5 → max = 5
    
    // METODO MIGLIORE IN KOTLIN: Usare maxOrNull()
    // Kotlin fornisce funzioni built-in ottimizzate per task comuni
    valuees2.maxOrNull()?.let { println(it) }  // Output: 5
    // maxOrNull() ritorna Int? (può essere null se l'array è vuoto)
    
    /* ================================================================= */

    /* ============ ESERCIZIO: WHEN expression ============
       
       'when' è il rimpiazzo moderno di switch-case in Kotlin,
       ma è più potente perché può gestire diverse forme di pattern matching.
    */
    
    val x = readLine()?.toIntOrNull() ?: return
    
    // VARIANTE 1: Match esatto su valori specifici
    // Confronta x con valori letterali
    when (x) {
        1 -> println("1")
        2 -> println("2")
        else -> println("altro")  // Caso di default (obbligatorio in alcuni contesti)
    }
    
    // VARIANTE 2: Match su condizioni (molto più flessibile)
    // Non confronta x con valori, ma valuta condizioni booleane
    when {
        x > 0 -> println("maggiore di 0")
        x < 0 -> println("minore di 0")
        else -> println("uguale a 0")
    }
    
    // NOTE:
    // - 'when' è un'expression, non solo uno statement (ritorna un valore)
    // - A differenza di switch-case, non serve 'break' tra i rami
    // - Se un ramo match, gli altri non vengono valutati
    
    /* ====================================================== */

    /* ============ LISTE (COLLECTIONS) ============
       
       Kotlin distingue tra:
       - Immutable List: non puoi modificare dopo la creazione
       - MutableList: puoi aggiungere/rimuovere elementi
    */
    
    // IMMUTABLE LIST (Immutabile)
    // listOf() crea una lista che NON può essere modificata
    val elements = listOf<String>("ciao", "uno", "gioco", "erba", "casa")
    
    // ITERAZIONE CON INDICE
    // withIndex() fornisce sia l'indice che il valore
    for ((index, element) in elements.withIndex()) { 
        println("$index: $element") 
    }
    
    // ERRORE: elements.add("appendo")
    // Non è possibile: listOf() crea una lista IMMUTABILE!
    
    // MUTABLE LIST (Mutabile)
    // mutableListOf() crea una lista che PUÒ essere modificata
    val elements2 = mutableListOf<String>("ciao", "uno", "gioco", "erba", "casa")
    
    // Ora possiamo aggiungere elementi
    elements2.add("Aggiungo")  // Funziona! La lista è mutabile
    
    /* ================================================ */

    /* ============ ESERCIZIO: Somma elementi pari ============
       
       Confronta due approcci:
       1. Stile imperativo (come Java): loop manuale
       2. Stile funzionale (come Kotlin): filter + fold
    */
    
    val oggetti = 1 .. 10  // Range: crea una sequenza da 1 a 10 (incluso)
    
    // APPROCCIO 1: Stile IMPERATIVO (Java-like)
    // Manuale, con loop esplicito e controllo esplicito
    var somma = 0
    for (oggetto in oggetti) {
        if (oggetto % 2 == 0) somma += oggetto  // Somma solo i numeri pari
    }
    // somma ora contiene: 2 + 4 + 6 + 8 + 10 = 30
    
    // APPROCCIO 2: Stile FUNZIONALE (Kotlin-idiomatic)
    // Concatenamento di operazioni, leggibile come "pipeline"
    oggetti
        .filter { it % 2 == 0 }      // Filtra: mantiene solo i numeri pari
        .fold(0) { acc, element -> acc + element }  // Somma gli elementi rimanenti
    // Pipeline:
    // 1. filter { it % 2 == 0 } → [2, 4, 6, 8, 10]
    // 2. fold(0, ...) → somma totale = 30
    
    // Nota: Per somme semplici, Kotlin ha anche sum()
    // oggetti.filter { it % 2 == 0 }.sum()
    
    /* ====================================================== */

}
