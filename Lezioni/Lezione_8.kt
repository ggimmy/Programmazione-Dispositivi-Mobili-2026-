/*
 * ============================================================================
 * LEZIONE 8: GENERICS, TYPE PARAMETERS, VARIANCE E INLINE FUNCTIONS
 * ============================================================================
 *
 * Obiettivi di questa lezione:
 * 1. Comprendere perché servono i generics (riuso del codice)
 * 2. Imparare i type parameters e come utilizzarli
 * 3. Capire i type bounds e i constraints
 * 4. Apprendere la varianza (covarianza, contravarianza, invarianza)
 * 5. Scoprire type projections (in, out, *)
 * 6. Imparare i reified type parameters per accedere a informazioni di tipo a runtime
 * 7. Comprendere le inline functions e quando usarle
 */

// ============================================================================
// INTRODUZIONE: PERCHÉ SERVONO I GENERICS?
// ============================================================================

/*
 * GENERICS = Modo per scrivere codice che funziona con MOLTEPLICI tipi
 *
 * PROBLEMA SENZA GENERICS:
 * Se vuoi una funzione sort che funzioni per Int, String, Double, etc.,
 * dovresti scrivere:
 *     fun sortInt(list: List<Int>) { }
 *     fun sortString(list: List<String>) { }
 *     fun sortDouble(list: List<Double>) { }
 *
 * SOLUZIONE CON GENERICS:
 * Scrivi una sola volta con un type parameter T:
 *     fun <T> sort(list: List<T>) { }
 *
 * VANTAGGI:
 * - DRY principle (Don't Repeat Yourself)
 * - Type-safe (il compilatore verifica i tipi)
 * - Riuso del codice
 * - Sintassi pulita
 *
 * SINTASSI:
 *     fun <T> functionName(param: T): T { }
 *     class ClassName<T> { }
 *     interface InterfaceName<T> { }
 */

// ============================================================================
// PARTE 1: TYPE PARAMETERS SEMPLICI
// ============================================================================

/*
 * TYPE PARAMETER = Placeholder per un tipo che sarà specificato in seguito
 *
 * SINTASSI:
 *     fun <T> myFunction(item: T): T { return item }
 *     class Box<T>(val value: T)
 *
 * CONVENZIONI PER I NOMI:
 * - T per Type
 * - E per Element
 * - K per Key
 * - V per Value
 * - U, V, W per tipi successivi
 *
 * ⚠️ IMPORTANTE - ERASURE:
 * I generics sono "erasati" a runtime! Questo significa:
 * - Box<Int> e Box<String> diventano entrambi Box a runtime
 * - Non puoi fare: if (x is Box<Int>)  <-- ERRORE!
 * - È una limitazione dovuta alla compatibilità con Java
 *
 * SOLUZIONE: Usa reified type parameters (vedi PARTE 7)
 */

/**
 * Funzione generica semplice - Ritorna lo stesso elemento
 */
fun <T> identity(item: T): T {
    return item
}

/**
 * Funzione con multipli type parameters
 */
fun <T, U> combine(first: T, second: U): Pair<T, U> {
    return Pair(first, second)
}

/**
 * Classe generica semplice
 */
class Box<T>(val value: T) {
    fun get(): T = value

    override fun toString() = "Box($value)"
}

// ============================================================================
// PARTE 2: TYPE BOUNDS - VINCOLI SUI TYPE PARAMETERS
// ============================================================================

/*
 * TYPE BOUNDS = Limitazioni sul tipo che può essere assegnato al type parameter
 *
 * SINTASSI:
 *     fun <T : SuperType> functionName(item: T) { }
 *
 * SIGNIFICATO: T può essere SuperType o qualsiasi sottoclasse di SuperType
 *
 * ESEMPIO:
 *     fun <T : Comparable<T>> max(a: T, b: T) { }
 * Qui T DEVE implementare Comparable<T>
 *
 * ⚠️ SENZA BOUNDS:
 * - Non puoi chiamare metodi su T (non sai quale tipo è!)
 * - È come T fosse di tipo Any (il bound di default)
 *
 * CON BOUNDS:
 * - Puoi chiamare metodi del bound
 * - Il compilatore verifica che siano disponibili
 */

/**
 * Funzione che richiede Comparable - Trova il massimo tra due elementi
 *
 * Il bound <T : Comparable<T>> significa:
 * "T deve implementare Comparable e deve poter comparare se stesso"
 */
fun <T : Comparable<T>> maxOfTwo(a: T, b: T): T {
    // Possiamo usare > perché Comparable fornisce compareTo()
    return if (a > b) a else b
}

/**
 * Funzione con multipli bounds - Usa 'where' keyword
 */
interface Movable {
    fun move(): String
}

interface Named {
    val name: String
}

/**
 * moveAndReport richiede che T implementi SIA Movable CHE Named
 *
 * SINTASSI con 'where':
 *     fun <T> func(x: T) where T : Bound1, T : Bound2
 */
fun <T> moveAndReport(entity: T): String where T : Movable, T : Named {
    return "${entity.name} says: ${entity.move()}"
}

/**
 * Classe generica con type bound
 */
class Container<T : Number>(val value: T) {
    fun doubleValue(): Double = value.toDouble() * 2

    override fun toString() = "Container($value)"
}

// ============================================================================
// PARTE 3: TYPE PROJECTIONS - VARIANZA
// ============================================================================

/*
 * VARIANZA = Relazione tra i tipi generici quando si ereditano
 *
 * PROBLEMA:
 * List<Int> NON è una sottoclasse di List<Any>, anche se Int è sottoclasse di Any!
 * Questo è un problema di design - perché?
 *
 * RISPOSTA:
 * Se List<Int> fosse sottoclasse di List<Any>, potresti fare:
 *     val intList: List<Int> = listOf(1, 2, 3)
 *     val anyList: List<Any> = intList  // ✓ Se fosse covariante
 *     anyList.add("stringa")            // ✗ ERRORE DI TIPO!
 *
 * Quindi List è INVARIANTE (non covariante, non contravariante)
 *
 * SOLUZIONE: USE-SITE VARIANCE
 * Usa in/out per dire al compilatore come intendi usare il tipo
 *
 * THREE TYPES OF VARIANCE:
 *
 * 1. INVARIANZA (nessun modificatore):
 *    - List<Int> NON è relazionato a List<Any>
 *    - Puoi leggere E scrivere
 *
 * 2. COVARIANZA (out):
 *    - List<Int> è sottoclasse di List<out Any>
 *    - Puoi SOLO leggere (output di T)
 *    - Non puoi scrivere (input di T sarebbe unsafe)
 *
 * 3. CONTRAVARIANZA (in):
 *    - List<Any> è sottoclasse di List<in Int>
 *    - Puoi SOLO scrivere (input di T)
 *    - Non puoi leggere (output di T non sarebbe sicuro)
 */

/**
 * Star projection - Non importa che tipo sia dentro
 *
 * Usa quando non importa il tipo generico (es. per stampare size)
 */
fun printListSize(list: List<*>) {
    println("List size: ${list.size}")
}

/**
 * Out projection - Covarianza (solo lettura)
 *
 * Puoi leggere T ma non scrivere
 * List<Int> è sottoclasse di List<out Number>
 */
fun getFirstElement(list: List<out Number>): Number? {
    return list.firstOrNull()
    // list.add(5)  // ✗ ERRORE! Non puoi aggiungere
}

/**
 * In projection - Contravarianza (solo scrittura)
 *
 * Puoi scrivere T ma non leggere (o leggi come Any)
 * List<Number> è sottoclasse di List<in Int>
 */
fun addNumbers(list: MutableList<in Int>) {
    list.add(5)
    list.add(10)
    // val num: Int = list[0]  // ✗ ERRORE! Non puoi leggere come Int
    // val any: Any = list[0]  // ✓ OK, leggi come Any
}

// ============================================================================
// PARTE 4: DECLARATION-SITE VARIANCE
// ============================================================================

/*
 * DECLARATION-SITE VARIANCE = Specifichiamo la varianza nel momento in cui
 * definiamo la classe/interfaccia generica
 *
 * DIFFERENZA DA USE-SITE VARIANCE:
 * - Use-site: Specifichi in/out quando usi il tipo
 * - Declaration-site: Specifichi in/out nella definizione
 *
 * VANTAGGI DECLARATION-SITE:
 * - Una volta per tutte
 * - Meno boilerplate quando usi il tipo
 * - Comunica l'intenzione
 *
 * ESEMPIO:
 *     interface Holder<out T> { }  // Sempre covariante
 * vs
 *     fun process(h: Holder<out T>) { }  // Covariante solo qui
 */

/**
 * Interface generica covariante - Holder può SOLO dare T (out)
 *
 * Grazie a 'out', Holder<Int> è sottoclasse di Holder<Any>
 */
interface Producer<out T> {
    fun produce(): T
    // fun consume(item: T) { }  // ✗ ERRORE! Non puoi con out
}

/**
 * Interface generica contravariante - Consumer può SOLO ricevere T (in)
 *
 * Grazie a 'in', Consumer<Any> è sottoclasse di Consumer<Int>
 */
interface Consumer<in T> {
    fun consume(item: T)
    // fun produce(): T { }  // ✗ ERRORE! Non puoi con in
}

/**
 * Implementazione di Producer covariante
 */
class IntProducer : Producer<Int> {
    override fun produce(): Int = 42
}

/**
 * Implementazione di Consumer contravariante
 */
class AnyConsumer : Consumer<Any> {
    override fun consume(item: Any) {
        println("Consumed: $item")
    }
}

// ============================================================================
// PARTE 5: HOLDER EXAMPLE - IN/OUT PROJECTION AVANZATO
// ============================================================================

/*
 * HOLDER PATTERN = Esempio pratico di varianza
 *
 * Un Holder è una struttura che:
 * - push(item): aggiunge un elemento
 * - pop(): rimuove e ritorna un elemento
 *
 * Quando usi <in T> in push, la gerarchia si INVERTE
 * Quando usi <out T> in pop, la gerarchia rimane NORMALE
 */

/**
 * Interface Holder generica - Invariante (no in/out)
 */
interface Holder<T> {
    fun push(item: T)
    fun pop(): T
    fun size(): Int
}

/**
 * Implementazione di Holder
 */
class HolderImpl<T>(var value: T? = null) : Holder<T> {

    private val list = mutableListOf<T>()

    override fun push(item: T) {
        list.add(item)
    }

    override fun pop(): T {
        return list.removeAt(0)
    }

    override fun size(): Int {
        return list.size
    }

    /**
     * Metodo con contravarianza nel parametro
     *
     * SIGNIFICATO:
     * Se HolderImpl<B> vuole regalare un elemento a un altro holder,
     * allora è safe passare un HolderImpl<in B>, ossia uno che accetti
     * anche superclassi di B (e un superclasse di B può accettare B)
     *
     * ESEMPIO PRATICO:
     * class Animal
     * class Dog : Animal
     *
     * val dogHolder: HolderImpl<Dog> = HolderImpl()
     * val animalHolder: HolderImpl<in Animal> = HolderImpl()
     * dogHolder.gift(animalHolder)  // ✓ Safe: animalHolder accetta Animal
     */
    fun gift(other: Holder<in T>) {
        if (size() > 0) {
            other.push(pop())
        }
    }

    override fun toString() = "HolderImpl(size=${size()})"
}

// ============================================================================
// PARTE 6: REIFIED TYPE PARAMETERS - ACCEDERE AL TIPO A RUNTIME
// ============================================================================

/*
 * PROBLEMA CON I GENERICS:
 * I generics sono ERASATI a runtime! Significa:
 *     fun <T> checkType(item: T) {
 *         if (item is T) { }  // ✗ ERRORE! T non è noto a runtime
 *     }
 *
 * SOLUZIONE: REIFIED TYPE PARAMETERS
 *
 * 'reified' permette di accedere al tipo T a runtime!
 * MA: deve essere usato SOLO in funzioni INLINE
 *
 * PERCHÉ FUNZIONA:
 * Il compilatore, per le funzioni inline, sostituisce il codice
 * direttamente dove viene chiamato (inlining), permettendo di conoscere
 * il tipo effettivo.
 *
 * SINTASSI:
 *     inline fun <reified T> functionName(item: Any) {
 *         if (item is T) { }  // ✓ OK!
 *     }
 */

/**
 * Funzione inline con reified - Puoi fare type checking a runtime
 */
inline fun <reified T> parseOrNull(value: Any): T? {
    return if (value is T) value else null
}

/**
 * Funzione inline con reified - Controlla se è istanza di tipo
 */
inline fun <reified T> isInstanceOf(value: Any): Boolean {
    return value is T
}

/**
 * Funzione inline con reified - Stampa il nome del tipo
 */
inline fun <reified T> printTypeName(value: T) {
    println("Tipo: ${T::class.simpleName}")
}

// ============================================================================
// PARTE 7: INLINE FUNCTIONS - QUANDO E PERCHÉ USARLE
// ============================================================================

/*
 * INLINE FUNCTIONS = Funzioni il cui codice viene copiato al punto di chiamata
 *
 * NORMALE FUNZIONE:
 *     fun execute(block: () -> Unit) {
 *         block()  // Chiama il lambda
 *     }
 *
 * INLINE FUNCTION:
 *     inline fun execute(block: () -> Unit) {
 *         block()  // Il codice di block() viene copiato QUI
 *     }
 *
 * VANTAGGI:
 * - Nessun overhead di function call
 * - Accesso a reified type parameters
 * - Migliori performance per funzioni molto utilizzate
 * - Permette return da lambda/lambdas in block
 *
 * SVANTAGGI:
 * - Aumenta la dimensione del bytecode
 * - Meno leggibile per debug
 * - Non usare per funzioni grandi
 *
 * QUANDO USARE:
 * - Funzioni molto piccole
 * - Funzioni usate frequentemente
 * - Funzioni con reified type parameters
 * - Funzioni di alto ordine con lambda
 *
 * QUANDO NON USARE:
 * - Funzioni grandi
 * - Funzioni non critiche per performance
 * - Funzioni ricorsive
 */

/**
 * Inline function semplice
 */
inline fun <T> measure(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    val elapsed = System.currentTimeMillis() - start
    return Pair(result, elapsed)
}

/**
 * Inline function con reified
 */
inline fun <reified T> List<*>.filterByType(): List<T> {
    return filter { it is T }.map { it as T }
}

// ============================================================================
// PARTE 8: DIMOSTRAZIONE PRATICA - MAIN
// ============================================================================

fun main() {

    println("=" * 80)
    println("PARTE 1: TYPE PARAMETERS SEMPLICI")
    println("=" * 80)

    // Funzione generica - Identity
    val num = identity(42)
    val str = identity("Kotlin")
    println("identity(42) = $num (tipo: ${num::class.simpleName})")
    println("identity(\"Kotlin\") = $str (tipo: ${str::class.simpleName})")

    // Funzione con multipli type parameters
    val pair = combine("Ciao", 123)
    println("\ncombine(\"Ciao\", 123) = $pair")

    // Classe generica
    val boxInt = Box(42)
    val boxStr = Box("Generics")
    println("\n$boxInt")
    println(boxStr)

    println()
    println("=" * 80)
    println("PARTE 2: TYPE BOUNDS - VINCOLI SUI TIPI")
    println("=" * 80)

    println("maxOfTwo(3, 5) = ${maxOfTwo(3, 5)}")
    println("maxOfTwo(\"a\", \"z\") = ${maxOfTwo("a", "z")}")
    println("maxOfTwo(3.14, 2.71) = ${maxOfTwo(3.14, 2.71)}")

    // ✓ Box<Number> accetta qualsiasi sottoclasse di Number
    val boxNumber: Box<Number> = Box(3)
    println("\nBox<Number> = Box(3): $boxNumber")

    // Container ha bound <T : Number>
    val container = Container(10)
    println("Container(10).doubleValue() = ${container.doubleValue()}")

    println()
    println("=" * 80)
    println("PARTE 3: MULTIPLE BOUNDS CON 'WHERE'")
    println("=" * 80)

    // Classe che implementa sia Movable che Named
    class Robot(val name: String) : Movable {
        override fun move(): String = "beep boop"
    }

    val robot = Robot("R2D2")
    //println(moveAndReport(robot))

    println()
    println("=" * 80)
    println("PARTE 4: TYPE PROJECTIONS - STAR PROJECTION")
    println("=" * 80)

    val intList: List<Int> = listOf(1, 2, 3)
    val strList: List<String> = listOf("a", "b", "c")

    println("intList: $intList")
    print("Size: ")
    printListSize(intList)

    println("strList: $strList")
    print("Size: ")
    printListSize(strList)

    println()
    println("=" * 80)
    println("PARTE 5: OUT PROJECTION - COVARIANZA (LETTURA)")
    println("=" * 80)

    val numbers: List<Int> = listOf(1, 2, 3)
    println("numbers: $numbers")
    println("First element (out projection): ${getFirstElement(numbers)}")
    println("\nCon out projection puoi LEGGERE ma non SCRIVERE")

    println()
    println("=" * 80)
    println("PARTE 6: IN PROJECTION - CONTRAVARIANZA (SCRITTURA)")
    println("=" * 80)

    val mutableNumbers: MutableList<Int> = mutableListOf()
    println("Mutable list prima: $mutableNumbers")
    addNumbers(mutableNumbers)
    println("Mutable list dopo addNumbers: $mutableNumbers")
    println("\nCon in projection puoi SCRIVERE ma non LEGGERE come tipo specifico")

    println()
    println("=" * 80)
    println("PARTE 7: DECLARATION-SITE VARIANCE")
    println("=" * 80)

    val intProducer: Producer<Int> = IntProducer()
    val anyProducer: Producer<Any> = intProducer  // ✓ Grazie a 'out'
    println("IntProducer produce: ${intProducer.produce()}")

    val anyConsumer: Consumer<Any> = AnyConsumer()
    val intConsumer: Consumer<Int> = anyConsumer as Consumer<Int>  // ✓ Grazie a 'in'
    println("AnyConsumer consume:")
    anyConsumer.consume(42)
    anyConsumer.consume("String")

    println()
    println("=" * 80)
    println("PARTE 8: HOLDER EXAMPLE - IN/OUT PROJECTION PRATICO")
    println("=" * 80)

    val holder1: HolderImpl<String> = HolderImpl()
    val holder2: HolderImpl<Any> = HolderImpl()

    holder1.push("ciao")
    holder1.push("mondo")
    println("holder1: $holder1, elementi: 2")
    println("holder2: $holder2, elementi: 0")

    println("\nChiamo holder1.gift(holder2) - trasferisce un elemento")
    holder1.gift(holder2)
    println("holder1: $holder1, elementi: 1")
    println("holder2: $holder2, elementi: 1")

    println()
    println("=" * 80)
    println("PARTE 9: REIFIED TYPE PARAMETERS")
    println("=" * 80)

    val value: Any = 42
    println("value = $value")
    println("parseOrNull<Int>(value) = ${parseOrNull<Int>(value)}")
    println("parseOrNull<String>(value) = ${parseOrNull<String>(value)}")

    println("\nisInstanceOf<Int>(42) = ${isInstanceOf<Int>(42)}")
    println("isInstanceOf<String>(42) = ${isInstanceOf<String>(42)}")

    println("\nprintTypeName(42):")
    printTypeName(42)
    println("printTypeName(\"Kotlin\"):")
    printTypeName("Kotlin")

    println()
    println("=" * 80)
    println("PARTE 10: INLINE FUNCTIONS")
    println("=" * 80)

    // Inline function con reified
    val mixedList: List<Any> = listOf(1, "ciao", 2, 3.14, "mondo", 4)
    println("mixedList: $mixedList")

    val ints: List<Int> = mixedList.filterByType()
    val strings: List<String> = mixedList.filterByType()
    println("Solo Int: $ints")
    println("Solo String: $strings")

    // Inline function per misurare tempo
    println("\nmeasure execution time:")
    val (result, elapsed) = measure {
        (1..1000000).sum()
    }
    println("  Risultato: $result")
    println("  Tempo impiegato: ${elapsed}ms")

    println()
    println("=" * 80)
    println("RIEPILOGO CONCETTI CHIAVE")
    println("=" * 80)
    println("""
        
        ✓ TYPE PARAMETERS:
          - <T> è un placeholder per un tipo generico
          - Permette di scrivere codice riusabile
          - Completamente type-safe
        
        ✓ TYPE BOUNDS:
          - <T : SuperType> limita T a SuperType e sottoclassi
          - Con 'where' puoi specificare multipli bounds
          - Permette di usare metodi di SuperType su T
        
        ✓ ERASURE:
          - I generics sono cancellati a runtime
          - Box<Int> e Box<String> diventano Box a runtime
          - Non puoi fare: if (x is T)
        
        ✓ VARIANZA (3 tipi):
          - INVARIANZA: nessun modificatore, nessuna relazione
          - COVARIANZA (out): T è in output, List<Int> ⊆ List<out Any>
          - CONTRAVARIANZA (in): T è in input, List<Any> ⊆ List<in Int>
        
        ✓ TYPE PROJECTIONS:
          - List<*>: non importa il tipo
          - List<out T>: solo lettura (covarianza)
          - List<in T>: solo scrittura (contravarianza)
          - Use-site variance vs Declaration-site variance
        
        ✓ REIFIED TYPE PARAMETERS:
          - Accedi al tipo a runtime con <reified T>
          - Funziona SOLO in funzioni inline
          - Permette: if (x is T), T::class, etc.
        
        ✓ INLINE FUNCTIONS:
          - Codice copiato al punto di chiamata
          - Migliore performance per funzioni piccole
          - Necessario per reified type parameters
          - Aumenta la dimensione del bytecode
    """.trimIndent())
}

// HELPER per stampa decorativa
private operator fun String.times(count: Int) = this.repeat(count)
