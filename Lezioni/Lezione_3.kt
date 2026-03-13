/**************************************+****OBJECT ORIENTED PROGRAMMING************************************************/

/*
 * DEFINIZIONE DI UNA CLASSE IN KOTLIN
 * 
 * A differenza di Java, in Kotlin:
 * 1. Non usiamo la keyword 'new' per creare istanze
 * 2. Il costruttore primario è definito direttamente nella dichiarazione della classe
 * 3. I parametri del costruttore prefissati con 'val' o 'var' diventano automaticamente proprietà della classe
 *    (non è necessario ridichiararli nel corpo della classe)
 * 4. 'val' rende la proprietà immutabile (read-only), proteggendo il campo senza bisogno di keywords come 'private'
 */
class Student(val name: String, val studentId: Int) {

    /*
     * INIZIALIZZAZIONE DI UNA PROPRIETÀ CON DIPENDENZE ESTERNE
     * 
     * La proprietà 'email' non può essere inizializzata direttamente nel costruttore
     * perché dipende da 'name' che riceve come parametro.
     * La dichiaramo qui senza un valore iniziale.
     */
    val email: String

    /*
     * PRIMO BLOCCO INIT - INIZIALIZZAZIONE PERSONALIZZATA
     * 
     * Il blocco init {} è chiamato SUBITO DOPO il costruttore primario.
     * È utile per:
     * - Inizializzare proprietà che dipendono da altri parametri
     * - Effettuare logica di inizializzazione complessa
     * - Preparare lo stato dell'oggetto
     * 
     * In questo caso, inizializziamo 'email' concatenando il nome con il dominio dell'università.
     * La sintassi ${} permette di inserire variabili all'interno di stringhe (string interpolation).
     */
    init {
        email = "$name@uniroma2.eu"
    }

    /*
     * SECONDO BLOCCO INIT - VALIDAZIONE E CONTROLLI
     * 
     * Possiamo avere MULTIPLE blocchi init in una classe.
     * Vengono eseguiti TUTTI nell'ordine in cui sono dichiarati.
     * 
     * La funzione 'require()' è usata per validare precondizioni:
     * - Se la condizione è false, lancia un'eccezione IllegalArgumentException
     * - Il secondo argomento (dopo le parentesi graffe) è una lambda che fornisce il messaggio di errore
     * - Questo pattern assicura che gli oggetti Student vengano creati solo in uno stato valido
     * 
     * In questo caso: il nome non può essere vuoto o contenere solo spazi
     */
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
    }

    /*
     * METODO DI ISTANZA
     * 
     * Un metodo che esegue un'azione specifica dell'oggetto Student.
     * Può accedere direttamente alle proprietà dell'istanza ($name, $studentId)
     * senza bisogno di 'this' (anche se è possibile usarlo).
     */
    fun present() {
        println("My name is $name and studentId is $studentId")
    }

}

fun main() {

    /*
     * CREAZIONE DI ISTANZE (OGGETTI)
     * 
     * Differenze rispetto a Java:
     * 1. Non si usa la keyword 'new'
     * 2. Chiamiamo direttamente Student(...) - il compilatore crea una nuova istanza
     * 3. Il costruttore primario e tutti i blocchi init {} vengono eseguiti automaticamente
     * 
     * Nota: Questa istanza viene creata correttamente perché il nome è valido.
     */
    val student = Student("MarioRossi", 123)
    
    /*
     * CREAZIONE DI ISTANZA CON DATI INVALIDI
     * 
     * Questa riga LANCIARÁ un'eccezione durante l'esecuzione:
     * - Il secondo blocco init contiene require(name.isNotBlank())
     * - Il nome è "  " (solo spazi), quindi isNotBlank() restituisce false
     * - Verrà lanciato: IllegalArgumentException: "Name must not be blank"
     * 
     * Questo è un esempio di FAIL-FAST: fallire il prima possibile durante la creazione
     * dell'oggetto per evitare stati invalidi.
     */
    val anotherStudent = Student("  ", 321)

    /*
     * STAMPA DI UN OGGETTO
     * 
     * Quando stampate un oggetto per nome, Kotlin (come Java) chiama il metodo toString().
     * Se non lo sovrascriviamo, mostra il formato: NomeClasse@IndirizzoMemoria
     * 
     * Esempio output: Student@7f690630
     */
    println(student)
    println(anotherStudent)

    /*
     * STRING INTERPOLATION PER ACCESSO ALLE PROPRIETÀ
     * 
     * La sintassi ${variabile} permette di:
     * - Accedere alle proprietà pubbliche dell'oggetto
     * - Inserire direttamente il loro valore nella stringa
     * - È più leggibile rispetto alla concatenazione
     * 
     * Output: Student ->  MarioRossi 123
     */
    println("Student ->  ${student.name} ${student.studentId}")
    
    /*
     * INVOCAZIONE DI UN METODO
     * 
     * Utilizziamo la sintassi punto (.) per chiamare i metodi dell'oggetto
     * Il metodo accede alle proprietà dell'istanza attraverso l'implicit 'this'
     */
    student.present() // Output: My name is MarioRossi and studentId is 123

    /*
     * ASSEGNAZIONE DI RIFERIMENTI
     * 
     * Importante: in Kotlin, le variabili sono RIFERIMENTI agli oggetti.
     * 'student3' e 'anotherStudent' puntano ALLO STESSO OGGETTO in memoria.
     * Se mutassimo una proprietà attraverso student3, cambierebbe anche in anotherStudent.
     * 
     * NB: 'anotherStudent' non è stata creata per via dell'eccezione nel costruttore,
     * quindi questa linea in realtà causerebbe un errore di compilazione.
     */
    val student3 = anotherStudent

    /*
     * OPERATORE == (UGUAGLIANZA DI CONTENUTO)
     * 
     * L'operatore == confronta il VALORE e il CONTENUTO degli oggetti.
     * Per classi data class, Kotlin implementa automaticamente questo confronto.
     * Per classi normali, il comportamento predefinito è simile a equals() di Java.
     * 
     * In questo caso: student != anotherStudent perché hanno proprietà diverse
     * Output: false (oppure un errore se anotherStudent non è stata creata)
     */
    println(student == anotherStudent)
    
    /*
     * OPERATORE === (UGUAGLIANZA DI IDENTITÀ/RIFERIMENTO)
     * 
     * L'operatore === confronta se due variabili puntano ALLO STESSO OGGETTO in memoria.
     * Controlla l'indirizzo di memoria, non il contenuto.
     * 
     * In questo caso:
     * - student3 === anotherStudent è TRUE perché entrambi puntano allo stesso oggetto
     * - student === anotherStudent è FALSE perché sono oggetti diversi in memoria diverse
     * 
     * Analogia:
     * == : "Questi due appartamenti hanno le stesse caratteristiche?"
     * === : "Questo è lo stesso appartamento?"
     */
    println(student3 == anotherStudent)   // TRUE: stesso contenuto (perché stesso oggetto)
    println(student3 === anotherStudent)  // TRUE: stessa locazione di memoria

    /*
     * ACCESSO ALLE PROPRIETÀ
     * 
     * Poiché email è una proprietà val pubblica (non abbiamo specificato 'private'),
     * possiamo accedervi da fuori della classe.
     * 
     * Esempio output: MarioRossi@uniroma2.eu
     */
    println(student.email)

}
