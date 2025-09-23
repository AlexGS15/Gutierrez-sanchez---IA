import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Arbol arbol = new Arbol();

        // Insertar nodos pidiendo al usuario
        System.out.println("¿Cuántos nodos deseas insertar?");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            arbol.insertarDesdeUsuario();
        }

        // Imprimir en PreOrden
        System.out.print("Recorrido PreOrden: ");
        arbol.imprimirPreOrden();

        // Probar búsqueda
        System.out.print("Ingresa un valor a buscar: ");
        int buscar = sc.nextInt();
        if (arbol.buscarNodo(buscar)) {
            System.out.println("El valor " + buscar + " SÍ se encuentra en el árbol.");
        } else {
            System.out.println("El valor " + buscar + " NO se encuentra en el árbol.");
        }

        sc.close();
    }
}


class Nodo {
    int valor;
    Nodo izq;
    Nodo der;

    Nodo(int valor) {
        this.valor = valor;
        this.izq = null;
        this.der = null;
    }
}

class Arbol {
    Nodo raiz;

    boolean estaVacio() {
        return raiz == null;
    }

    // Método para pedir valor al usuario y luego insertar
    void insertarDesdeUsuario() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingresa el valor del nodo a insertar: ");
        int valor = sc.nextInt();
        raiz = insertarRec(raiz, valor);
    }

    private Nodo insertarRec(Nodo actual, int valor) {
        if (actual == null) {
            return new Nodo(valor);
        }
        if (valor < actual.valor) {
            actual.izq = insertarRec(actual.izq, valor);
        } else if (valor > actual.valor) {
            actual.der = insertarRec(actual.der, valor);
        }
        // Si el valor ya existe, no se inserta nada
        return actual;
    }

    // Método buscar nodo
    boolean buscarNodo(int valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(Nodo actual, int valor) {
        if (actual == null) {
            return false;
        }
        if (valor == actual.valor) {
            return true;
        }
        return valor < actual.valor
                ? buscarRec(actual.izq, valor)
                : buscarRec(actual.der, valor);
    }

    // Imprimir solo en PreOrden
    void imprimirPreOrden() {
        imprimirPre(raiz);
        System.out.println();
    }

    private void imprimirPre(Nodo actual) {
        if (actual != null) {
            System.out.print(actual.valor + " ");
            imprimirPre(actual.izq);
            imprimirPre(actual.der);
        }
    }
}
