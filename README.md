![alt text](image.png)


Pseudocode dijkstra:
função dijkstra(início):

    criar uma fila de prioridade PQ (menor distância primeiro)

    para cada nó i:
        dist[i] = infinito
        parent[i] = -1

    dist[início] = 0
    inserir na PQ o par (0, início)

    enquanto PQ não está vazia:
        remover da PQ o par (distAtual, u)

        se distAtual > dist[u]:
            continuar  

        para cada aresta (u → v) com peso w:
            se dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u
                inserir na PQ o par (dist[v], v)

fim da função


Implementação que passou:


import java.io.*;
import java.util.*;
 
public class Main {
    static List<List<Pair>> graph;
    static int vertex;
    static int edge;
    static int[] parent;
    static long[] dist;
 
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        StringBuilder out = new StringBuilder();
 
        vertex = in.nextInt();
        edge = in.nextInt();
 
        graph = new ArrayList<>(vertex + 1);
        for (int i = 0; i <= vertex; i++) graph.add(new ArrayList<>());
 
        for (int i = 0; i < edge; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int w = in.nextInt();
            graph.get(u).add(new Pair(v, w));
            graph.get(v).add(new Pair(u, w));
        }
 
        parent = new int[vertex + 1];
        dist = new long[vertex + 1];
 
        dijkstra(1);
 
        List<Integer> path = getPath(1, vertex);
        if (path.isEmpty()) {
            out.append("-1\n");
        } else {
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) out.append(' ');
                out.append(path.get(i));
            }
            out.append('\n');
        }
 
        System.out.print(out.toString());
    }
 
    public static void dijkstra(int start) {
        PriorityQueue<Node> graphPQ = new PriorityQueue<>();
 
        for (int i = 1; i <= vertex; i++) {
            dist[i] = Long.MAX_VALUE;
            parent[i] = -1;
        }
 
        dist[start] = 0L;
        graphPQ.add(new Node(start, 0L));
 
        while (!graphPQ.isEmpty()) {
            Node top = graphPQ.poll();
            long distAtual = top.dist;
            int u = top.node;
 
            if (distAtual > dist[u]) continue;
            if (dist[u] == Long.MAX_VALUE) continue;
 
            for (Pair edge : graph.get(u)) {
                int v = edge.node;
                int w = edge.weight;
 
                long nd = dist[u] + (long) w;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    parent[v] = u;
                    graphPQ.add(new Node(v, nd));
                }
            }
        }
    }
 
    public static List<Integer> getPath(int start, int end) {
        List<Integer> path = new ArrayList<>();
        if (dist[end] == Long.MAX_VALUE) return path;
 
        int cur = end;
        while (cur != -1) {
            path.add(cur);
            if (cur == start) break;
            cur = parent[cur];
        }
        Collections.reverse(path);
        return path;
    }
 
    static class Node implements Comparable<Node> {
        int node;
        long dist;
        public Node(int node, long dist) { this.node = node; this.dist = dist; }
        @Override public int compareTo(Node other) { return Long.compare(this.dist, other.dist); }
    }
 
    static public class Pair {
        public int node;
        public int weight;
        public Pair(int node, int weight) { this.node = node; this.weight = weight; }
    }
 
    static class FastReader {
        private final BufferedReader br;
        private StringTokenizer st;
        FastReader() { br = new BufferedReader(new InputStreamReader(System.in)); }
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
    }
}