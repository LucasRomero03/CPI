/*
 * =====================================================================
 *  GROOVY COLLECTIONS – EXEMPLOS PRÁTICOS (MAP, LIST E MÉTODOS ÚTEIS)
 * =====================================================================
 *
 * Este script NÃO depende de CPI. É apenas referência de estudo.
 *
 * Inclui:
 *   ✔ each com List
 *   ✔ each com Map (key, value)
 *   ✔ find, findAll
 *   ✔ collect (transformação)
 *   ✔ sum (redução)
 *   ✔ any / every (match)
 *   ✔ groupBy
 *   ✔ sort
 *
 * =====================================================================
 */

println "=============================="
println "LIST + EACH"
println "=============================="

def lista = [1, 2, 3, 4]

/*
 * each em List
 * Executa a closure para cada elemento.
 */
lista.each { numero ->
    println "Número: ${numero}"
}

println "\n=============================="
println "MAP + EACH (CHAVE / VALOR)"
println "=============================="

/*
 * Um Map JSON-like
 */
def mapa = [
    nome: "Lucas",
    idade: 22,
    curso: "Engenharia"
]

/*
 * each em Map recebe dois parâmetros:
 *   key  -> chave do Map
 *   value -> valor da chave
 */
mapa.each { key, value ->
    println "${key} = ${value}"
}

println "\n=============================="
println "find (pega o primeiro que combina)"
println "=============================="

/*
 * find -> retorna o PRIMEIRO elemento que satisfaz a condição
 */
def primeiraPar = lista.find { n -> n % 2 == 0 }
println "Primeiro número par = ${primeiraPar}"

println "\n=============================="
println "findAll (filtra vários)"
println "=============================="

def apenasPares = lista.findAll { n -> n % 2 == 0 }
println "Todos pares = ${apenasPares}"

println "\n=============================="
println "collect (mapear valores)"
println "=============================="

/*
 * collect transforma cada elemento da lista em outro valor
 * (equivalente ao map() das linguagens funcionais)
 */
def quadrados = lista.collect { n -> n * n }
println "Quadrados = ${quadrados}"

println "\n=============================="
println "sum (redução)"
println "=============================="

def soma = lista.sum()
println "Soma = ${soma}"

println "\n=============================="
println "any / every"
println "=============================="

/*
 * any -> retorna true se ALGUM elemento satisfaz
 * every -> retorna true se TODOS satisfazem
 */

println "Algum número maior que 3?  ${ lista.any { it > 3 } }"
println "Todos são positivos?       ${ lista.every { it > 0 } }"

println "\n=============================="
println "groupBy (criar grupos)"
println "=============================="

def pessoas = [
    [nome: "Ana",   idade: 20],
    [nome: "Lucas", idade: 22],
    [nome: "João",  idade: 20],
    [nome: "Maria", idade: 22]
]

def agrupado = pessoas.groupBy { p -> p.idade }
println agrupado
/*
 * Saída:
 *  [
 *    20: [[Ana, 20], [João, 20]],
 *    22: [[Lucas, 22], [Maria, 22]]
 *  ]
 */

println "\n=============================="
println "sort (ordenar)"
println "=============================="

def naoOrdenado = [5, 1, 9, 2]
println "Ordenado = ${ naoOrdenado.sort() }"

/*
 * Também dá pra ordenar Map por campo
 */
def ordenadoPorIdade = pessoas.sort { p -> p.idade }
println "Pessoas ordenadas por idade = ${ordenadoPorIdade}"
