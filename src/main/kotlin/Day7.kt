import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import java.nio.file.Files

//fun main() {
//    val graph = Graph()
//    Files.readAllLines(fromResource("day6Test"))
//        .map {
//            "^([\\w\\s]+?) bags contain (.+).$".toRegex().find(it)!!
//        }
//        .forEach { it ->
//            val source = it.groupValues[1]
//            val s = it.groupValues[2]
//            println(source)
//            if (s == "no other bags") {
//                graph.addVertex(source)
//            } else {
//                val dests = s
//                    .split(",")
//                    .map {
//                        "(\\d+) ([\\w ]+?) bags?".toRegex().find(it)!!
//                    }
//                    .map {
//                        Integer.valueOf(it.groupValues[1]) to it.groupValues[2]
//                    }
//
//                graph.addVertex(source)
//                dests.forEach { (value, destLabel) ->
//                    graph.addVertex(destLabel)
//                    graph.addEdge(source, destLabel, value)
//                }
//            }
//        }
//
//    graph.edges.forEach { edge ->
//
//    }
//}
//
//data class Graph(
//    val vertexes: MutableSet<String> = mutableSetOf(),
//    val edges: MutableSet<Edge> = mutableSetOf()
//) {
//    fun addVertex(vertex: String) {
//        vertexes.add(vertex)
//    }
//
//    fun addEdge(source: String, target: String, value: Int) {
//        if (!vertexes.contains(source)) {
//            throw IllegalArgumentException("Unknown vertex: $source")
//        }
//        if (!vertexes.contains(target)) {
//            throw IllegalArgumentException("Unknown vertex: $target")
//        }
//        edges.add(Edge(source, target, value))
//    }
//}
//
//data class Edge(
//    val source: String,
//    val target: String,
//    val weight: Int
//)

fun main() {
    val graph = DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
    Files.readAllLines(fromResource("day7"))
        .map {
            "^([\\w\\s]+?) bags contain (.+).$".toRegex().find(it)!!
        }
        .forEach { it ->
            val source = it.groupValues[1]
            val s = it.groupValues[2]
            if (s == "no other bags") {
                graph.addVertex(source)
            } else {
                val dests = s
                    .split(",")
                    .map {
                        "(\\d+) ([\\w ]+?) bags?".toRegex().find(it)!!
                    }
                    .map {
                        Integer.valueOf(it.groupValues[1]) to it.groupValues[2]
                    }

                graph.addVertex(source)
                dests.forEach { (value, destLabel) ->
                    graph.addVertex(destLabel)
                    graph.setEdgeWeight(graph.addEdge(source, destLabel), value.toDouble())
                }
            }
        }

    val dest = "shiny gold"

    println(graph.getWeight(dest))

//    graph.vertexSet()
//        .filter {
//            graph.outgoingEdgesOf(it).isEmpty()
//        }
//        .forEach {
//            DijkstraShortestPath<String, DefaultWeightedEdge>(graph).getPath(dest, it)
//                .edgeList
//                .also { print(it + ": ") }
//                .fold(1) { res, edge ->
//                    res * graph.getEdgeWeight(edge).toInt()
//                }
//                .also { println(it) }
//        }

//    graph.vertexSet()
//        .filterNot { it == dest }
//        .filter {
//            DijkstraShortestPath<String, DefaultWeightedEdge>(graph).getPath(it, dest) != null
//        }
//        .also { println(it) }
//        .also { println(it.count()) }
}

fun DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>.getWeight(source: String): Int =
    outgoingEdgesOf(source)
        .takeUnless { it.isEmpty() }
        ?.sumBy {
            (getEdgeWeight(it).toInt() * getWeight(getEdgeTarget(it)) + getEdgeWeight(it).toInt())
                .also { sum ->
                    println(it.toString() + " -> " + getEdgeWeight(it).toInt() + " * " + getWeight(getEdgeTarget(it)) + " + " + getEdgeWeight(it).toInt() + " = " + sum)
                }
        }
        ?: 0


/*
fun main() {
    Files.readAllLines(fromResource("day6"))
        .map {
            "^([\\w\\s]+?) bags contain (.+).$".toRegex().find(it)!!
        }
        .fold() { graph: Graph, it ->
            val source = it.groupValues[1]
            val s = it.groupValues[2]
            println(source)
            if (s == "no other bags") {
                graph.add(Node(source))
                graph
            } else {
                val dests = s
                    .split(",")
                    .map {
                        "(\\d+) ([\\w ]+?) bags?".toRegex().find(it)!!
                    }
                    .map {
                        Integer.valueOf(it.groupValues[1]) to it.groupValues[2]
                    }

                graph.editNode(source) { node ->
                    node.edges = dests.map { (value, destLabel) ->
                        Edge(
                            source = node,
                            dest = graph.findOrAdd(destLabel),
                            value = value
                        )
                    }
                }
            }
        }
//        .filter {
//            it.childrenNodes.any { it.label == "shiny gold" }
//        }
//        .also {
//            println(it.joinToString("\n"))
//        }
//        .also {
//            println(it.count())
//        }
}

data class Node(
    val label: String,
    var edges: List<Edge> = emptyList()
) {
    override fun toString(): String {
        return "Node { $label : ${edges.joinToString(", ") { it.dest.label }} }"
    }
    val childrenNodes: List<Node>
        get() = edges.flatMap { it.dest.childrenNodes + it.dest }
}

data class Edge(
    val source: Node,
    val dest: Node,
    val value: Int
)

typealias Graph = MutableSet<Node>

private fun Graph.editNode(label: String, block: (Node) -> Unit): Graph {
    findOrAdd(label).also(block)
    return this
}

private fun Graph.findOrAdd(label: String): Node {
    println("Add $label")
    return (find { it.label == label } ?: Node(label))
        .also {
            add(it)
        }
}
*/
