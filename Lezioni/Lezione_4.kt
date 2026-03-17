/*
 * ============================================================================
 * LEZIONE 3: CLASSI, ASTRAZIONI, INTERFACCE E INCAPSULAZIONE IN KOTLIN
 * ============================================================================
 *
 * Obiettivi di questa lezione:
 * 1. Comprendere i costruttori primari e secondari
 * 2. Apprendere la differenza tra Interfacce e Classi Astratte
 * 3. Capire l'incapsulazione e i modificatori di accesso
 * 4. Imparare il polimorfismo attraverso le interfacce
 */

// ============================================================================
// PARTE 1: COSTRUTTORI E VALIDAZIONE DELLO STATO
// ============================================================================

/**
 * Classe Student - Esempio di costruttore primario con validazione
 *
 * SPIEGAZIONE IMPORTANTE:
 * - Nel costruttore primario, SOLO le variabili dichiarate con 'val' o 'var'
 *   diventano proprietà della classe e sono accessibili dopo l'inizializzazione
 * - Le variabili NON dichiarate con val/var sono solo parametri del costruttore
 * - L'init block è il primo luogo dove difendere lo STATO della classe
 */
class Student(
    val name: String,           // Proprietà immutabile della classe
    val studentId: Int,         // Proprietà immutabile della classe
    var cfu: Int                // Proprietà mutabile della classe
) {

    // ✓ BUONA PRATICA: La validazione dello stato avviene qui, nell'init
    init {
        // require() lancia un'eccezione se la condizione è falsa
        // Questo impedisce di creare oggetti Student con CFU negativi
        require(cfu >= 0) { "I crediti (CFU) devono essere positivi o nulli" }
    }

    // ⚠️ IMPORTANTE - Costruttore Secondario
    // Quando usiamo un costruttore secondario, DEVE sempre delegare al costruttore
    // primario tramite 'this(...)'. Questo assicura che l'init block sia sempre eseguito
    constructor(name: String, studentId: Int) : this(name, studentId, 0) {
        // Questo codice viene eseguito DOPO l'init block
        // Utile per inizializzazioni aggiuntive specifiche di questo costruttore
        this.cfu += 10  // Inizializziamo con 10 CFU aggiuntivi se non specificati
    }

    // NOTA DI DESIGN: La validazione (require) è stata fatta nell'init, non qui.
    // Questo garantisce che TUTTI i costruttori passino per la stessa validazione.
}

// ============================================================================
// PARTE 2: INTERFACCE E CLASSI ASTRATTE - I CONTRATTI
// ============================================================================

/*
 * DIFFERENZA CHIAVE:
 *
 * INTERFACCIA:
 * - Definisce funzioni SENZA corpo (funzioni astratte)
 * - NON può avere stato (variabili di istanza)
 * - Rappresenta un "contratto": "chi ti implementa DEVE avere questi metodi"
 * - Uso: quando vuoi definire UN COMPORTAMENTO che diverse classi devono seguire
 *
 * CLASSE ASTRATTA:
 * - Può avere funzioni con corpo E funzioni astratte
 * - PUÒ avere stato (proprietà private, protected)
 * - NON può essere istanziata direttamente
 * - Uso: quando vuoi condividere sia COMPORTAMENTO che STATO tra sottoclassi
 *
 * EREDITÀ IN KOTLIN:
 * ⚠️ ATTENZIONE: In Kotlin, le classi sono "final" per default!
 * Non puoi estendere una classe normale. Puoi estendere SOLO:
 * - Classi astratte
 * - Classi esplicitamente dichiarate come "open"
 * Questo è per ragioni di sicurezza e design.
 */

// --- INTERFACCIA ---
/**
 * Interfaccia Printable - Definisce il "contratto" per la stampa
 *
 * Qualsiasi classe che implementa questa interfaccia DEVE fornire
 * un'implementazione del metodo printInfo()
 */
interface Printable {
    /**
     * Metodo astratto che tutte le implementazioni devono fornire.
     * Non ha corpo, è solo una "firma" che le sottoclassi devono implementare.
     */
    fun printInfo()
}

// --- CLASSE ASTRATTA ---
/**
 * Classe astratta Person - Condivide stato E comportamento tra sottoclassi
 *
 * PERCHÉ astratta?
 * - Ha proprietà comuni a tutti gli utenti (name, surname)
 * - Ha uno stato privato (accessCounter) che vogliamo proteggere
 * - Definisce il metodo printInfo() come responsabilità della sottoclasse
 * - Non ha senso creare un "Person" generico senza un ruolo specifico
 */
abstract class Person(
    val name: String,
    val surname: String
) {
    // ⚠️ IMPORTANTE: Proprietà astratta
    // Ogni sottoclasse DEVE fornire un valore per 'role'
    abstract val role: String

    // INCAPSULAZIONE - Stato privato
    // Questa variabile è PRIVATA: accessibile SOLO dentro questa classe
    // NON è visibile dalle sottoclassi!
    private var accessCounter: Int = 0

    // INCAPSULAZIONE - Metodo protetto
    // PROTECTED significa: accessibile SOLO in questa classe e nelle sottoclassi
    // Non accessibile dall'esterno della gerarchia di classi
    protected fun incrementAccessCounter() {
        accessCounter++  // Posso accedere qui perché sono dentro la classe
    }

    /**
     * Metodo concreto che tutte le sottoclassi ereditano
     * Questo metodo IMPLEMENTA il comportamento della stampa
     */
    fun printInfo() {
        // Posso accedere ad 'accessCounter' qui perché sono dentro Person
        // (private è accessibile dai metodi della stessa classe)
        accessCounter++

        // String interpolation con ${}
        println("Name: $name, $surname, Role: $role")
    }
}

// ============================================================================
// PARTE 3: IMPLEMENTAZIONE DELL'INTERFACCIA - POLIMORFISMO
// ============================================================================

/**
 * Classe Studente2 - Implementa l'interfaccia Printable
 *
 * NOTA: Questa classe NON eredita da Person (è indipendente)
 * La sintassi ": Printable" sostituisce "implements Printable" di Java
 */
class Studente2(
    val name: String,
    var studentId: Int
) : Printable {
    // OBBLIGATORIO: Override del metodo astratto dell'interfaccia
    override fun printInfo() {
        println("Name: $name, studentId: $studentId")
    }
}

// ============================================================================
// PARTE 4: EREDITÀ DA CLASSI ASTRATTE
// ============================================================================

/**
 * Classe Studente3 - Eredita dalla classe astratta Person
 *
 * Cosa succede qui:
 * - Eredita le proprietà name e surname
 * - Eredita il metodo printInfo() concreto
 * - DEVE fornire un'implementazione per la proprietà astratta 'role'
 * - NON può accedere ad accessCounter perché è PRIVATE
 */
class Studente3(
    name: String,
    surname: String
) : Person(name, surname) {  // Delego al costruttore della classe astratta

    // OBBLIGATORIO: Override della proprietà astratta
    override val role: String = "studente"

    // ⚠️ ERRORE DI COMPRENSIONE COMUNE:
    // Se provassimo a fare:
    //     accessCounter++
    // Otterremmo ERRORE! Perché accessCounter è PRIVATE in Person
    // e i membri privati NON vengono ereditati.
    //
    // Se volessimo incrementarlo, dovremmo:
    // 1. Usare un metodo protetto come incrementAccessCounter() da Person
    // 2. Oppure renderlo protected/internal nella classe astratta
}

/**
 * Classe Professore - Eredita dalla classe astratta Person
 * Altro esempio di sottoclasse che implementa il ruolo astratto
 */
class Professore(
    name: String,
    surname: String
) : Person(name, surname) {

    override val role: String = "Professore"
}

/**
 * Classe Corso - Implementa l'interfaccia Printable
 * Dimostra che DIVERSE classi possono implementare la stessa interfaccia
 */
class Corso(
    val name: String,
    val docente: String
) : Printable {

    override fun printInfo() {
        println("Nome: $name, docente: $docente")
    }
}

// ============================================================================
// PARTE 5: DIMOSTRAZIONE PRATICA - MAIN
// ============================================================================

fun main() {

    println("=" * 70)
    println("PARTE 1: COSTRUTTORI E VALIDAZIONE")
    println("=" * 70)

    // Creiamo istanze di Student usando il costruttore primario
    val s1 = Student("Jack", 28123, 28)
    val s2 = Student("John", 2812323, 18)

    // Creiamo un'istanza usando il costruttore secondario (2 parametri)
    // Il CFU verrà inizializzato a 10 (come definito nel costruttore secondario)
    val s3 = Student("John", 281234323)

    println("Studente 1: ${s1.name} - ID: ${s1.studentId} - CFU: ${s1.cfu}")
    println("Studente 2: ${s2.name} - ID: ${s2.studentId} - CFU: ${s2.cfu}")
    println("Studente 3: ${s3.name} - ID: ${s3.studentId} - CFU: ${s3.cfu}")

    // ⚠️ QUESTO DAREBBE ERRORE:
    // val s4 = Student("Bob", 137289, -12)
    // Errore: require() fallirebbe perché CFU < 0

    println()
    println("=" * 70)
    println("PARTE 2: IMPLEMENTAZIONE DI INTERFACCE")
    println("=" * 70)

    // Creiamo oggetti che implementano Printable
    val studente = Studente2("James", 1234)
    val corso1 = Corso("PW", "Loreti")
    val corso2 = Corso("SO", "Croce")

    // Ogni oggetto implementa printInfo() a modo suo
    studente.printInfo()
    corso1.printInfo()
    corso2.printInfo()

    println()
    println("=" * 70)
    println("PARTE 3: POLIMORFISMO CON INTERFACCE")
    println("=" * 70)

    // ✨ POLIMORFISMO IN AZIONE:
    // Possiamo mettere in una lista oggetti di DIVERSI tipi
    // purché implementino l'interfaccia Printable.
    // Quando chiamiamo printInfo(), Kotlin chiama la versione corretta
    // per ogni oggetto (questo si chiama "dynamic dispatch")

    val daStampare: List<Printable> = listOf(
        Studente2("gino", 123),
        Corso("PW", "Loreti"),
        Studente2("gina", 321),
        Corso("SO", "Croce")
    )

    println("Oggetti polimorfici:")
    for (elemento in daStampare) {
        elemento.printInfo()  // Chiama il metodo corretto per ogni tipo!
    }

    println()
    println("=" * 70)
    println("PARTE 4: EREDITÀ DA CLASSI ASTRATTE")
    println("=" * 70)

    // Creiamo istanze di sottoclassi di Person
    val studente3 = Studente3("Paul", "Miller")
    val professore = Professore("Mario", "Verdi")

    // Entrambi hanno ereditato il metodo printInfo() da Person
    // e l'implementazione è la stessa per tutti
    println("Studente:")
    studente3.printInfo()

    println("Professore:")
    professore.printInfo()

    println()
    println("=" * 70)
    println("RIEPILOGO DEI CONCETTI")
    println("=" * 70)
    println("""
        
        ✓ COSTRUTTORI:
          - Costruttore primario: parametri nel class declaration con val/var
          - Costruttore secondario: delegano sempre al primario con 'this'
          - init block: eseguito per PRIMO, ideale per validazione
        
        ✓ INTERFACCE:
          - Definiscono contratti (quali metodi implementare)
          - Non hanno stato proprio
          - Si implementano con 'override'
          - Una classe può implementare PIÙ interfacce
        
        ✓ CLASSI ASTRATTE:
          - Condividono stato E comportamento
          - Non possono essere istanziate
          - Si ereditano (estendono) una sola classe astratta
        
        ✓ INCAPSULAZIONE:
          - private: accessibile solo nella classe
          - protected: accessibile nella classe e nelle sottoclassi
          - public (default): accessibile da qualsiasi posto
          - I membri private NON vengono ereditati
        
        ✓ POLIMORFISMO:
          - Oggetti di diversi tipi possono rispondere alla stessa interfaccia
          - Il metodo corretto viene scelto al runtime
          - Utile per scrivere codice generico e flessibile
    """.trimIndent())
}

// HELPER per stampa decorativa (non è una feature di Kotlin, solo per la lezione)
private operator fun String.times(count: Int) = this.repeat(count)
