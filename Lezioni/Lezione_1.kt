const val NOME = "Mario" //Costante in compile-time che deve essre globale. (standard nome uppercase)


fun main(args: Array<String>){
    println("Hello, world!") //notare che ; non è obbligatoria

    /*                   VARIABILI                      */
    var mutabile : Int
    mutabile = 5
    mutabile = 6

    val immutabile: Int
    immutabile = 2
    //immutabile = 3 Non posso farlo, le variabili inizializzate con val sono immutabili dopo la prima assegnazione
    //Solo il puntatore è immutabile per esempio se assegno un oggetto mutabile ad una variabile val, posso mutare
    //l'oggetto internamente ma non cambiare di puntatore alla variabile.

    /*          NB         */
    //val è consiglato per ragionio di sicurezza e buona pratica di codice in Kotlin.
    //val è anche considerata come una costante in runtime

    /*                    NULL SAFETY                           */

    //le variabili dichiarate sopra NON possono assumere valore NULL, uesto per scelta di design e sicurezza del
    //linguaggio Kotlin

    val nullableText: String? = "ciao" //con l'operatore ? indico che uella variabile potrebbe assumere un valore NULL

    //per controllare se un valore è null (SAFE CALL) invece di utilizzare if possibilmente annidati, kotlin fornisce
    //l'operatore ?. che funge propio da if NULL...

    nullableText?.get(0) //se nullableText non è null prendo il carattere in pos 0

    //mentre ?. funge da if, l'operatore ?: funge da if-else

    nullableText?.get(1) ?: println("VARIABILE NULL!") //se nullableText non è null prendo iil carattere in pos 1
                                                       //altrimenti stampo VARIABILE NULL
    //oppure possiamo usufruire dell'operatore let per effettuare un azione in caso di variabile non NULL

    nullableText?.let { println(it) }

}

/*               FUNZIONI           */


//in Kotlin possiamo usufruire di funzione single expression

fun sum(a: Int, b: Int): Int = a + b //utilizziamo l'operatore di assegnazione per queste semplici funzioni

fun funzione(a:Int, b:Int, cose:Array<String>): Unit {  //la keyword unit è come il void

    //lo switch case è rimpiazzato da when
    //NB!!! WHEN E UN ESPRESSIONE QUINDI HA UN VALORE DI RITORNO!!!
    when(a){
        1 -> println("a == 1")
        2 -> println("a == 2")
        else -> println("a non è ne 2 ne 1")
    }

    //come in python possiamo usare i seguenti opearatori:
    //in ==> per verificare se un oggetto si trova in un collezionabile
    //is ==> per controllare il tipo di un oggetto

    //range posso usare l'operatore .. per definire il range
    for (i in 1..5) {
        println(i)
    }



}
