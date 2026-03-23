import kotlin.math.sqrt

/*
TRACCIA:
Crea una classe BankAccount che rappresenta un conto bancario con:
- Proprietà: accountHolder (String), accountNumber (Int), balance (Double)
- Il saldo NON può essere negativo
- Implementa blocchi init per:
  1. Validare che il nome del titolare non sia vuoto
  2. Validare che il numero di conto sia positivo
  3. Validare che il saldo iniziale sia >= 0
- Metodo: deposit(amount: Double) che aggiunge denaro
- Metodo: withdraw(amount: Double): Boolean che ritorna true se il prelievo è riuscito
*/
class bankAccount(
    val accountHolder: String,
    val accountNumber: Int,
    var balance: Double
){

    init {
        require(balance >= 0) { "balance can't be negative" }
    }

    init {
        require(accountNumber >= 0) { "accountNumber can't be negative" }
    }

    init {
        require(accountHolder.isNotEmpty()) { "accountHolder can't be empty" }
    }

    fun deposit(amount: Double) {
        balance += amount
        println("Deposited $amount, current balance is $balance")
    }

    fun withdraw(amount: Double): Boolean{
        if(balance >= amount){
          balance -= amount
          println("Withdrew $amount, current balance is $balance")
          return true
        } else {
            println("Not enough balance to withdraw $amount, current balance is $balance")
            return false
        }
    }

}

/*TRACCIA:
Crea un'interfaccia Vehicle con il metodo:
- fun accelerate()
- fun brake()
- fun getSpeed(): Int

Crea due classi che implementano Vehicle:
1. Car: velocità max 200 km/h, velocità aumenta di 20 al accelerate()
2. Bicycle: velocità max 40 km/h, velocità aumenta di 5 al accelerate()

Entrambi devono:
- Tenere traccia della velocità corrente (min 0)
- Quando brake() è chiamato, riduce la velocità di 10 (ma non sotto 0)
- getSpeed() ritorna la velocità corrente
*/

interface Vehicle{
    fun accelerate()
    fun brake()
    fun getterSpeed(): Int
}

class Car(
    var speed: Int
): Vehicle{
    override fun accelerate() {
        speed = minOf(speed + 20, 200)
        println("Auto accellera, velocità: $speed")
    }

    override fun brake() {
        speed = maxOf(speed - 10, 0)
        println("Auto decellera, velocità: $speed")
    }

    override fun getterSpeed(): Int = speed

}

class Bicycle(
    var speed: Int
): Vehicle{
    override fun accelerate() {
        speed = minOf(speed + 5, 40)
        println("Bicicletta accellera, velocità: $speed")
    }

    override fun brake() {
        speed = maxOf(speed - 10, 0)
        println("Bicicletta decellera, velocità: $speed")
    }

    override fun getterSpeed(): Int = speed

}

/*
TRACCIA:
Crea una classe astratta Employee con:
- Proprietà: name (String), salary (Double)
- Metodo astratto: calculateBonus(): Double
- Metodo concreto: printInfo() che stampa nome e stipendio

Crea due sottoclassi:
1. Manager: bonus = 20% dello stipendio
2. Developer: bonus = 15% dello stipendio
*/

abstract class Employee(
    val name: String,
    val salary: Double
) {
    abstract fun calculateBonus(): Double

    fun printInfo(){
        println("$name, $salary")
    }

}


class Manager(name: String, salary: Double): Employee(name, salary){
    override fun calculateBonus(): Double = (salary * 20) / 100
}

class Developer(name: String, salary: Double): Employee(name, salary){
    override fun calculateBonus(): Double = (salary * 15) / 100
}
/*
Crea una classe Temperature che rappresenta una temperatura:
- Memorizza la temperatura in Celsius (con backing field)
- Implementa un getter custom che permette di accedere in Fahrenheit
- Implementa un setter custom che converte da Fahrenheit a Celsius
- Se la temperatura scende sotto -273.15°C (zero assoluto), deve essere 0 K

Formula: C = (F - 32) * 5/9  |  F = C * 9/5 + 32
*/

class Temperature(
    var temperatura: Double
) {
    var celsius: Double = temperatura //getter e setter per celsius
        get(){
            return field
        }
    set(value) {
        field = if (value < -273.15) 0.0 else value
    }

    var fahrenheit: Double
        get(){
            return (celsius * ( 9.0/5.0)) + 32
        }
        set(value) {
            celsius = (value - 32) / (5.0/9.0)
        }

}

/*
TRACCIA:
Crea una classe Vector2D che rappresenta un vettore 2D con:
- Proprietà: x (Double), y (Double)
- Sovraccarica l'operatore + per sommare due vettori
- Sovraccarica l'operatore - per sottrarre due vettori
- Sovraccarica l'operatore * per moltiplicare per uno scalare
- Implementa compareTo() per confrontare l'ampiezza dei vettori
*/

class Vector2D(
    val x: Double,
    val y: Double
) {
    operator fun plus(other: Vector2D): Vector2D{
        return Vector2D(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2D): Vector2D{
        return Vector2D(x - other.x, y - other.y)
    }

    operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)

    fun compareTo(other: Vector2D): Unit{
        val v1 = sqrt( (x*x) + (y*y) )
        val v2 = sqrt(  (other.x * other.x) + (other.y * other.y ) )

        when{
            v1 == v2 -> println("Ampiezza uguale")
            v1 < v2  -> println("Primo vettore minore del secondo")
            v2 < v1  -> println("Primo vettore maggiore del secondo")
        }

    }

}

/*
TRACCIA:
Aggiungi extension functions alla classe String:
1. fun String.isPalindrome(): Boolean
- Controlla se la stringa è un palindromo (ignora maiuscole/minuscole)

2. fun String.countVowels(): Int
- Conta il numero di vocali nella stringa

3. fun String.reverseWord(): String
- Ritorna la stringa invertita
*/

fun String.isPalindrome(): Boolean {
    val cleaned = this.lowercase().filter { it.isLetterOrDigit() }
    return cleaned == cleaned.reversed()
}

fun String.countVowels(): Int{
    var counter: Int = 0
    for(c in this){
        if(c.lowercaseChar() in listOf<Char>('a','e','i','o','u')){
            counter++
        }
    }

    return counter

}

fun String.reverseWord(): String{
    return this.reversed()
}

/*
TRACCIA:
Crea un'interfaccia Shape con:
- fun area(): Double
- fun perimeter(): Double

Implementala in almeno 2 classi:
1. Circle (raggio)
2. Rectangle (lunghezza e larghezza)

Crea una funzione che accetta una List<Shape> e:
- Stampa l'area e il perimetro di ogni forma
- Ritorna la forma con l'area maggiore
*/

interface Shape{
    fun area(): Double
    fun perimeter(): Double

}

class Circle(
    var radius: Double
): Shape{
    override fun area(): Double = (radius * radius) * 3.14
    override fun perimeter(): Double = (3.14 * radius) * 2
}

class Rectangle(
    var width: Double,
    var height: Double
): Shape{
    override fun area(): Double = width * height
    override fun perimeter(): Double = 2 * height + 2 * width
}

fun largestShape(forme: List<Shape>): Shape{
    var tmp: Shape = forme[0]
    for(shape in forme){
        println("${shape.perimeter()}, ${shape.area()}")
        if(shape.area() >= tmp.area()){
            tmp = shape
        }
    }

    return tmp

}

/*
TRACCIA:
Crea una classe BankAccount2 (diversa da quella dell'esercizio 1) con:
- Private: transactionHistory (List<String>)
- Protected: logTransaction(message: String) - aggiunge alla history
- Public: deposit() e withdraw() che DEVONO usare logTransaction()

La transactionHistory non deve essere modificabile dall'esterno.
Fornisci un metodo getTransactionHistory() che ritorna una COPIA della lista.
*/

class BankAccount2(
    val accountHolder: String,
    val accountNumber: Int,
    var balance: Double,
) {

    private val transactionHistory: MutableList<String> = mutableListOf()

    protected fun logTransaction(message: String){
        transactionHistory.add(message)
    }

    fun deposit(amount: Double) {
        balance += amount
        logTransaction("Deposited $amount, current balance is $balance")
    }

    fun withdraw(amount: Double): Boolean{
        if(balance >= amount){
            balance -= amount
            logTransaction("Withdrew $amount, current balance is $balance")
            return true
        } else {
            logTransaction("Not enough balance to withdraw $amount, current balance is $balance")
            return false
        }
    }

    fun getTransactionHistory():List<String>{
        val tmp = transactionHistory.toList()
        return tmp
    }

}


fun main() {

    println("ESERCIZIO 1")
    val bankAccount = bankAccount("Mario", 1, 0.00);
    bankAccount.deposit(10.0)
    bankAccount.withdraw(2.0)

    println("ESERCIZIO 2")
    val bicycle = Bicycle(0)
    val car = Car(0)
    bicycle.accelerate()
    bicycle.brake()
    bicycle.accelerate()
    car.accelerate()
    car.accelerate()
    car.brake()
    println("velocità corrente bici: ${bicycle.getterSpeed()}")
    println("velocità corrente auto: ${car.getterSpeed()}")

    println("ESERCIZIO 3")
    val manager = Manager("Mario", 1000.0)
    val developer = Developer(name = "Piero", salary = 1000.0)
    println(manager.calculateBonus())
    manager.printInfo()
    println(developer.calculateBonus())
    developer.printInfo()

    println("Esercizio 4")
    val temperature = Temperature(20.0)
    temperature.celsius = 100.0
    println(temperature.fahrenheit)

    println("Esercizio 5")
    val vet1 = Vector2D(2.0 , 4.0)
    val vet2 = Vector2D(3.0, 4.0)
    vet1.compareTo(vet2)
    val vet3 = vet1 + vet2 //operatore overloading
    println("${vet3.x}, ${vet3.y}")



    println("Esercizio 6")
    val testString = "hello"
    println("'$testString' è palindromo? ${testString.isPalindrome()}")
    println("Vocali in '$testString': ${testString.countVowels()}")
    println("Invertito: ${testString.reverseWord()}")
    val palindrome = "racecar"
    println("'$palindrome' è palindromo? ${palindrome.isPalindrome()}")
    println("Vocali in '$palindrome': ${palindrome.countVowels()}")

    println("Esercizio 7")
    val shapes: List<Shape> = listOf(
        Circle(5.0),
        Rectangle(4.0, 6.0),
        Circle(3.0),
        Rectangle(2.0, 8.0)
    )
    for (shape in shapes) {
        println("$shape - Area: ${String.format("%.2f", shape.area())}, Perimeter: ${String.format("%.2f", shape.perimeter())}")
    }
    val largest = largestShape(shapes)
    println("Forma con area maggiore: $largest (Area: ${String.format("%.2f", largest.area())})")

    println("Esercizio 8")
    val bank = BankAccount2("John Doe", 1234, 1000.0)
    bank.deposit(500.0)
    bank.withdraw(200.0)
    bank.withdraw(100.0)

    println("Transaction History:")
    for (transaction in bank.getTransactionHistory()) {
        println("  - $transaction")
    }


}
