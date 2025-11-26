import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

//    5 6
//    1 2 2
//    2 5 5
//    2 3 4
//    1 4 1
//    4 3 3
//    3 5 1
public class Main {
    static List<List<Pair>> graph;
    static int vertex;
    static int edge;
    static int[] parent;
    static long[] dist;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        vertex = sc.nextInt(); // ler as entradas da quantidade de vertex
        edge = sc.nextInt(); // ler as entradas da quantidade de edge

        graph = new ArrayList<>();
        // Esse for tem que guardar todos os vertex,
        // pois independente da quantidade de edges quem irá guardar será o vertex essa adjacências
        for (int i = 0; i <= vertex; i++) {
            graph.add(new ArrayList<>());
        }

        // Esse for tem que salvar todos os caminho/edges que estão ligados a vertex
        for(int i = 0; i < edge; i++){
            int u = sc.nextInt(); // Vertex que irá apontar para as adjacências
            int v = sc.nextInt(); // Vertex adjacente a U
            int w = sc.nextInt(); // Peso/valor da ligação de U a V

            // salva o caminho "indo e voltando", pois é um grafo não direcionado
            graph.get(u).add(new Pair(v, w));
            graph.get(v).add(new Pair(u, w));
        }
        // seta o numero de vertex total em parent and dist
        parent = new int[vertex + 1];
        dist = new long[vertex + 1];
        
        // chama a função
        dijkstra(1);

        // Constroi o path
        List<Integer> path = getPath(1, vertex);
        // Se empty não tem caminho
        if (path.isEmpty()) {
            System.out.println("-1");
        } else {
            // Impressão do caminho no terminal
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(path.get(i));
            }
            System.out.println();
        }


        sc.close();
    }

    public static void dijkstra(int start){
        // Cria uma PQ
        PriorityQueue<Node> graphPQ = new PriorityQueue<>();
        // Aloca em todos os vertex -1, menos no primeiro, já que ele é 0, e em todos os edges infinity, menos no primeiro. 
        for (int i = 1; i <= vertex; i++) {
            dist[i] = Long.MAX_VALUE;
            parent[i] = -1;
        }
        
        dist[start] = 0;
        graphPQ.add(new Node(start, 0L));

        // Vasculha a PQ até ela está vazia, enquanto ela não estiver vazia fica rodando o while
        while (!graphPQ.isEmpty()){
            // Com base na prioridade definida em Node, que é a distancia, ela retira a menor distancia
            Node top = graphPQ.poll();
            // Seta a distancia
            long distAtual = top.dist;
            // Seta o node
            int u = top.node;

            // Verifica se a distancia que já tem é menor que a distancia do node adjacente, caso sim continua com a distancia que está em U, 
            // caso não continua para os próximo ifs 
            if (distAtual > dist[u]) continue;

            // Verificação para que não ocorra overflow
            if(dist[u] == Long.MAX_VALUE) continue;

            // Aqui começa a mágica, ele ver dentro de U, os vertex e edge (os vertex adjacente e o edge até ele)
            for (Pair edge : graph.get(u)) {
                // Seta o caminho e o nó em V e W (v de vertex e w de weight)
                int v = edge.node;
                int w = edge.weight;

                // Aqui realizar a soma do Weight de U + Weight de V
                long nd = dist[u] + (long) w;

                // Caso seja menor realiza a mudança para o novo local, U irá apontar o caminho para V e V receberá o Weight de U + V
                if(nd < dist[v]){
                    dist[v] = nd;
                    parent[v] = u;
                    // Após realizar o apontamento adicionar V a lista de PQ
                    graphPQ.add(new Node(v, dist[v]));
                }
            }
        }
    }


    public static List<Integer> getPath(int start, int end) {
        List<Integer> path = new ArrayList<>();
        // se não existe caminho
        if (dist[end] == Long.MAX_VALUE) return path;

        int cur = end;
        // Irá iniciar do ultimo node, que seria o destino final
        // ao fazer isso ele precisa chegar em um nó que o seu parente
        // é -1, ele é -1 para informar que ele não tem parente, ele é o inicio
        // a partir disso ele percorre do último ao primeiro (-1), armazena na lista cada node
        // e ao final inverte.
        while (cur != -1) {
            path.add(cur);
            if (cur == start) break;
            cur = parent[cur];
        }

        Collections.reverse(path);
        return path;
    }
    // Node é usaddo para saber qual é Node e Dist até ele.
    static class Node implements Comparable<Node> {
        int node;
        long dist;

        public Node(int node, long dist){
            this.node = node;
            this.dist = dist;
        }

        // Aqui é realizado a questão do compareTo com outro Dist, ou seja, compara o Weight de U -> V com o que já existe em V
        @Override
        public int compareTo(Node other) {
            return Long.compare(this.dist, other.dist);
        }
    }

    // Pair serve para guardaro Node e o Weight dele, ele é usado para construir a lista de adjacência
    static public class Pair {
        public int node;
        public int weight;

        public Pair(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    // É um leitor de entrada mais eficaz que o Scanner, utilizei ele para ganhar performance e conseguir passar para entradas grandes.
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