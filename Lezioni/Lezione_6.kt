/*
 * ============================================================================
 * LEZIONE 6: EXTENSION AVANZATE, DATA CLASSES, ENUM, SEALED CLASSES E SINGLETON
 * ============================================================================
 * 
 * Obiettivi di questa lezione:
 * 1. Approfondire le extension functions su classi non controllate
 * 2. Comprendere le data classes e la generazione automatica di metodi
 * 3. Imparare il destructuring con componentN
 * 4. Apprendere enum, sealed classes e il pattern matching
 * 5. Capire singleton e companion objects
 * 6. Esplorare la type hierarchy di Kotlin
 */

// ============================================================================
// PARTE 1: EXTENSION FUNCTIONS AVANZATE
// ============================================================================

/*
 * EXTENSION FUNCTIONS SU CLASSI NON CONTROLLATE
 * 
 * Una delle feature più potenti di Kotlin è poter aggiungere metodi
 * a classi che non abbiamo scritto (es. String, Int, List, etc.)
 * 
 * SYNTAX: fun [CLASSE_ESISTENTE].[NOME_FUNZIONE]([PARAMETRI]): [RITORNO] { }
 * 
 * ⚠️ REGOLA IMPORTANTE:
 * Se un metodo è già definito nella classe, verrà usato quello originale,
 * NON l'extension. Le extension non possono sovrascrivere metodi esistenti!
 * 
 * CASO D'USO: Aggiungere utilità specifiche al dominio senza modificare le classi base
 */

/**
 * Extension function: Aggiunge parentesi quadre a una stringa
 * Estende String (classe che non controllate) aggiungendo un nuovo metodo
 */
fun String.inBrackets(): String {
    return "[$this]"
}

/**
 * Extension function: Ripete una stringa N volte
 */
fun String.repeat(times: Int): String {
    return (1..times).joinToString("") { this }
}

/**
 * Extension function: Controlla se una stringa è un email (validazione semplice)
 */
fun String.isEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

// ============================================================================
// PARTE 2: DATA CLASSES - GENERAZIONE AUTOMATICA DI METODI
// ============================================================================

/*
 * DATA CLASSES = Classi ottimizzate per contenere DATI (non comportamento)
 * 
 * Il compilatore Kotlin AUTOMATICAMENTE genera per te:
 * 1. equals() - Confronta il CONTENUTO (non il reference!)
 * 2. hashCode() - Codice hash basato sul contenuto (utile per Set, Map)
 * 3. toString() - Rappresentazione leggibile (es: "Libro(titolo=Lo Hobbit)")
 * 4. componentN() - Per il destructuring (component1(), component2(), etc.)
 * 5. copy() - Crea una copia con alcuni parametri modificati
 * 
 * PERCHÉ UTILI?
 * - Elimina boilerplate code (non devi scrivere questi metodi)
 * - Confronto di contenuto automatico (== funziona correttamente)
 * - Debugging facilitato (toString() è leggibile)
 * - Immutabilità incoraggiata (usa val per i parametri)
 * 
 * ⚠️ AVVERTENZA IMPORTANTE:
 * Se una data class contiene una sottoclasse NON data,
 * il confronto di equals() confronterà i RIFERIMENTI di quella sottoclasse,
 * rendendo l'uguaglianza falsa anche se i dati sono identici.
 * Soluzione: Usa data classes annidate invece di sottoclassi normali.
 */

/**
 * Classe semplice come data class - il compilatore genera equals(), hashCode(), toString(), componentN(), copy()
 */
data class Libro(
    val titolo: String,
    val autore: String = "Anonimo",  // Parametro con valore di default
    val pagine: Int = 0
)

/**
 * Esempio di data class più complessa
 */
data class Studente(
    val nome: String,
    val matricola: Int,
    val email: String
)

// ============================================================================
// PARTE 3: DESTRUCTURING CON COMPONENTN
// ============================================================================

/*
 * DESTRUCTURING = Estrarre i componenti di un oggetto in variabili separate
 * 
 * Qualsiasi classe può supportare il destructuring se implementa componentN()
 * Le data classes lo implementano AUTOMATICAMENTE!
 * 
 * SYNTAX:
 *     val (var1, var2, var3) = oggetto
 * 
 * Internamente, Kotlin chiama:
 *     var1 = oggetto.component1()
 *     var2 = oggetto.component2()
 *     var3 = oggetto.component3()
 * 
 * ⚠️ IMPORTANTE: L'ordine conta! Deve corrispondere all'ordine dei parametri del constructor
 */

/**
 * Classe con operatori componentN definiti manualmente
 * (normalmente usi data classes, ma questo mostra come funziona)
 */
class PC(
    val brand: String,
    val price: Int,
    val os: String
) {
    // Operatore component1 - accessibile tramite destrutturazione
    operator fun component1() = brand
    
    // Operatore component2
    operator fun component2() = price
    
    // Operatore component3
    operator fun component3() = os
    
    override fun toString() = "PC(brand=$brand, price=$price, os=$os)"
}

// ============================================================================
// PARTE 4: ENUM - INSIEMI DI COSTANTI PREDEFINITI
// ============================================================================

/*
 * ENUM = Enumerazione di valori costanti predefiniti
 * 
 * Utile per rappresentare un insieme FISSO di opzioni (come in tutti i linguaggi)
 * 
 * DIFFERENZA DA JAVA:
 * In Kotlin, gli enum sono ancora più potenti perché puoi associarvi dati e metodi
 * 
 * PROPRIETÀ AUTOMATICHE:
 * - name: il nome dell'enum (es: "SCRITTO")
 * - ordinal: l'indice (es: 0 per il primo)
 * 
 * CASO D'USO: Rappresentare un insieme fisso di scelte (stati, tipi, livelli)
 */

/**
 * Enum semplice - Nessun valore associato
 */
enum class ExamType {
    SCRITTO,    // ordinal = 0
    ORALE,      // ordinal = 1
    PROGETTO    // ordinal = 2
}

/**
 * Enum con parametri e proprietà
 * Ogni valore dell'enum ha associati dati (in questo caso, i CFU)
 */
enum class StudyLevel(val cfu: Int) {
    TRIENNALE(180),      // Una laurea triennale ha 180 CFU
    MAGISTRALE(120),     // Una magistrale ha 120 CFU
    DOTTORATO(180)       // Un dottorato ha 180 CFU aggiuntivi
}

/**
 * Enum con metodi custom
 */
enum class DayOfWeek {
    LUNEDÌ, MARTEDÌ, MERCOLEDÌ, GIOVEDÌ, VENERDÌ, SABATO, DOMENICA;
    
    fun isWeekend(): Boolean = this == SABATO || this == DOMENICA
    
    override fun toString(): String = when (this) {
        LUNEDÌ -> "Inizio settimana 📅"
        VENERDÌ -> "Quasi weekend! 🎉"
        SABATO, DOMENICA -> "Weekend! 🌞"
        else -> "Giorno feriale"
    }
}

// ============================================================================
// PARTE 5: SEALED CLASSES - GERARCHIA DI CLASSI CONTROLLATA
// ============================================================================

/*
 * SEALED CLASSES = Classe base che permette una gerarchia CONTROLLATA
 * 
 * CARATTERISTICHE:
 * 1. Non può essere istanziata direttamente
 * 2. Tutte le sottoclassi DEVONO essere note a compile-time
 *    (devono essere nello stesso file o nel medesimo package)
 * 3. Ideale per rappresentare "scelte limitate" di sottoclassi
 * 4. Si usa spesso con 'when' per pattern matching
 * 
 * DIFFERENZA DA ABSTRACT:
 * - abstract: tante sottoclassi possibili in tutta l'applicazione
 * - sealed: numero FISSO e NOTO di sottoclassi
 * 
 * CASO D'USO: Rappresentare il risultato di un'operazione che può avere
 * stati finiti e noti (Success/Failure), o un AST (Abstract Syntax Tree)
 */

/**
 * Sealed class - Rappresenta i possibili risultati di un esame
 * 
 * Tutte le sottoclassi devono essere definite nello stesso file
 * Il compilatore garantisce che 'when' su ExamResult è esaustivo
 */
sealed class ExamResult {
    abstract fun summary(): String
}

/**
 * Sottoclasse: Esame superato con voto
 */
class Passed(val grade: Int) : ExamResult() {
    override fun summary() = "Esame superato con voto: $grade"
}

/**
 * Sottoclasse: Esame non superato con motivo
 */
class Failed(val reason: String) : ExamResult() {
    override fun summary() = "Esame non superato: $reason"
}

/**
 * Sottoclasse: Studente assente
 */
class Absent : ExamResult() {
    override fun summary() = "Studente assente"
}

/**
 * Sottoclasse: Esame rimandato
 */
class Deferred : ExamResult() {
    override fun summary() = "Esame rimandato"
}

/**
 * Funzione che utilizza sealed class con pattern matching
 * 
 * ✨ VANTAGGI:
 * - Il compilatore verifica che TUTTI i casi siano gestiti
 * - Se aggiungiamo un nuovo caso a ExamResult, otteniamo errore di compilazione
 *   finché non aggiungiamo il relativo case nel when
 */
fun printResult(result: ExamResult) {
    when (result) {
        is Passed -> println("✓ ${result.summary()}")
        is Failed -> println("✗ ${result.summary()}")
        is Absent -> println("⊘ ${result.summary()}")
        is Deferred -> println("⏳ ${result.summary()}")
    }
}

// ============================================================================
// PARTE 6: SINGLETON E COMPANION OBJECTS
// ============================================================================

/*
 * SINGLETON = Un oggetto che esiste in una sola istanza per l'intera applicazione
 * 
 * In Kotlin, definire un singleton è banale:
 *     object MySingleton { }
 * 
 * Kotlin garantisce che verrà istanziato UNA SOLA VOLTA (thread-safe)
 * 
 * COMPANION OBJECT = Blocco statico dentro una classe
 * 
 * Analogo ai metodi statici di Java, ma molto più potente:
 * - Può avere proprietà e metodi
 * - Può implementare interfacce
 * - Ha accesso ai membri privati della classe
 * 
 * CASO D'USO:
 * - Variabili statiche (contatori, configurazioni globali)
 * - Factory methods per creare istanze della classe
 * - Codice comune a tutte le istanze
 */

/**
 * Classe con companion object
 * 
 * Il companion object contiene lo "stato globale" della classe
 * che è condiviso tra TUTTE le istanze
 */
class Student(val name: String, val studentID: Int) {
    
    // COMPANION OBJECT - Blocco statico della classe
    companion object {
        // Variabile condivisa tra TUTTE le istanze
        var counter: Int = 0
        
        /**
         * Factory method - Metodo statico per creare Student
         */
        fun createWithID(name: String): Student {
            return Student(name, ++counter)
        }
        
        fun getTotalStudents() = counter
    }
    
    init {
        // Incrementa il contatore ogni volta che creiamo uno Student
        counter++
    }
    
    fun printInfo() {
        println("$name (ID: $studentID), Totale studenti: $counter")
    }
}

/**
 * Singleton - Un oggetto globale unico
 */
object DatabaseConnection {
    private val connectionString = "jdbc:mysql://localhost:3306/mydb"
    
    fun connect() {
        println("Connesso a: $connectionString")
    }
    
    fun disconnect() {
        println("Disconnesso dal database")
    }
}

// ============================================================================
// PARTE 7: KOTLIN TYPE HIERARCHY
// ============================================================================

/*
 * TIPO HIERARCHY DI KOTLIN:
 * 
 *                          Any
 *                         /   \
 *                    Comparable  \
 *                       ...      [Tipi]
 *                                  |
 *                               Nothing
 * 
 * ANY:
 * - È la superclasse di TUTTI gli oggetti in Kotlin
 * - Fornisce equals(), hashCode(), toString()
 * 
 * NOTHING:
 * - È la sottoclasse di TUTTI i tipi
 * - Rappresenta "nessun valore" (mai ritorna)
 * - Usato per funzioni che lanciano eccezioni o loop infiniti
 * 
 * NULLABLE:
 * - Ogni tipo T ha una versione nullable T?
 * - String != String? (String non può essere null, String? sì)
 * - Convertibile tramite Elvis operator (?:)
 */

// ============================================================================
// PARTE 8: DIMOSTRAZIONE PRATICA - MAIN
// ============================================================================

fun main() {
    
    println("=" * 80)
    println("PARTE 1: EXTENSION FUNCTIONS SU CLASSI NON CONTROLLATE")
    println("=" * 80)
    
    val str = "test"
    println("Stringa originale: $str")
    println("Con brackets: ${str.inBrackets()}")
    println("Ripetuta 3 volte: ${str.repeat(3)}")
    println("È un email? ${"mario@gmail.com".isEmail()}")
    println()
    
    println("=" * 80)
    println("PARTE 2: DATA CLASSES - GENERAZIONE AUTOMATICA")
    println("=" * 80)
    
    // Creiamo due libri con lo stesso contenuto
    val l1 = Libro("Lo Hobbit", "J.R.R. Tolkien", 310)
    val l2 = Libro("Lo Hobbit", "J.R.R. Tolkien", 310)
    
    // toString() viene generato automaticamente
    println("l1: $l1")
    println("l2: $l2")
    
    // equals() confronta il CONTENUTO, non il reference!
    println("l1 == l2 (uguaglianza contenuto)? ${l1 == l2}")  // true
    println("l1 === l2 (uguaglianza reference)? ${l1 === l2}")  // false
    
    // hashCode() è basato sul contenuto
    println("l1.hashCode() == l2.hashCode()? ${l1.hashCode() == l2.hashCode()}")  // true
    
    // copy() crea una copia modificando alcuni parametri
    val l3 = l1.copy(autore = "Christopher Tolkien")
    println("Copia modificata: $l3")
    println()
    
    println("=" * 80)
    println("PARTE 3: DESTRUCTURING CON COMPONENTN")
    println("=" * 80)
    
    val pc1 = PC(brand = "HP", price = 600, os = "Debian")
    
    // Destrutturazione: estrae i componenti in variabili separate
    val (brand, price, os) = pc1
    println("PC originale: $pc1")
    println("Estratti dalla destrutturazione:")
    println("  Brand: $brand")
    println("  Prezzo: €$price")
    println("  OS: $os")
    
    // Funziona anche con data classes
    val studente = Studente("Mario", 123, "mario@uni.it")
    val (nome, matricola, email) = studente
    println("\nStudente destrutturato:")
    println("  Nome: $nome, Matricola: $matricola, Email: $email")
    println()
    
    println("=" * 80)
    println("PARTE 4: ENUM - INSIEMI DI COSTANTI")
    println("=" * 80)
    
    val type = ExamType.SCRITTO
    println("Tipo esame: $type")
    println("Nome: ${type.name}")  // "SCRITTO"
    println("Ordine: ${type.ordinal}")  // 0
    
    val level = StudyLevel.TRIENNALE
    println("\nLivello studio: $level")
    println("CFU: ${level.cfu}")  // 180
    
    val day = DayOfWeek.VENERDÌ
    println("\n$day")  // "Quasi weekend! 🎉"
    println("È weekend? ${day.isWeekend()}")
    println()
    
    println("=" * 80)
    println("PARTE 5: SEALED CLASSES - PATTERN MATCHING")
    println("=" * 80)
    
    // Creiamo diverse istanze di ExamResult
    val e1: ExamResult = Passed(28)
    val e2: ExamResult = Failed("argomento non studiato")
    val e3: ExamResult = Absent()
    val e4: ExamResult = Deferred()
    
    // Il when su sealed class è ESAUSTIVO
    val results = listOf(e1, e2, e3, e4)
    println("Risultati esami:")
    for (result in results) {
        printResult(result)
    }
    println()
    
    println("=" * 80)
    println("PARTE 6: SINGLETON E COMPANION OBJECTS")
    println("=" * 80)
    
    println("Creazione studenti con companion object:")
    val s1 = Student("Mario", 124)
    s1.printInfo()  // Totale: 1
    
    val s2 = Student("Marco", 125)
    s1.printInfo()  // Totale: 2 (condiviso tra istanze!)
    s2.printInfo()  // Totale: 2 (stesso contatore)
    
    val s3 = Student("Matteo", 126)
    s1.printInfo()  // Totale: 3 (variabile universale per tutte le istanze)
    
    println("\nTotale studenti dal companion object: ${Student.getTotalStudents()}")
    
    // Factory method dal companion object
    val s4 = Student.createWithID("Giovanni")
    s4.printInfo()
    
    println("\nSingleton DatabaseConnection:")
    DatabaseConnection.connect()
    DatabaseConnection.disconnect()
    println()
    
    println("=" * 80)
    println("RIEPILOGO CONCETTI CHIAVE")
    println("=" * 80)
    println("""
        
        ✓ EXTENSION FUNCTIONS:
          - Aggiungono metodi a classi non controllate (String, List, etc.)
          - Non possono sovrascrivere metodi esistenti
          - Utili per aggiungere utilità specifiche al dominio
        
        ✓ DATA CLASSES:
          - Generano automaticamente: equals(), hashCode(), toString()
          - Supportano destructuring automaticamente
          - Forniscono copy() per copie modificate
          - Ideali per contenere dati (non comportamento)
        
        ✓ DESTRUCTURING:
          - Estrae i componenti di un oggetto in variabili separate
          - Syntax: val (var1, var2, var3) = oggetto
          - L'ordine deve corrispondere ai parametri del constructor
        
        ✓ ENUM:
          - Insiemi fissi di costanti predefiniti
          - Possono avere parametri e metodi custom
          - Utili per rappresentare scelte limitate (stati, tipi)
        
        ✓ SEALED CLASSES:
          - Gerarchia di classi CONTROLLATA e nota a compile-time
          - Supportano pattern matching esaustivo con 'when'
          - Il compilatore verifica che tutti i casi siano gestiti
        
        ✓ SINGLETON:
          - object MySingleton { } crea un singolo oggetto globale
          - Thread-safe per default
        
        ✓ COMPANION OBJECTS:
          - Blocco statico dentro una classe
          - Contiene variabili e metodi comuni a tutte le istanze
          - Accessibile tramite ClassName.methodName
    """.trimIndent())
}

// HELPER per stampa decorativa
private operator fun String.times(count: Int) = this.repeat(count)
