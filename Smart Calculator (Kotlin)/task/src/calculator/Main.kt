package calculator
import kotlin.math.pow
import kotlin.text.contains

data class MapOfVariables(val mapOfVariable: MutableMap<String, Int> = mutableMapOf())

fun main() {
    loopOfProcess()
}

fun loopOfProcess() {
    val map = MapOfVariables()
    while (true) {
        val input = readln().trim()
        val operation = evaluatorOfProcess(input)
        if (input.isBlank()) continue
        if (operation == 0) {
            if (checkerOfInvalidExpression(input) ){
                println("Invalid expression")
                continue
            } else if (!checkerOfVariable(input)) {
                println("Invalid identifier")
                continue
            } else if (!checkerOfValue(input)) {
                println("Invalid assignment")
                continue
            }
        }
        when (operation) {
            1 -> {
                when (input) {
                    "/exit" -> {
                        printerOfFarewell()
                        break
                    }

                    "/help" -> {
                        printerOfHelp()
                    }

                    else -> println("Unknown command")
                }
            }
            2 -> adderToMap(input = input.replace(" ", "").split("="), map = map)

            3 -> calculatorOfResult(input,map)

            4 -> showerOfVariable(input, map = map)
        }
    }
}


fun showerOfVariable(input: String, map: MapOfVariables) {
    if (input in map.mapOfVariable) {
        println(map.mapOfVariable[input])
    } else {
        println("Unknown variable")
    }
}

fun calculatorOfResult(operation: String, map: MapOfVariables) {
    val input = sanitizeOperators(operation)
    val operationToList = infixToPostFix(input)
    println(postfixToResult(operationToList, map))
}

fun evaluatorOfProcess(input: String): Int {
    val cleanInput = input.trim()
    if (cleanInput.isBlank()) return 0
    val regexForCommand = Regex("""^/[a-zA-Z]+$""")
    val regexToAddVariable = Regex("""^[a-zA-Z]+\s*=\s*([a-zA-Z]+|[0-9]+)$""")
    val regexForOperation = Regex("""^[a-zA-Z0-9\s+\-*/^()]*[+\-*/^()][a-zA-Z0-9\s+\-*/^()]*$""")
    val regexForShowingVariable = Regex("""^[a-zA-Z]+$""")

    return when {
        checkerOfInvalidExpression(cleanInput) -> 0
        regexForCommand.matches(cleanInput) -> 1
        regexToAddVariable.matches(cleanInput) -> 2
        regexForOperation.matches(cleanInput) -> 3
        regexForShowingVariable.matches(cleanInput) -> 4
        else -> 0
    }
}

fun changerOfValue(input: List<String>, map: MapOfVariables) {
    val key = input.first()
    val value = input.last()
    if (value in map.mapOfVariable) {
        map.mapOfVariable[key] = map.mapOfVariable[value]!!
    } else{
        map.mapOfVariable[key] = value.toInt()
    }

}

fun adderToMap(input: List<String>, map: MapOfVariables) {
    val key = input.first()
    val value = input.last()

    if (key in map.mapOfVariable) {
        changerOfValue(input, map)
    } else if (value in map.mapOfVariable) {
        map.mapOfVariable[key] = map.mapOfVariable[value] ?: 0
    } else {
        try {
            map.mapOfVariable[key] = value.trim().toInt()
        } catch (e: NumberFormatException) {
            println("Unknown variable")
        }
    }
}

fun checkerOfVariable(possibleVariable: String): Boolean {
    val dividerOfVariable = possibleVariable.split("=")
    val checker = Regex("[a-zA-Z]+")
    return dividerOfVariable.first().trim().matches(checker)
}

fun checkerOfValue(possibleVariable: String): Boolean {
    val dividerOfVariable = possibleVariable.split("=")
    val checker = Regex("""[0-9a-zA-Z|a-zA-Z]+""")
    return dividerOfVariable.size < 2 && dividerOfVariable.last().trim().matches(checker)
}
fun checkerOfInvalidExpression(input: String): Boolean {
    val cleanInput = input.trim()

    val openCount = cleanInput.count { it == '(' }
    val closeCount = cleanInput.count { it == ')' }
    if (openCount != closeCount) return true


    val invalidOperatorsRegex = Regex("""[*/^]{2,}|[*/^]\s*[*/^+]|[*/^]\s*-$""")
    if (invalidOperatorsRegex.containsMatchIn(cleanInput)) return true

    val endsWithOperatorRegex = Regex("""[+\-*/^]\s*$""")
    if (endsWithOperatorRegex.containsMatchIn(cleanInput)) return true

    val nonAsciiRegex = Regex(""".*[^\x00-\x7F].*""")
    if (nonAsciiRegex.matches(cleanInput)) return true

    return false
}

fun printerOfHelp() {
    println("The program calculates the sum of numbers")
}

fun printerOfFarewell() {
    println("Bye!")
}
fun sanitizeOperators(input: String): String {
    var result = input.trim()

    if (result.startsWith("+")) {
        result = result.removePrefix("+").trim()
    }

    while (result.contains("--")) {
        result = result.replace("--", "+")
    }
    result = result.replace(Regex("\\++"), "+")
    result = result.replace(Regex("\\+-|-\\+"), "-")
    result = result.replace(Regex("\\++"), "+")

    return result
}

fun infixToPostFix(operation: String): List<String> {
    val tokenRegex = Regex("""(?<=\(|^|[+\-*/^])\s*[+-]?\d+|[a-zA-Z]+|[+\-*/^()]""")
    val charOperants = tokenRegex.findAll(operation)
        .map { it.value.trim() }
        .filter { it.isNotEmpty() }
        .toList()

    val result: MutableList<String> = mutableListOf()
    val stack = ArrayDeque<String>()
    for (i in charOperants.indices) {
        if (charOperants[i] in listOfValidOperators()) {
            if (charOperants[i] == "(") {
                stack.addLast(charOperants[i])
            } else if (charOperants[i] == ")") {
                while (stack.last() != "("){
                    result.add(stack.last())
                    stack.removeLast()
                }
                stack.removeLast()
            } else {
                val newOperator = importantOfOperators(charOperants[i])

                while (!stack.isEmpty() && importantOfOperators(stack.last()) >= newOperator && stack.last() != "(") {
                    result.add(stack.last())
                    stack.removeLast()
                }
                stack.addLast(charOperants[i])
            }


        } else {
            result += charOperants[i]
        }
    }
    while (stack.isNotEmpty()){
        result.add(stack.last())
        stack.removeLast()
    }

    for (i in result.indices) {
        result[i] = result[i].trim()
    }
    return result
}

fun postfixToResult(charOperants: List<String>, map: MapOfVariables): Int {
    var result: Int
    val stack = ArrayDeque<Int>()
    for (i in charOperants.indices) {
        if (charOperants[i] in listOfValidOperators()) {
            val number2 = stack.last()
            stack.removeLast()
            val number1 = stack.last()
            stack.removeLast()
            when(charOperants[i]) {
                "^" -> {
                    result = number1.toDouble().pow(number2.toDouble()).toInt()
                    stack.add(result)
                }
                "*" -> {
                    result = number1 * number2
                    stack.add(result)
                }
                "/" -> {
                    result = number1 / number2
                    stack.add(result)
                }
                "+" -> {
                    result = number1 + number2
                    stack.add(result)
                }
                "-" -> {
                    result = number1 - number2
                    stack.add(result)
                }
            }
        } else {
            if (map.mapOfVariable.containsKey(charOperants[i])) {
                val value = map.mapOfVariable[charOperants[i]]
                stack.addLast(value?: 0)
            } else{
                stack.addLast(charOperants[i].toInt())
            }

        }
    }
    return stack.last()
}

fun importantOfOperators(sign: String): Int{
    when(sign){
        "^" -> return 3
        "/" -> return 2
        "*" -> return 2
        "+" -> return 1
        "-" -> return 1
    }
    return 0
}

fun listOfValidOperators() : List<String>{
    return listOf("^","/","*","+","-","(",")")
}