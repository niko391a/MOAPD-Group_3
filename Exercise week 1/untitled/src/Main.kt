

// 01.01
data class person(val name: String,
                  val age: Int,
                  val mother: person?
)
val god = person("god",10000000,null) // 01.03
val Eve = person("Eve",100,god)
val Margret = person("Margret",70,Eve)
val Louise = person("Louise",20,Margret)
val Conrad = person("Conrad",77,Eve)
val Isaac = person("Isaac",4,Louise)
val Cane = person("Cane",30,Margret)
val Able = person("Able",10,Louise)
val Millie = person("Millie",20,Margret)
val Sydney = person("Sydney",18,Louise)

// 01.02
val people: List<person> = listOf(
Eve,Margret,Louise,Cane,Able,Millie,Conrad,Isaac,Sydney
)

// 01.04
fun directdecendants(mother: person, List: List<person>): Sequence<person> = sequence{ // skal returnere en sequence for at bruge yield https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.sequences/-sequence-scope/yield-all.html
    val decendants = List.filter { it.mother == mother }// filter efter eves børm

    for (d in decendants){
        yield(d) // tag eves børn put dem i sequencen
        yieldAll(directdecendants(d,List)) // så tag alle kør funktion på hver barn, recursion nedad
    }
}
// 01.06
fun nimDecendants(mother: person, List: List<person>): Sequence<person> = sequence{
    val decendants = List.filter { it.mother == mother }

    for (d in decendants){
        yieldAll(Helper(d,List))
    }
}

fun Helper(mother: person, List: List<person>): Sequence<person> = sequence{
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
}