/*
 * ============================================================================
 * LEZIONE 5: PROPRIETÀ, GETTER/SETTER, OPERATOR OVERLOADING E EXTENSIONS
 * ============================================================================
 *
 * Obiettivi di questa lezione:
 * 1. Comprendere getter e setter custom in Kotlin
 * 2. Imparare quando usare 'open' vs 'abstract'
 * 3. Apprendere l'operator overloading
 * 4. Capire le extension functions e i loro limiti
 */

// ============================================================================
// PARTE 1: PROPRIETÀ DELL'OGGETTO - GETTER E SETTER
// ============================================================================

/*
 * IN KOTLIN, OGNI PROPRIETÀ HA IMPLICITAMENTE:
 *
 * Quando scrivi:
 *     val nome: String = "Mario"
 *     var eta: Int = 25
 *
 * Kotlin AUTOMATICAMENTE genera getter e setter (per var) dietro le quinte.
 * Questo significa che quando fai:
 *     persona.eta = 26
 *     println(persona.eta)
 *
 * Stai REALMENTE chiamando:
 *     persona.setEta(26)
 *     println(persona.getEta())
 *
 * Ma in Kotlin la sintassi è semplificata. Possiamo però CUSTOMIZZARE
 * questi getter e setter per aggiungere logica personalizzata!
 *
 * SYNTAX:
 *     var propertyName: Type = value
 *         get() {
 *             return field  // 'field' è il backing field
 *         }
 *         set(value) {
 *             field = value
 *         }
 *
 * 'field' è una keyword speciale che rappresenta il valore reale memorizzato.
 * Non è possibile accedere direttamente alla proprietà nel getter/setter,
 * bisogna usare 'field' altrimenti andremmo in ricorsione infinita.
 */

/**
 * Classe PositiveAttitude - Esempio di getter/setter custom
 *
 * CASO D'USO: Vogliamo che 'attitude' non possa mai essere negativo.
 * I numeri negativi vengono automaticamente convertiti a 0.
 */
class PositiveAttitude(startingAttribute: Int) {

    // Proprietà con getter e setter custom
    var attitude: Int = 0
        get() {
            // ✓ GETTER CUSTOM
            // Questo codice viene eseguito OGNI volta che accediamo a 'attitude'
            println("get")  // Log per vedere quando viene chiamato
            return field    // Ritorniamo il valore reale memorizzato
        }
        set(value) {
            // ✓ SETTER CUSTOM
            // Questo codice viene eseguito OGNI volta che assegniamo a 'attitude'
            // Qui implementiamo la LOGICA: se negativo, diventa 0
            println("set($value)")  // Log per vedere quando viene chiamato
            field = if (value < 0) 0 else value
        }

    init {
        // Nel blocco init, quando assegniamo a 'attitude',
        // il setter custom viene automaticamente chiamato
        attitude = startingAttribute
    }
}

// ============================================================================
// PARTE 2: CLASSI OPEN E PROPRIETÀ OPEN - EREDITARIETÀ CONTROLLATA
// ============================================================================

/*
 * ⚠️ RICORDA: In Kotlin, le classi sono FINAL per default (non estendibili)
 * Questo è diverso da Java dove sono estendibili per default.
 *
 * KEYWORDS PER CONTROLLARE L'EREDITARIETÀ:
 *
 * 1. 'open' su una classe:
 *    - Consente alle altre classi di ESTENDERLA
 *    - La classe può comunque essere istanziata direttamente
 *    - È una classe non astratta ma estendibile
 *
 * 2. 'open' su una proprietà:
 *    - Consente alle sottoclassi di fare OVERRIDE della proprietà
 *    - Senza 'open', non puoi fare override
 *
 * 3. 'abstract' su una classe:
 *    - NON può essere istanziata direttamente
 *    - DEVE essere estesa
 *    - È un contratto
 *
 * 4. 'final' su un override:
 *    - Proibisce ulteriori override nelle sottoclassi di sottoclassi
 *    - "Blocca" l'override da questo punto in poi
 *
 * DIFFERENZA FONDAMENTALE: open vs abstract
 * ┌─────────────────┬────────────────────┬──────────────────┐
 * │ Caratteristica  │ open class         │ abstract class   │
 * ├─────────────────┼────────────────────┼──────────────────┤
 * │ Istanziazione   │ ✓ Possibile        │ ✗ NON possibile  │
 * │ Estensione      │ ✓ Possibile        │ ✓ Possibile      │
 * │ Implementazione │ Sì, ha corpo       │ Può avere corpo  │
 * │ Uso             │ Quando vuoi che    │ Quando vuoi un   │
 * │                 │ sia sia usato che  │ contratto        │
 * │                 │ esteso             │                  │
 * └─────────────────┴────────────────────┴──────────────────┘
 */

/**
 * Classe base open - Definisce un piano di studi generico
 * È OPEN, quindi può essere estesa, ma può anche essere istanziata
 */
open class StudyPlan {
    // ⚠️ IMPORTANTE: Le proprietà devono essere 'open' se vogliamo fare override
    open val maxcfu: Int = 0           // Proprietà aperta per override
    open var cfuNumber: Int = 0        // Funziona anche con var (mutabile)
}

/**
 * Classe TriennaleStudyPlan - Override di proprietà
 * Specializza StudyPlan per una laurea triennale (3 anni)
 */
class TriennaleStudyPlan : StudyPlan() {
    // ✓ Override delle proprietà della superclasse
    // NOTA: Scriviamo i valori specifici per una laurea triennale
    override val maxcfu = 180          // Una triennale ha max 180 CFU
    override var cfuNumber = 30        // Di solito 30 CFU per anno
}

/**
 * Classe MagistraleStudyPlan - Altro esempio di override
 * Specializza StudyPlan per una laurea magistrale (2 anni)
 */
class MagistraleStudyPlan : StudyPlan() {
    // Una magistrale ha diversi CFU totali
    override val maxcfu = 120          // Una magistrale ha max 120 CFU
}

/**
 * Classe NuovoTriennaleStudyPlan - Esempio di FINAL override
 *
 * Qui usiamo 'final' per BLOCCARE ulteriori override
 * Se qualcuno provasse a creare una sottoclasse di questa
 * e fare override di maxcfu, otterrebbe un errore.
 */
open class NuovoTriennaleStudyPlan : StudyPlan() {
    // 'final override' = blocchiamo qui. Nessun'altra sottoclasse può cambiarlo
    final override val maxcfu = 200    // BLOCCATO: nessuno può fare override oltre questo

    /*
     * CASO D'USO: Quando vogliamo dire "questo valore è definitivo"
     * Ad esempio, se una legge dice che la nuova laurea triennale
     * DEVE avere 200 CFU, usiamo 'final' per assicurarci che
     * nessuno lo possa cambiare ulteriormente.
     */
}

// ============================================================================
// PARTE 3: OPERATOR OVERLOADING - DAI SIGNIFICATO AI SIMBOLI
// ============================================================================

/*
 * OPERATOR OVERLOADING = Ridifinire il comportamento degli operatori (+, -, *, etc)
 * per le tue classi custom.
 *
 * PERCHÉ? Perché il simbolo + ha più senso della funzione plus() per il lettore.
 *
 * SIMBOLI SOVRACCARICABILI:
 * - Aritmetici: +  -  *  /  %
 * - Comparazione: ==  !=  <  >  <=  >=
 * - Accesso: []  (per array e map)
 * - Assegnazione: +=  -=  *=  /=
 * - Unari: +x  -x  !x
 * - Incremento: ++x  x++  --x  x--
 *
 * ⚠️ IMPORTANTE: Non usi mai l'operator overloading tramite ereditarietà!
 * L'operator overloading NON è polimorfo.
 * Se fai una sottoclasse che sovraccarica un operatore,
 * questo funziona solo se il riferimento è della sottoclasse, non della superclasse.
 */

/**
 * Classe Credits - Rappresenta crediti formativi
 *
 * VOGLIAMO: Poter sommare credits usando l'operatore +
 * INVECE DI: credits1.plus(credits2)
 * POSSIAMO SCRIVERE: credits1 + credits2
 */
class Credits(val value: Int) {

    /**
     * Sovraccarica l'operatore '+' per la somma di Credits
     *
     * SYNTAX: operator fun [OPERATORE]([PARAMETRI]): [TIPO_RITORNO]
     *
     * Quando il compilatore vede:
     *     c1 + c2
     *
     * Traduce a:
     *     c1.plus(c2)
     */
    operator fun plus(other: Credits): Credits {
        return Credits(value + other.value)
    }

    /**
     * Bonus: Possiamo sovraccaricare altri operatori per la stessa classe
     */
    operator fun minus(other: Credits): Credits {
        return Credits((value - other.value).coerceAtLeast(0))  // Non può essere negativo
    }

    /**
     * Comparazione: possiamo controllare se due Credits sono uguali
     */
    operator fun compareTo(other: Credits): Int {
        return value.compareTo(other.value)
    }
}

/**
 * Classe Libro - Rappresenta un libro in una biblioteca
 */
class Libro(val titolo: String)

/**
 * Classe Biblioteca - Collezione di libri
 *
 * VOGLIAMO: Accedere ai libri usando la sintassi array: biblioteca[0]
 * INVECE DI: biblioteca.getLibro(0)
 */
class Biblioteca(val libri: List<Libro>) {

    /**
     * Sovraccarica l'operatore [] per accedere ai libri per indice
     *
     * SYNTAX: operator fun get(index: Type): ReturnType
     *
     * Quando il compilatore vede:
     *     bib[1]
     *
     * Traduce a:
     *     bib.get(1)
     */
    operator fun get(index: Int): Libro {
        return libri[index]
    }

    /**
     * Bonus: Possiamo anche fare SET con []
     * Sovraccarica l'operatore []= per modificare i libri
     */
    operator fun set(index: Int, libro: Libro) {
        // In realtà List è immutabile, quindi qui faremmo un errore.
        // Ma dimostriamo la syntax:
        // libri[index] = libro  <-- sarebbe la sintassi di utilizzo
    }
}

// ============================================================================
// PARTE 4: EXTENSION FUNCTIONS - AGGIUNGERE METODI SENZA EREDITARIETÀ
// ============================================================================

/*
 * EXTENSION FUNCTIONS = Aggiungere nuovi metodi a classi ESISTENTI
 * senza doverle modificare o estendere.
 *
 * SYNTAX: fun [CLASSE_ESISTENTE].[NOME_FUNZIONE]([PARAMETRI]): [RITORNO] { }
 *
 * VANTAGGI:
 * - Non modifichi il codice originale
 * - Non crei sottoclassi inutili
 * - Puoi aggiungere metodi a classi che non possiedi
 * - Codice più leggibile e organizzato
 *
 * ⚠️ ATTENZIONE - LIMITAZIONI IMPORTANTI:
 * 1. Le extension functions NON sono polimorfiche!
 *    Se chiami una extension su un riferimento di superclasse,
 *    non ottieni il comportamento della sottoclasse.
 * 2. Non possono accedere a membri private della classe estesa.
 * 3. Se la classe ha un metodo con lo stesso nome, la classe "vince".
 *
 * CASO D'USO: Quando vuoi aggiungere utilità senza modificare il design.
 * Ad esempio, aggiungere un metodo hasPassed() a Credits.
 */

/**
 * Extension function: Verifica se il numero di crediti è sufficiente
 * Estende la classe Credits aggiungendo il metodo hasPassed()
 */
fun Credits.hasPassed(): Boolean {
    return value >= 6  // 6 è il minimo per passare (dipende dall'università)
}

/**
 * Extension function: Formatta i crediti in modo leggibile
 */
fun Credits.format(): String {
    return "$value CFU"
}

/**
 * Extension function: Raddoppia i crediti
 */
fun Credits.doubled(): Credits {
    return Credits(value * 2)
}

// ============================================================================
// PARTE 5: DIMOSTRAZIONE PRATICA - MAIN
// ============================================================================

fun main() {

    println("=" * 80)
    println("PARTE 1: PROPRIETÀ CON GETTER E SETTER CUSTOM")
    println("=" * 80)

    /*
     * COSA ACCADE:
     * 1. Creiamo una PositiveAttitude con valore 3
     * 2. Nel constructor, attitude = 3 chiama il setter
     * 3. Il setter stampa "set(3)" e salva 3 in field
     */
    val p = PositiveAttitude(3)
    println("Initial attitude: ${p.attitude}")  // Chiama il getter, stampa "get"

    /*
     * Proviamo ad assegnare un valore NEGATIVO
     * Il setter intercetta e lo converte in 0
     */
    println("\nAssigniamo -10 (valore negativo)...")
    p.attitude = -10  // Chiama il setter con value = -10
    println("Dopo assegnazione, attitude è: ${p.attitude}")
    // Il setter ha convertito -10 in 0, quindi ottieniamo 0

    println()
    println("=" * 80)
    println("PARTE 2: CLASSI OPEN E OVERRIDE DI PROPRIETÀ")
    println("=" * 80)

    // Istanziamo la classe base (open, quindi è possibile)
    val sp = StudyPlan()
    println("StudyPlan generico - maxcfu: ${sp.maxcfu}")

    // Istanziamo le sottoclassi che fanno override
    val spt = TriennaleStudyPlan()
    val spm = MagistraleStudyPlan()
    val nspt = NuovoTriennaleStudyPlan()

    println("\nPlani di studio specializzati:")
    println("TriennaleStudyPlan - maxcfu: ${spt.maxcfu}")
    println("MagistraleStudyPlan - maxcfu: ${spm.maxcfu}")
    println("NuovoTriennaleStudyPlan - maxcfu: ${nspt.maxcfu} (FINAL - non può essere sovrascritto)")

    println()
    println("=" * 80)
    println("PARTE 3: OPERATOR OVERLOADING - CREAZIONE DI LINGUAGGIO NATURALE")
    println("=" * 80)

    // Creiamo crediti
    val c1 = Credits(6)
    val c2 = Credits(12)

    println("Credito 1: ${c1.format()}")
    println("Credito 2: ${c2.format()}")

    // Usiamo l'operatore '+' che abbiamo sovraccaricato
    println("\nUSO DELL'OPERATOR OVERLOADING:")
    println("c1 + c2 = ${(c1 + c2).format()}")  // Che eleganza!

    // Usiamo anche il meno (anch'esso sovraccaricato)
    println("c2 - c1 = ${(c2 - c1).format()}")

    // Comparazione
    println("\nc1 < c2? ${c1 < c2}")
    println("c1 == c1? ${c1 == c1}")  // NOTA: questo controlla il reference, non il valore

    println()
    println("=" * 80)
    println("PARTE 4: OPERATOR [] PER ACCEDERE COME ARRAY")
    println("=" * 80)

    val bib = Biblioteca(
        listOf(
            Libro("Il Signore degli Anelli"),
            Libro("Harry Potter"),
            Libro("1984")
        )
    )

    println("Biblioteca con 3 libri:")
    println("bib[0] = ${bib[0].titolo}")  // Uso di operator[]
    println("bib[1] = ${bib[1].titolo}")
    println("bib[2] = ${bib[2].titolo}")

    println()
    println("=" * 80)
    println("PARTE 5: EXTENSION FUNCTIONS - AGGIUNGERE FUNZIONALITÀ")
    println("=" * 80)

    // Usiamo le extension function che abbiamo definito
    val credito = Credits(8)

    println("\nCredito: ${credito.format()}")
    println("Hai passato l'esame? ${credito.hasPassed()}")
    println("Credito raddoppiato: ${credito.doubled().format()}")

    println()
    println("=" * 80)
    println("COMBINAZIONE: OPERATOR OVERLOADING + EXTENSION FUNCTIONS")
    println("=" * 80)

    val c3 = Credits(3)
    val c4 = Credits(4)
    val c5 = Credits(5)

    // Combiniamo tutto: operatori + extension functions
    val totale = c3 + c4 + c5
    println("Crediti totali: $c3 + $c4 + $c5 = ${totale.format()}")
    println("Sono sufficienti? ${totale.hasPassed()}")

    println()
    println("=" * 80)
    println("RIEPILOGO CONCETTI CHIAVE")
    println("=" * 80)
    println("""
        
        ✓ PROPRIETÀ CUSTOM:
          - Getter: codice che si esegue quando LEGGIAMO la proprietà
          - Setter: codice che si esegue quando ASSEGNIAMO alla proprietà
          - 'field' è la keyword per accedere al valore reale
        
        ✓ OPEN vs ABSTRACT:
          - open: classe estendibile ma istanziabile
          - abstract: classe non istanziabile, deve essere estesa
          - open su proprietà: consente override nelle sottoclassi
          - final override: blocca ulteriori override
        
        ✓ OPERATOR OVERLOADING:
          - Ridefinisce il significato degli operatori (+, -, [], etc)
          - NON è polimorfo: dipende dal tipo concreto
          - Rende il codice più leggibile (es: c1 + c2 vs c1.plus(c2))
        
        ✓ EXTENSION FUNCTIONS:
          - Aggiungono metodi a classi esistenti senza modificarle
          - NON sono polimorfiche (il tipo statico determina quale extension)
          - Non hanno accesso ai privati della classe estesa
          - Utili per aggiungere utilità senza cambiare il design
    """.trimIndent())
}

// HELPER per stampa decorativa
private operator fun String.times(count: Int) = this.repeat(count)
