

// 01.01
data class Person(val name: String,
                  val age: Int,
                  val mother: Person?
)



val god = Person("god",10000000,null) // 01.03
val Eve = Person("Eve",100,god)
val Margret = Person("Margret",70,Eve)
val Louise = Person("Louise",20,Margret)
val Conrad = Person("Conrad",77,Eve)
val Isaac = Person("Isaac",4,Louise)
val Cane = Person("Cane",30,Margret)
val Able = Person("Able",10,Louise)
val Millie = Person("Millie",20,Margret)
val Sydney = Person("Sydney",18,Louise)

// 01.06
fun Person.imediated(): Boolean {
    return mother == Eve
}




// 01.02
val people: List<Person> = listOf(
Eve,Margret,Louise,Cane,Able,Millie,Conrad,Isaac,Sydney
)


// 01.04
fun directdecendants(mother: Person, List: List<Person>): Sequence<Person> = sequence{ // skal returnere en sequence for at bruge yield https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.sequences/-sequence-scope/yield-all.html
    val decendants = List.filter { it.mother == mother }// filter efter eves børm

    for (d in decendants){
        yield(d) // tag eves børn put dem i sequencen
        yieldAll(directdecendants(d,List)) // så tag alle kør funktion på hver barn, recursion nedad
    }
}
// 01.05
fun nimDecendants(mother: Person, List: List<Person>): Sequence<Person> = sequence{
    val decendants = List.filter { it.mother == mother }

    for (d in decendants){
        yieldAll(Helper(d,List))
    }
}

fun Helper(mother: Person, List: List<Person>): Sequence<Person> = sequence{
    val decendants = List.filter { it.mother == mother }
    for (d in decendants){
        yield(d)
        yieldAll(Helper(d,List))
    }

}


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    println(nimDecendants(Eve,people).toList()) // to list er vigtig for at konvertere fra sequence objekt til liste.
    println(Conrad.imediated())
}