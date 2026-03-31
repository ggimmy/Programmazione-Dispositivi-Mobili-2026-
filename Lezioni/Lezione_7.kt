/*
 * ============================================================================
 * LEZIONE 7: COLLECTIONS - LISTE, SET, MAPPE, RANGE E SEQUENCE
 * ============================================================================
 * 
 * Obiettivi di questa lezione:
 * 1. Comprendere le diverse strutture dati (List, Set, Map, Array)
 * 2. Imparare la differenza tra mutable e immutable collections
 * 3. Capire l'interfaccia Iterable e come iterare
 * 4. Apprendere il ruolo di hashCode() e equals()
 * 5. Scoprire Range (progressions) e Sequence (lazy evaluation)
 * 6. Imparare le operazioni avanzate su collections
 * 7. Comprendere la differenza tra eager e lazy evaluation
 */

// ============================================================================
// INTRODUZIONE: COS'SONO LE COLLECTIONS?
// ============================================================================

/*
 * COLLECTIONS = Strutture dati che contengono MULTIPLI oggetti dello stesso tipo
 * 
 * Tutte le collections in Kotlin implementano l'interfaccia Iterable,
 * che permette di iterare su di esse.
 * 
 * TIPI PRINCIPALI:
 * 1. List    - Collezione ORDINATA e INDICIZZATA (con indici 0, 1, 2, ...)
 * 2. Set     - Collezione di elementi UNICI (no duplicati)
 * 3. Map     - Collezione di coppie CHIAVE -> VALORE
 * 4. Array   - Lista con DIMENSIONE FISSA
 * 5. Range   - Progressione di numeri (es: 1..10)
 * 6. Sequence - Versione LAZY di List (elementi generati on-demand)
 * 
 * MUTABLE vs IMMUTABLE:
 * - Immutable: NON puoi aggiungere/rimuovere elementi (listOf, setOf, mapOf)
 * - Mutable: Puoi aggiungere/rimuovere/modificare elementi (mutableListOf, etc.)
 */

// ============================================================================
// PARTE 1: LIST - COLLEZIONE ORDINATA E INDICIZZATA
// ============================================================================

/*
 * LIST = Collezione ORDINATA dove gli elementi sono accessibili tramite indice
 * 
 * METODI BASE:
 * - size                    -> numero di elementi
 * - isEmpty() / isNotEmpty()-> verifica se vuota
 * - contains(x)            -> x è nella lista?
 * - containsAll(collection) -> tutti gli elementi di collection sono presenti?
 * - get(index) o [index]   -> elemento in posizione index
 * - first() / last()       -> primo e ultimo elemento
 * - indices                -> range da 0 a size-1
 * - subList(from, to)      -> ⚠️ ATTENZIONE: è una VIEW, non una COPIA!
 * - iterator()             -> ritorna un Iterator per iterare
 * 
 * IMMUTABLE vs MUTABLE:
 * - listOf<T>()        -> immutable, non puoi modificare
 * - mutableListOf<T>() -> mutable, puoi aggiungere/rimuovere
 * - buildList { }      -> syntax alternativa per creare liste immutable
 * 
 * ⚠️ IMPORTANTE: Una lista immutable NON permette di aggiungere elementi,
 * ma se gli elementi sono oggetti mutabili, puoi comunque modificarli!
 */

data class User(var name: String, val age: Int)

/**
 * Classe A - Utilizza hashCode/equals di default
 */
class A(val primary: Int, val secondary: Int) {
    override fun toString() = "A(primary=$primary, secondary=$secondary)"
}

/**
 * Classe B - Override di hashCode e equals
 * 
 * ⚠️ IMPORTANTE PER I SET:
 * Se vuoi che due oggetti siano considerati "uguali" in un Set,
 * DEVI sovrascrivere hashCode() e equals()
 */
class B(val primary: Int, val secondary: Int) {
    
    /**
     * Override di hashCode - Deve essere COERENTE con equals()
     * 
     * Se due oggetti sono uguali (equals == true),
     * DEVONO avere lo stesso hashCode!
     * 
     * In questo caso, la classe B considera uguali due oggetti
     * se hanno lo stesso 'primary', ignorando 'secondary'
     */
    override fun hashCode(): Int {
        return primary
    }
    
    /**
     * Override di equals - Confronta il contenuto, non il reference
     * 
     * L'operatore 'as?' fa un safe cast:
     * - Se other può essere castato a B, lo fa e accede a .primary
     * - Se no, ritorna null, e quindi il ? restituisce null per tutto
     */
    override fun equals(other: Any?): Boolean {
        return primary == (other as? B)?.primary
    }
    
    override fun toString() = "B(primary=$primary, secondary=$secondary)"
}

// ============================================================================
// PARTE 2: SET - COLLEZIONE DI ELEMENTI UNICI
// ============================================================================

/*
 * SET = Collezione dove ogni elemento è UNICO (no duplicati)
 * 
 * CARATTERISTICHE:
 * - Non ordinata (l'ordine di iterazione è arbitrario)
 * - Implementa equals() e hashCode() per evitare duplicati
 * - Utile per verificare unicità e operazioni su insiemi (unione, intersezione)
 * 
 * COME FUNZIONA L'UNICITÀ:
 * 1. Quando aggiungi un elemento, Kotlin chiama hashCode()
 * 2. Usa l'hash per trovare il "bucket" dove mettere l'elemento
 * 3. Se c'è già un elemento nello stesso bucket, chiama equals()
 * 4. Se equals() ritorna true, l'elemento è considerato un DUPLICATO
 * 
 * METODI SPECIFICI:
 * - union(other)     -> unione di due set
 * - intersect(other) -> intersezione (elementi comuni)
 * - subtract(other)  -> differenza (elementi in questo ma non in other)
 * 
 * ⚠️ CRITICO: Se non implementi equals() e hashCode() in modo coerente,
 * il Set non funzionerà correttamente!
 */

// ============================================================================
// PARTE 3: MAP - COLLEZIONE DI COPPIE CHIAVE -> VALORE
// ============================================================================

/*
 * MAP = Collezione di coppie (CHIAVE, VALORE), come un dizionario
 * 
 * CARATTERISTICHE:
 * - Accesso veloce ai valori tramite chiave (O(1) average)
 * - Le chiavi devono essere UNICHE
 * - I valori possono essere duplicati
 * - Le chiavi usano hashCode() e equals() come i Set
 * 
 * METODI BASE:
 * - size                 -> numero di coppie
 * - get(key) o [key]    -> valore associato a key (⚠️ può essere null!)
 * - getOrDefault(key, v) -> valore di key, altrimenti default v
 * - keys / values       -> insieme di chiavi / collezione di valori
 * - entries             -> insieme di coppie Pair(key, value)
 * - containsKey(key)    -> chiave presente?
 * - containsValue(value)-> valore presente?
 * - put(key, value)     -> aggiunge/modifica coppia (solo mutable)
 * - remove(key)         -> rimuove la coppia (solo mutable)
 * 
 * ⚠️ IMPORTANTE: get() ritorna nullable!
 * È buona pratica usare getOrDefault() o Elvis operator (?:)
 */

// ============================================================================
// PARTE 4: ARRAY - COLLEZIONE A DIMENSIONE FISSA
// ============================================================================

/*
 * ARRAY = Lista con DIMENSIONE FISSA, non può cambiare di taglia
 * 
 * CARATTERISTICHE:
 * - Dimensione stabilita al momento della creazione
 * - NON puoi aggiungere/rimuovere elementi
 * - Puoi SOLO modificare elementi esistenti
 * - Più efficiente di List in termini di memoria
 * 
 * CREAZIONE:
 * - intArrayOf(1, 2, 3)     -> array di Int
 * - IntArray(5)             -> array di 5 Int (inizializzati a 0)
 * - IntArray(5) { it + 1 }  -> array dove elemento[i] = i+1
 * - arrayOf<String>("a", "b") -> array di String
 * 
 * DIFFERENZA DALL'ITALIA:
 * - Array è PRIMITIVO Java (int[], String[])
 * - List è OBJECT Java (List<Integer>, List<String>)
 * - Array è più veloce ma meno flessibile
 */

// ============================================================================
// PARTE 5: RANGE - PROGRESSIONI DI NUMERI
// ============================================================================

/*
 * RANGE = Progressione di numeri (ordini), rappresenta una sequenza
 * 
 * CREAZIONE:
 * - 1..10        -> range da 1 a 10 (incluso)
 * - 1 until 10   -> range da 1 a 9 (esclude 10)
 * - 'a'..'z'     -> range da 'a' a 'z'
 * - 10 downTo 1  -> range decrescente
 * - 1..100 step 2-> range con passo di 2
 * 
 * CARATTERISTICHE:
 * - NON crea una lista, è una "rappresentazione logica"
 * - Puoi iterare su di esso: for (i in 1..10) { }
 * - Puoi convertirlo in lista: (1..10).toList()
 * - Implementa Iterable
 */

// ============================================================================
// PARTE 6: SEQUENCE - LAZY EVALUATION
// ============================================================================

/*
 * SEQUENCE = Versione LAZY di List
 * 
 * DIFFERENZA DALLA LIST:
 * List:     EAGER - Calcola TUTTI gli elementi subito
 * Sequence: LAZY - Genera elementi ON-DEMAND durante l'iterazione
 * 
 * ESEMPIO:
 * 
 * List version (EAGER - calcola tutto):
 *     val result = listOf(1, 2, 3)
 *         .filter { it > 1 }      // Filtra TUTTI gli elementi (istantaneamente)
 *         .map { it * 2 }         // Mappa TUTTI i risultati (istantaneamente)
 *         .take(1)                // Prende solo il primo
 *     // Risultato: il map() ha processato 2 elementi inutilmente!
 * 
 * Sequence version (LAZY - solo ciò che serve):
 *     val result = sequenceOf(1, 2, 3)
 *         .filter { it > 1 }      // Definisce il filtro (non esegue!)
 *         .map { it * 2 }         // Definisce la mappa (non esegue!)
 *         .take(1)                // "Voglio 1 elemento!"
 *         .toList()               // ORA esegue: filter(2), map(4), take(1)
 *     // Risultato: più efficiente!
 * 
 * CREAZIONE:
 * - sequenceOf(1, 2, 3)          -> sequence con elementi iniziali
 * - generateSequence(1) { it+1 } -> sequenza infinita
 * - sequence { yield(...) }      -> builder syntax
 * - lista.asSequence()           -> converte lista a sequence
 * 
 * QUANDO USARE:
 * - Sequence: quando hai pipeline lunghe e vuoi efficienza
 * - List: quando hai pochi elementi o accesso frequente per indice
 */

// ============================================================================
// PARTE 7: OPERAZIONI AVANZATE SU COLLECTIONS
// ============================================================================

/*
 * OPERAZIONI UTILI:
 * 
 * - filter { condition }     -> mantiene solo elementi che soddisfano condition
 * - map { transform }        -> trasforma ogni elemento
 * - flatMap { transform }    -> trasforma e appiattisce
 * - fold / reduce            -> accumula elementi in un valore singolo
 * - take(n)                  -> prende primi n elementi
 * - drop(n)                  -> scarta primi n elementi
 * - chunk(n)                 -> spezzetta in sottoliste di n elementi
 * - windowed(n)              -> "finestra" di n elementi (come chunked ma sovrapposta)
 * - groupBy { key }          -> raggruppa per chiave
 * - distinct()               -> rimuove duplicati
 * - sorted() / sortedBy()    -> ordina
 * - reversed()               -> inverte ordine
 * - any { condition }        -> almeno un elemento soddisfa?
 * - all { condition }        -> tutti gli elementi soddisfano?
 * - none { condition }       -> nessun elemento soddisfa?
 * - first() / last()         -> primo / ultimo elemento
 * - find { condition }       -> primo elemento che soddisfa
 * - partition { condition }  -> divide in due liste (soddisfa / non soddisfa)
 */

// ============================================================================
// PARTE 8: ITERATOR - ITERAZIONE MANUALE
// ============================================================================

/*
 * ITERATOR = Oggetto che permette di iterare su una collection manualmente
 * 
 * METODI:
 * - hasNext()  -> c'è un elemento successivo?
 * - next()     -> ritorna elemento corrente e si sposta al prossimo
 * - remove()   -> rimuove l'elemento corrente (solo su mutable collections)
 * 
 * QUANDO USARE:
 * - Normalmente NON serve, usi for (item in collection)
 * - Utile quando vuoi rimuovere elementi durante l'iterazione
 *   (non puoi farlo durante un for loop!)
 */

// ============================================================================
// PARTE 9: DIMOSTRAZIONE PRATICA - MAIN
// ============================================================================

fun main() {
    
    println("=" * 80)
    println("PARTE 1: LIST - COLLEZIONE ORDINATA E INDICIZZATA")
    println("=" * 80)
    
    val lista = listOf("a", "b", "c", "d", "e", "f")
    
    println("Lista originale: $lista")
    println("Dimensione: ${lista.size}")
    println("Primo elemento: ${lista.first()}")
    println("Ultimo elemento: ${lista.last()}")
    
    println("\nIterazione con indice (Java-style):")
    for (i in lista.indices) {
        println("  [$i] = ${lista[i]}")
    }
    
    println("\nIterazione diretta (Python-style):")
    for (item in lista) {
        println("  $item")
    }
    
    println("\nIterazione con iterator manuale:")
    val iterator = lista.iterator()
    while (iterator.hasNext()) {
        println("  ${iterator.next()}")
    }
    
    println("\nOperazioni di ricerca:")
    println("  Contiene 'a'? ${lista.contains("a")}")
    println("  Contiene tutti ['a', 'b', 'c']? ${lista.containsAll(listOf("a", "b", "c"))}")
    
    // Sublista (⚠️ è una VIEW, non una COPIA!)
    val sublista = lista.subList(1, 4)  // da indice 1 a 3 (4 escluso)
    println("  Sublista [1..4): $sublista")
    
    println()
    println("=" * 80)
    println("PARTE 2: LIST IMMUTABLE vs MUTABLE")
    println("=" * 80)
    
    // Lista immutable con oggetti mutabili
    val lista2 = listOf(User("Alberto", 29), User("Bob", 31))
    println("Lista di User: $lista2")
    
    println("\nProva a modificare elemento:")
    println("  Originale: ${lista2[0]}")
    lista2[0].name = "Carlo"  // ✓ Posso modificare l'oggetto dentro
    println("  Dopo modifica: ${lista2[0]}")
    
    // ⚠️ Questa riga darebbe ERRORE perché lista2 è immutable:
    // lista2[0] = User("Giuseppe", 29)
    
    // Lista mutable - Posso aggiungere/rimuovere
    println("\nLista mutable con rimozione tramite iterator:")
    val lista3 = mutableListOf(User("Alberto", 29), User("Bob", 31))
    println("  Prima: $lista3")
    
    val iterator2 = lista3.iterator()
    while (iterator2.hasNext()) {
        iterator2.next()
        iterator2.remove()  // ✓ Posso rimuovere durante iterazione
    }
    println("  Dopo rimozione: $lista3")
    
    // Build list syntax
    val lista4 = buildList {
        add(10)
        add(20)
        add(30)
    }
    println("\nLista creata con buildList: $lista4")
    
    println()
    println("=" * 80)
    println("PARTE 3: SET - ELEMENTI UNICI E hashCode/equals")
    println("=" * 80)
    
    // Classe A usa hashCode/equals di default (per reference)
    val a1 = A(1, 2)
    val a2 = A(1, 2)
    println("Due istanze A(1,2):")
    println("  a1.hashCode() = ${a1.hashCode()}")
    println("  a2.hashCode() = ${a2.hashCode()}")
    println("  a1 == a2? ${a1 == a2}")  // false (reference diversi)
    println("  a1 === a2? ${a1 === a2}") // false (reference diversi)
    
    // Classe B ha hashCode/equals override basato su 'primary'
    val b1 = B(1, 2)
    val b2 = B(1, 3)  // Stesso primary, secondary diverso
    println("\nDue istanze B(1,2) e B(1,3):")
    println("  b1.hashCode() = ${b1.hashCode()}")
    println("  b2.hashCode() = ${b2.hashCode()}")
    println("  b1 == b2? ${b1 == b2}")  // true (stesso primary!)
    println("  b1 === b2? ${b1 === b2}") // false (reference diversi)
    
    // Set con A - Considera i duplicati
    val set1 = setOf(A(1, 2), A(1, 2))
    println("\nSet con A(1,2), A(1,2):")
    println("  Dimensione: ${set1.size}")  // 2 (duplicati non rimossi!)
    println("  Contenuto: $set1")
    
    // Set con B - Riconosce i duplicati
    val set2 = setOf(B(1, 2), B(1, 3), B(1, 2))
    println("\nSet con B(1,2), B(1,3), B(1,2):")
    println("  Dimensione: ${set2.size}")  // 2 (B(1,2) è duplicato!)
    println("  Contenuto: $set2")
    
    // Operazioni su set
    val set3 = setOf(1, 2, 3)
    val set4 = setOf(2, 3, 4)
    println("\nOperazioni su set {1,2,3} e {2,3,4}:")
    println("  Union: ${set3.union(set4)}")
    println("  Intersect: ${set3.intersect(set4)}")
    println("  Subtract: ${set3.subtract(set4)}")
    
    println()
    println("=" * 80)
    println("PARTE 4: MAP - COPPIE CHIAVE -> VALORE")
    println("=" * 80)
    
    val words = listOf("ciao", "casa", "computer", "bottiglia", "ciao")
    
    // Contare occorrenze usando Map
    println("Conteggio occorrenze di parole: $words")
    val counter = mutableMapOf<String, Int>()
    for (word in words) {
        counter[word] = counter.getOrDefault(word, 0) + 1
    }
    
    println("\nContatore risultato:")
    for ((word, count) in counter) {
        println("  '$word' -> $count")
    }
    
    // Operazioni su map
    println("\nOperazioni su map:")
    println("  Chiavi: ${counter.keys}")
    println("  Valori: ${counter.values}")
    println("  Entries: ${counter.entries}")
    println("  Contiene chiave 'ciao'? ${counter.containsKey("ciao")}")
    
    // ⚠️ get() può ritornare null
    println("  counter['ciao'] = ${counter["ciao"]}")
    println("  counter['inesistente'] = ${counter["inesistente"]}")  // null
    
    println()
    println("=" * 80)
    println("PARTE 5: RANGE - PROGRESSIONI DI NUMERI")
    println("=" * 80)
    
    val range1 = 1..5
    println("Range 1..5:")
    println("  È un Iterable (non una List)")
    for (num in range1) print("$num ")
    println()
    
    val range2 = 'a'..'e'
    println("Range 'a'..'e':")
    for (char in range2) print("$char ")
    println()
    
    val range3 = 10 downTo 1
    println("Range 10 downTo 1:")
    for (num in range3) print("$num ")
    println()
    
    val range4 = 1..10 step 2
    println("Range 1..10 step 2:")
    for (num in range4) print("$num ")
    println()
    
    val listFromRange = (1..5).toList()
    println("\nConverti range a lista: $listFromRange")
    
    println()
    println("=" * 80)
    println("PARTE 6: SEQUENCE - LAZY EVALUATION")
    println("=" * 80)
    
    println("Creazione di sequenze:")
    val seq1 = sequenceOf(1, 2, 3, 4, 5)
    println("  sequenceOf(1,2,3,4,5)")
    
    val seq2 = generateSequence(1) { it + 1 }.take(5)
    println("  generateSequence(1) { it+1 }.take(5)")
    
    val seq3 = sequence {
        println("    [yield 1]")
        yield(1)
        println("    [yield 2]")
        yield(2)
        println("    [yield 3]")
        yield(3)
    }
    println("  sequence { yield(1), yield(2), yield(3) }")
    
    println("\nIterazione su sequence (genera on-demand):")
    for (num in seq3) {
        println("    Elemento: $num")
    }
    
    println()
    println("=" * 80)
    println("PARTE 7: EAGER vs LAZY - DIFFERENZA CRITICA")
    println("=" * 80)
    
    val words2 = "ciao come stai spero bene".split(" ")
    println("Parole: $words2")
    
    println("\n--- VERSIONE EAGER (List) ---")
    println("Pipeline: filter (len>3) -> map (lunghezza) -> take(3)")
    val resultList = words2
        .filter {
            println("  filter: '$it' (len=${it.length})")
            it.length > 3
        }
        .map {
            println("  map: '$it' -> ${it.length}")
            it.length
        }
        .take(3)
    println("Risultato: $resultList")
    
    println("\n--- VERSIONE LAZY (Sequence) ---")
    println("Pipeline: filter (len>3) -> map (lunghezza) -> take(3)")
    val resultSeq = words2.asSequence()
        .filter {
            println("  filter: '$it' (len=${it.length})")
            it.length > 3
        }
        .map {
            println("  map: '$it' -> ${it.length}")
            it.length
        }
        .take(3)
        .toList()
    println("Risultato: $resultSeq")
    
    println("\n⚠️ Nota: Con Sequence, filter e map vengono eseguiti")
    println("   SOLO per gli elementi necessari (uno alla volta)")
    
    println()
    println("=" * 80)
    println("PARTE 8: OPERAZIONI AVANZATE")
    println("=" * 80)
    
    val nums = listOf(1, 2, 3, 4, 5, 6)
    println("Lista: $nums")
    
    println("\nOperazioni di trasformazione:")
    println("  filter (pari): ${nums.filter { it % 2 == 0 }}")
    println("  map (x2): ${nums.map { it * 2 }}")
    println("  take(3): ${nums.take(3)}")
    println("  drop(2): ${nums.drop(2)}")
    println("  chunked(2): ${nums.chunked(2)}")
    println("  windowed(2): ${nums.windowed(2)}")
    
    println("\nOperazioni di aggregazione:")
    println("  sum: ${nums.sum()}")
    println("  max: ${nums.maxOrNull()}")
    println("  min: ${nums.minOrNull()}")
    println("  fold: ${nums.fold(0) { acc, x -> acc + x }}")
    
    println("\nOperazioni predicative:")
    println("  any (>5): ${nums.any { it > 5 }}")
    println("  all (>0): ${nums.all { it > 0 }}")
    println("  none (>10): ${nums.none { it > 10 }}")
    
    println("\nOperazioni di ricerca:")
    println("  find (>3): ${nums.find { it > 3 }}")
    println("  distinct: ${listOf(1, 2, 2, 3, 3, 3).distinct()}")
    
    val (pari, dispari) = nums.partition { it % 2 == 0 }
    println("\nPartition (pari vs dispari):")
    println("  Pari: $pari")
    println("  Dispari: $dispari")
    
    println()
    println("=" * 80)
    println("RIEPILOGO CONCETTI CHIAVE")
    println("=" * 80)
    println("""
        
        ✓ COLLECTIONS:
          - List: ordinata, indicizzata, permette duplicati
          - Set: elementi unici, usa hashCode() e equals()
          - Map: coppie chiave->valore, accesso veloce
          - Array: dimensione fissa, più efficiente della List
        
        ✓ MUTABLE vs IMMUTABLE:
          - listOf/setOf/mapOf: immutable
          - mutableListOf/mutableSetOf/mutableMapOf: mutable
          - Liste immutable possono contenere oggetti mutabili
        
        ✓ ITERATOR:
          - Permette iterazione manuale
          - Utile per rimuovere durante iterazione
          - La lista immutable NON permette rimozione via iterator
        
        ✓ RANGE E PROGRESSION:
          - 1..10: range inclusivo
          - 1 until 10: range esclusivo
          - 1..10 step 2: range con passo
          - 10 downTo 1: range decrescente
        
        ✓ SEQUENCE (LAZY EVALUATION):
          - Genera elementi on-demand
          - Efficiente per pipeline lunghe
          - Converti con .asSequence() o sequenceOf()
        
        ✓ OPERAZIONI COMUNI:
          - filter/map/flatMap: trasformazione
          - take/drop: selezione
          - fold/reduce: aggregazione
          - any/all/none: predicati
          - chunked/windowed: sezionamento
        
        ✓ hashCode E equals:
          - Critico per Set e Map (come chiavi)
          - Devono essere COERENTI
          - Override con @Override
    """.trimIndent())
}

// HELPER per stampa decorativa
private operator fun String.times(count: Int) = this.repeat(count)
