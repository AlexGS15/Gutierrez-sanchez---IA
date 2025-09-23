import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {

    public static void main(String[] args) {
        // Define los estados inicial y final del puzzle 8
        String estadoInicial = "7245 6831";
        String estadoFinal = " 12345678";

        Arbol arbol = new Arbol(new Nodo(estadoInicial, null));


        System.out.println("--- Ejecutando Búsqueda Primero en Anchura (BFS) ---");
        Nodo resultadoBFS = arbol.realizarBusquedaEnAnchura(estadoFinal);
        imprimirSolucion(resultadoBFS, estadoInicial);
      
        
        System.out.println("\n--- Ejecutando Búsqueda Primero en Profundidad (DFS) ---");
        Nodo resultadoDFS = arbol.realizarBusquedaEnProfundidad(estadoFinal);
        imprimirSolucion(resultadoDFS, estadoInicial); 
        

        System.out.println("\n--- Ejecutando Búsqueda por Costo Uniforme (UCS) ---");
        Nodo resultadoUCS = arbol.realizarBusquedaCostoUniforme(estadoFinal);
        imprimirSolucion(resultadoUCS, estadoInicial);

        System.out.println("\n--- Ejecutando Búsqueda en Profundidad Limitada (DLS) ---");
        int limiteDeProfundidad = 20; 
        System.out.println("Límite establecido en: " + limiteDeProfundidad);

    
        System.out.println("\n--- Ejecutando Búsqueda A* (UCS con Heurística) ---");
        Object[] resultadoAStar = arbol.realizarBusquedaAEstrella(estadoFinal);
        Nodo nodoFinalAStar = (Nodo) resultadoAStar[0];
        int nodosGeneradosAStar = (Integer) resultadoAStar[1];
        System.out.println("Nodos generados por A*: " + nodosGeneradosAStar);
        imprimirSolucion(nodoFinalAStar, estadoInicial);

    }


    public static void imprimirSolucion(Nodo nodoFinal, String estadoInicial) {
        if (nodoFinal == null) {
            System.out.println("No se encontró una solución.");
            return;
        }

        List<Nodo> camino = new ArrayList<>();
        Nodo actual = nodoFinal;
        while (actual != null) {
            camino.add(actual);
            actual = actual.padre;
        }
        Collections.reverse(camino);

        System.out.println("Solución encontrada en " + nodoFinal.profundidad + " pasos.");
        System.out.println("Costo total del camino: " + nodoFinal.costo);

        for (Nodo nodo : camino) {
            System.out.println("Paso " + nodo.profundidad + ":");
            imprimirTablero(nodo.estado);
            System.out.println("----------");
        }
    }


    public static void imprimirTablero(String estado) {
        System.out.println(estado.substring(0, 3));
        System.out.println(estado.substring(3, 6));
        System.out.println(estado.substring(6, 9));
    }
}