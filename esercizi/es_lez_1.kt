/*

    Combinazione di Concetti
    Crea una funzione calcolaDesconto(prezzo: Double?, percentuale: Int): Double che:

    Se il prezzo è null, usa 100.0 come default (con ?:)
    Se la percentuale è tra 1 e 10: applica lo sconto
    Se è tra 11 e 20: applica sconto doppio
    Se è > 20: applica uno sconto massimo del 50%
    Altrimenti: nessuno sconto


*/

fun calcolaDesconto(prezzo: Double?, percentuale: Int): Double {

    val prezzoEffettivo = prezzo ?: 100.0 //val visto che so che non cambierà, con elvis operator che defaulta a 100 se
                                          //prezzo == NULL
    val sconto: Double //val visto che so che non cambierà

    when(percentuale){

        in 1 ..10 -> sconto = ( prezzoEffettivo * percentuale) / 100  //controllo se il valore di percentuale sia
                                                                            //nell'itarabile creato da 1..10

        in 11..20 -> sconto =  ( prezzoEffettivo * (percentuale * 2) / 100) //controllo se il valore di percentuale sia
                                                                                  //nell'itarabile creato da 11..20

        in 21 .. 100 -> sconto =  ( ( prezzoEffettivo *  50)  / 100)  //controllo se il valore di percentuale sia
                                                                            //nell'itarabile creato da 21..100

        else -> sconto = 0.0 //altrimenti 0% di sconto

    }


    return prezzoEffettivo - sconto


}

/*

    Esercizio Pratico: Validatore di Email (Semplice)
    Crea una funzione isEmailValida(email: String?): Boolean che:

    Controlla se l'email è null (restituisce false)
    Verifica che contenga un @
    Verifica che contenga un .
    Che il . sia dopo l'@

*/

fun isEmailValids(email: String?): Boolean {

    email ?: return false

    if (email.contains("@") && email.contains(".") && (email.indexOf("@") < email.indexOf(".") ) ) {

        return true

    }
    else return false

}

/*

    Esercizio di Logica: Calcolatore Semplice
    Crea una funzione calcola(a: Int, b: Int, operazione: Char): Int? che:

    Accetta due numeri e un'operazione (+, -, *, /)
    Usa when per selezionare l'operazione
    Restituisce null se la divisione è per zero
    Restituisce il risultato altrimenti

*/

fun calcola(a: Int, b: Int, operazione: Char): Int? {

    when (operazione) {
        '+' -> return a + b
        '-' -> return a - b
        '*' -> return a * b
        '/' -> if (b == 0) return null else return a / b
        else -> return null
    }

}

fun main(){

    println(calcolaDesconto(10.0, 10))

    println(isEmailValids(null))

    println(calcola(2, 2, '.'))

}
