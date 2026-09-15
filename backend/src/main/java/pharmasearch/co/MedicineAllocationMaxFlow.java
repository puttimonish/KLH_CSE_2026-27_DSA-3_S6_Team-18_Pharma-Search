package pharmasearch.co;

/**
 * CO4 - Network Flow
 *
 * Models medicine allocation as a capacity-constrained
 * network-flow problem.
 *
 * Source -> Medicine Demand -> Pharmacy -> Sink
 *
 * Edmonds-Karp is used to compute the maximum amount
 * of medicine that can be allocated through the network.
 *
 * Time Complexity:
 * O(V * E^2)
 */
public final class MedicineAllocationMaxFlow {

    private MedicineAllocationMaxFlow() {
        // Utility class
    }

    /**
     * Computes maximum medicine allocation using
     * the Edmonds-Karp implementation of Ford-Fulkerson.
     *
     * @param capacity capacity matrix
     * @param source source vertex
     * @param sink sink vertex
     * @return maximum possible flow
     */
    public static int maxFlow(
            int[][] capacity,
            int source,
            int sink) {

        if (capacity == null || capacity.length == 0) {
            return 0;
        }

        int n = capacity.length;

        if (source < 0 || source >= n
                || sink < 0 || sink >= n
                || source == sink) {
            return 0;
        }

        int[][] residual = new int[n][n];

        for (int i = 0; i < n; i++) {
            if (capacity[i] == null
                    || capacity[i].length != n) {
                return 0;
            }

            for (int j = 0; j < n; j++) {
                if (capacity[i][j] < 0) {
                    return 0;
                }

                residual[i][j] = capacity[i][j];
            }
        }

        int maxFlow = 0;

        while (true) {

            int[] parent =
                    new int[n];

            boolean[] visited =
                    new boolean[n];

            if (!bfs(
                    residual,
                    source,
                    sink,
                    parent,
                    visited)) {
                break;
            }

            int pathFlow =
                    Integer.MAX_VALUE;

            int current = sink;

            while (current != source) {

                int previous =
                        parent[current];

                pathFlow =
                        Math.min(
                                pathFlow,
                                residual[previous][current]
                        );

                current = previous;
            }

            current = sink;

            while (current != source) {

                int previous =
                        parent[current];

                residual[previous][current]
                        -= pathFlow;

                residual[current][previous]
                        += pathFlow;

                current = previous;
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    /**
     * Breadth-first search for an augmenting path.
     */
    private static boolean bfs(
            int[][] residual,
            int source,
            int sink,
            int[] parent,
            boolean[] visited) {

        int n = residual.length;

        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }

        int[] queue =
                new int[n];

        int front = 0;
        int rear = 0;

        queue[rear++] = source;
        visited[source] = true;

        while (front < rear) {

            int current =
                    queue[front++];

            for (int next = 0;
                    next < n;
                    next++) {

                if (!visited[next]
                        && residual[current][next] > 0) {

                    parent[next] =
                            current;

                    visited[next] = true;

                    if (next == sink) {
                        return true;
                    }

                    queue[rear++] = next;
                }
            }
        }

        return false;
    }
}