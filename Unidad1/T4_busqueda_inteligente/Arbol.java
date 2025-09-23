import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;



public class Arbol {
    Nodo raiz;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    public Nodo realizarBusquedaEnAnchura(String objetivo){
        int tamano=1;
        Queue<Nodo> cola = new LinkedList<Nodo>();
        HashSet<String> visitados = new HashSet<String>();
        cola.add(raiz);
        visitados.add(raiz.estado);
        boolean encontrado = false;
        Nodo actual = null;
        while(!encontrado && cola.size() > 0){
            actual = cola.poll();
            System.out.println("Procesando => "+actual.estado);
        
            if(actual.estado.equals(objetivo)){
                encontrado = true;
            }else{
                List<String> sucesores = actual.obtenerSucesores();
                for(String sucesor: sucesores){
             
                    if(visitados.contains(sucesor))
                        continue;
                    System.out.println("Agergando a cola => "+sucesor);
                    cola.add(new Nodo(sucesor, actual));
                    tamano++;
                    visitados.add(sucesor);
                }
            }
          
        }
        System.out.println(tamano);
       
        return actual;
    }

     public Nodo realizarBusquedaEnProfundidad(String objetivo) {
        int tamano=1;
        Stack<Nodo> pila = new Stack<>();
        HashSet<String> visitados = new HashSet<>();
        pila.push(raiz);
        visitados.add(raiz.estado);

        while (!pila.isEmpty()) {
            Nodo actual = pila.pop();
            if (actual.estado.equals(objetivo)) {
                return actual; 
            }


            for (String sucesorEstado : actual.obtenerSucesores()) {
                if (!visitados.contains(sucesorEstado)) {
                    tamano++;
                    visitados.add(sucesorEstado);
                    Nodo nodoHijo = new Nodo(sucesorEstado, actual);
                    pila.push(nodoHijo);
                }
            }
        }
        System.out.println(tamano);
        return null; 
    }

     public Nodo realizarBusquedaCostoUniforme(String objetivo) {
        int tamano=1;
       
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(n -> n.costo));
        HashSet<String> visitados = new HashSet<>();
        
        colaPrioridad.add(raiz);
        visitados.add(raiz.estado);

        while (!colaPrioridad.isEmpty()) {
            Nodo actual = colaPrioridad.poll();
            if (actual.estado.equals(objetivo)) {
                return actual;
            }

            for (String sucesorEstado : actual.obtenerSucesores()) {
                if (!visitados.contains(sucesorEstado)) {
                    tamano++;
                    visitados.add(sucesorEstado);
                    Nodo nodoHijo = new Nodo(sucesorEstado, actual);
                    colaPrioridad.add(nodoHijo);
                }
            }
        }
        System.out.println("total de nodos " + tamano);
        return null; 
    }

   public Object[] realizarBusquedaProfundidadLimitada(String objetivo, int limite) {

    int nodosGenerados = 1; 
    Stack<Nodo> pila = new Stack<>();
    HashSet<String> visitados = new HashSet<>(); 

    pila.push(raiz);
    visitados.add(raiz.estado);

    while (!pila.isEmpty()) {
        Nodo actual = pila.pop();
        if (actual.estado.equals(objetivo)) {
            return new Object[]{actual, nodosGenerados, "encontrado"};
        }

        if (actual.profundidad >= limite) {
            continue;
        }
        
        for (String sucesorEstado : actual.obtenerSucesores()) {
            if (!visitados.contains(sucesorEstado)) {
                visitados.add(sucesorEstado);
                Nodo nodoHijo = new Nodo(sucesorEstado, actual);
                nodosGenerados++;
                pila.push(nodoHijo);
            }
        }
                System.out.println("total de nodos " + nodosGenerados);
    }
    
    return new Object[]{null, nodosGenerados, "limite_alcanzado"};
}



public Object[] realizarBusquedaAEstrella(String objetivo) {
    int nodosGenerados = 1; 
    

    PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(
        n -> n.costo + calcularHeuristica(n.estado, objetivo)
    ));
    
    HashSet<String> visitados = new HashSet<>();
    
    colaPrioridad.add(raiz);
    visitados.add(raiz.estado);

    while (!colaPrioridad.isEmpty()) {
        Nodo actual = colaPrioridad.poll();
        if (actual.estado.equals(objetivo)) {
            return new Object[]{actual, nodosGenerados};
        }

        for (String sucesorEstado : actual.obtenerSucesores()) {
            if (!visitados.contains(sucesorEstado)) {
                visitados.add(sucesorEstado);
                Nodo nodoHijo = new Nodo(sucesorEstado, actual);
                nodosGenerados++;
                colaPrioridad.add(nodoHijo);
            }
        }
    }
    return new Object[]{null, nodosGenerados};
}


    public int calcularHeuristica(String estadoActual, String estadoObjetivo) {
        int h = 0;
        for (int i = 0; i < estadoActual.length(); i++) {
            if (estadoActual.charAt(i) != ' ' && estadoActual.charAt(i) != estadoObjetivo.charAt(i)) {
                h++;
            }
        }
        return h;
    }

}


