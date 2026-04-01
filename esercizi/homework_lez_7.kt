//HOMEWORK - Event Log Analyzer
//Dato il seguente log di eventi, scrivi un programma kotlin che estragga informazione strutturata e stampi un piccolo report finale
val events = listOf(
    "LOGIN:alice",
    "LOGIN:bob",
    "LOGOUT:alice",
    "LOGIN:alice",
    "ERROR:db",
    "LOGIN:carol",
    "ERROR:net",
    "LOGIN:bob"
)
/*
1 Estrai tutti gli username coinvolti nei LOGIN in una List<String>.
2 Costruisci il Set<String> degli utenti che hanno effettuato almeno un login.
3 Costruisci una Map<String, Int> con il numero di login per utente.
4 Costruisci il Set<String> dei tipi di evento distinti.
5 Produci tutte le finestre di 3 eventi consecutivi usando windowed.
6 Mostra una piccola pipeline lazy con asSequence() che tenga solo i LOGIN, estragga l’username
e prenda i primi 2 risultati.
7 Stampa un report finale leggibile.
*/
fun main(){

    val loginsList = mutableListOf<String>() //punto 1
    val loginsSet = mutableSetOf<String>() //punto 2
    val eventsSet = mutableSetOf<String>() //punto 4

    for (event in events){
        //println(event.split(':'))
        eventsSet.add(event.split(':')[0])

        if("db" !in event.split(':') && "net" !in event.split(':') && "LOGOUT" !in event.split(':')){
            loginsSet.add(event.split(':')[1])
            loginsList.add(event.split(':')[1])
        }
    }

    println(loginsList)
    println(loginsSet)
    println(eventsSet)

    val loginsMap = mutableMapOf<String, Int>() //punto 3
    for(login in events){
        if("LOGIN" in login.split(':')){
            loginsMap[login.split(':')[1]] = loginsMap.getOrDefault(login.split(':')[1],
                0) + 1
        }
    }

    println(loginsMap)

    for(event in events.windowed(3)) println(event) //punto 5

    /*val eventSeq = sequenceOf(events)
        .filter {"LOGIN" in it}
        .take(2)
    */
    val eventSeq = events.asSequence() //punto 6
        .filter {"LOGIN" in it}
        .map {it.split(':')[1]}
        .take(2)

    println(eventSeq.toList())

    /* punto 7 */

    var loginSum = 0
    for((nome, valore) in loginsMap){
        loginSum += valore
    }

    print("Ci sono stati $loginSum login effettuati da: ")
    for((name, value) in loginsMap){
        print("$name $value volte/a ")
    }

    val logoutMap = mutableMapOf<String, Int>()
    for(logout in events){
        if("LOGOUT" in logout){
            logoutMap[logout.split(':')[1]] = logoutMap.getOrDefault(logout.split(':')[1],
                0) + 1
        }
    }

    var logoutSum = 0
    for((nome, valore) in logoutMap){
        logoutSum += valore
    }

    println()
    print("Ci sono stati $logoutSum logout effettuati da: ")
    for((name, value) in logoutMap){
        print("$name $value volte/a ")
    }
    println()

    var errorCounter: Int = 0

    val errorMap = mutableMapOf<String, Int>()
    for(error in events){
        if("ERROR" in error){
            errorCounter++
            errorMap[error.split(':')[1]] = errorMap.getOrDefault(error.split(':')[1],
                0) + 1
        }
    }

    println()
    print("ci sono stati $errorCounter errori del tipo: ")
    for((error, value) in errorMap){
        print("$error $value volte/a ")
    }
}
