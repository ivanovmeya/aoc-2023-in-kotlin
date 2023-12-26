package Day25.karger

import java.util.*

class MinCut(private val V: Int, private val E: Int, private val vertices: Set<String>) {
    private var rand = Random()

    private val rank = mutableMapOf<String, Int>().apply {
        for (vertice in vertices) {
            this[vertice] = 0
        }
    }

    private val parent = vertices.associate { it to it }.toMutableMap()

    // Function to find minimum cut.
    fun minCutKarger(edges: List<Edge>): Int {
        var vertices = V

        // Iterating till vertices are
        // greater than 2.
        while (vertices > 2) {
            // Getting a random integer
            // in the range [0, E-1].
            val i = rand.nextInt(E)

            // Finding leader element to which
            // edges[i].u belongs.
            val set1 = find(edges[i].u)
            // Finding leader element to which
            // edges[i].v belongs.
            val set2 = find(edges[i].v)

            // If they do not belong
            // to the same set.
            if (set1 != set2) {
                // Merging vertices u and v into one.
                union(edges[i].u, edges[i].v)
                // Reducing count of vertices by 1.
                vertices--
            }
        }
        println("Edges needs to be removed - ")
        var ans = 0
        for (i in 0 until E) {
            // Finding leader element to which edges[i].u belongs.
            val set1 = find(edges[i].u)
            // Finding leader element to which edges[i].v belongs.
            val set2 = find(edges[i].v)

            if (set1 != set2) {
                println(edges[i].u + " <----> " + edges[i].v)
                ans++
            }
        }

        return ans
    }

    fun calcMultiplyOfSetSizes(): Int {

        val sets = parent.entries.groupBy { it.value }
        println("It has ${sets.size} sets")

        var multiply = 1
        for (set in sets) {
            multiply *= set.value.size
        }

        return multiply
    }

    //find with path compression
    // If the node is the parent of
    // itself then it is the leader
    // of the tree.
    fun find(node: String): String {
        if (node != parent[node]) {
            parent[node] = find(parent[node]!!)
        }
        return parent[node]!!
    }

    //returns true if union connects x and y, false if already was connected
    fun union(x: String, y: String): Boolean {
        val parentX = find(x)
        val parentY = find(y)

        if (parentX == parentY) return false

        //union by rank
        when {
            rank[parentX]!! < rank[parentY]!! -> parent[parentX] = parentY
            rank[parentX]!! > rank[parentY]!! -> parent[parentY] = parentX
            else -> {
                parent[parentX] = parentY
                rank[parentY] = rank.getOrDefault(parentY, 0) + 1
            }
        }
        return true
    }

}

data class Edge(var u: String, var v: String)