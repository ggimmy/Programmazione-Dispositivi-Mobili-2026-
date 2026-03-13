/*
 Requisiti minimi
    ● All’avvio, leggi opzionalmente un argomento da linea di comando: se args non è vuoto, usalo come
    nome utente o prefisso (es. 'Ciao, <args[0]>').
    ● Struttura dati: mutableListOf<String>() per salvare le task.
    ● Menu in loop (while(true)) con when per scegliere l'azione.
    ● Azioni: 1) Add task 2) List tasks 3) Remove task by index 4) Stats 5) Quit.
    ● Input robusto: readLine() nullable; parsing numerico con toIntOrNull(); usare Elvis e/o continue/return
    per gestire input invalido senza crash.
    ● Output con string templates ($ e ${}).
    ● Remove sicuro: controlla idx in tasks.indices (usa 'in' + range).
    ● Stats: usa almeno una lambda e fold (es. somma delle lunghezze delle task).
    ● Organizza il codice in almeno 2 funzioni oltre main (es. printMenu(), addTask(...), removeTask(...)).
    ● Opzionale ma consigliato: una funzione bonus non implementata con TODO("...") (es. export).
    Bonus (facoltativi)
    ● Search case-insensitive: chiedi parola e stampa task che la contengono.
    ● Validazione task: blocca stringhe vuote o solo spazi (trim).
    ● Conferma remove: chiedi 'y/n' prima di cancellare.
*/

import kotlin.system.exitProcess

fun addTask(tasks: MutableList<String>): Unit{

    print("Digitare nuova task: ")
    val newTask = readln().lowercase()
    if (newTask.trim().isEmpty()) { println("Nome task vuoto"); return}
    tasks.add(newTask)

    return

}

fun listTasks(tasks: MutableList<String>): Unit{

    for( (index, element) in tasks.withIndex() ){
        println("$index: $element")
    }

    return

}

fun removeTask(tasks: MutableList<String>): Unit {

    print("Digitare numero task da cancellare: ")
    val taskNumber = readln().toIntOrNull() ?: return

    if (taskNumber in tasks.indices){

        println("Sei sicuro vuoi cancellare ${tasks[taskNumber]}? y/n")
        val flag = readln()

        when (flag) {
            "y" -> tasks.removeAt(taskNumber)
            else -> return
        }


    } else {

        println("Numero task non valido")

    }

    return

}

fun searchTask(tasks: MutableList<String>): Unit{

    print("Digitare nome task: ")
    val task = readln().lowercase()

    val results = tasks.filterIndexed { idx, t -> t.contains(task) }
    if (results.isNotEmpty()) {
        results.forEach { t -> println("${tasks.indexOf(t)}: $t") }
    } else {
        println("Task non trovata")
    }

}

fun export(tasks: MutableList<String>): Nothing = TODO("Da implementare")

fun main(args: Array<String>) {

    if (args.isNotEmpty()) {println("ciao ${args[0]}!")}

    val tasks = mutableListOf<String>();


    while(true){
        print("Inserire comando (1 add task, 2 list task, 3 remove task, 4 export, 5 stats, 6 quit, 7 cerca) ")
        val cmd = readln().toIntOrNull() ?: continue
        when(cmd){
            1 -> addTask(tasks)
            2 -> listTasks(tasks)
            3 -> removeTask(tasks)
            4 -> export(tasks)
            5 -> println("Lunghezza totale tasks ${(tasks.fold(0) {acc, task -> acc + task.length})}") //somma lunghezza elementi tasks per esercizio"
            6 -> exitProcess(0)
            7 -> searchTask(tasks)
            else -> println("Invalid command")
        }

    }

}
