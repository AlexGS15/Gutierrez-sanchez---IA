import java.util.*;

// Clase para representar una arista con su nodo de destino y su peso
class Arista {
    int destino;
    int peso;

    public Arista(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
}

// Clase para representar un nodo en la cola de prioridad
class Nodo implements Comparable<Nodo> {
    int vertice;
    int distancia;

    // ESTE ES EL CONSTRUCTOR QUE EL ERROR DICE QUE NO ENCUENTRA
    public Nodo(int vertice, int distancia) {
        this.vertice = vertice;
        this.distancia = distancia;
    }

    @Override
    public int compareTo(Nodo otro) {
        return Integer.compare(this.distancia, otro.distancia);
    }
}

public class T3_Dijkstra { // <-- CAMBIO 1
    private final int V; // Número de vértices
    private final List<List<Arista>> adyacencia; // Lista de adyacencia

    public T3_Dijkstra(int V) { // <-- CAMBIO 2
        this.V = V;
        adyacencia = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adyacencia.add(new ArrayList<>());
        }
    }

    // Método para añadir una arista al grafo
    public void agregarArista(int origen, int destino, int peso) {
        adyacencia.get(origen).add(new Arista(destino, peso));
    }

    // Implementación del algoritmo de Dijkstra
    public void dijkstra(int origen) {
        int[] distancias = new int[V];
        Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[origen] = 0;

        PriorityQueue<Nodo> pq = new PriorityQueue<>();
        pq.add(new Nodo(origen, 0));

        while (!pq.isEmpty()) {
            Nodo nodoActual = pq.poll();
            int u = nodoActual.vertice;

            if (nodoActual.distancia > distancias[u]) {
                continue;
            }

            for (Arista arista : adyacencia.get(u)) {
                int v = arista.destino;
                int pesoArista = arista.peso;

                if (distancias[u] + pesoArista < distancias[v]) {
                    distancias[v] = distancias[u] + pesoArista;
                    pq.add(new Nodo(v, distancias[v]));
                }
            }
        }

        imprimirSolucion(origen, distancias);
    }

    // Método para imprimir el resultado
    private void imprimirSolucion(int origen, int[] distancias) {
        System.out.println("Algoritmo de Dijkstra (desde el vértice " + origen + ")");
        System.out.println("Vértice \t Distancia desde el origen");
        for (int i = 0; i < V; i++) {
            System.out.println(i + " \t\t " + (distancias[i] == Integer.MAX_VALUE ? "Infinito" : distancias[i]));
        }
    }

    public static void main(String[] args) {
        int V = 5;
        T3_Dijkstra g = new T3_Dijkstra(V); 

        g.agregarArista(0, 1, 9);
        g.agregarArista(0, 2, 6);
        g.agregarArista(0, 3, 5);
        g.agregarArista(0, 4, 3);
        g.agregarArista(2, 1, 2);
        g.agregarArista(2, 3, 4);
        
        g.dijkstra(0);
    }
}