import java.util.ArrayList;
import java.util.List;

public class Nodo {
    String estado;
    Nodo padre;
    int profundidad;
    int costo;


    public Nodo(String estado, Nodo padre) {
        this.estado = estado;
        this.padre = padre;
        if (padre == null) {
            this.profundidad = 0;
            this.costo = 0;
        } else {
            this.profundidad = padre.profundidad + 1;
            this.costo = padre.costo + 1;
        }
    }


    public List<String> obtenerSucesores() {
        List<String> sucesores = new ArrayList<>();
        int posEspacio = estado.indexOf(" ");

        switch (posEspacio) {
            case 0: // Mueve derecha, abajo
                sucesores.add(intercambiarCaracteres(0, 1));
                sucesores.add(intercambiarCaracteres(0, 3));
                break;
            case 1: // Mueve izquierda, derecha, abajo
                sucesores.add(intercambiarCaracteres(1, 0));
                sucesores.add(intercambiarCaracteres(1, 2));
                sucesores.add(intercambiarCaracteres(1, 4));
                break;
            case 2: // Mueve izquierda, abajo
                sucesores.add(intercambiarCaracteres(2, 1));
                sucesores.add(intercambiarCaracteres(2, 5));
                break;
            case 3: // Mueve arriba, derecha, abajo
                sucesores.add(intercambiarCaracteres(3, 0));
                sucesores.add(intercambiarCaracteres(3, 4));
                sucesores.add(intercambiarCaracteres(3, 6));
                break;
            case 4: // Mueve arriba, izquierda, derecha, abajo
                sucesores.add(intercambiarCaracteres(4, 1));
                sucesores.add(intercambiarCaracteres(4, 3));
                sucesores.add(intercambiarCaracteres(4, 5));
                sucesores.add(intercambiarCaracteres(4, 7));
                break;
            case 5: // Mueve arriba, izquierda, abajo
                sucesores.add(intercambiarCaracteres(5, 2));
                sucesores.add(intercambiarCaracteres(5, 4));
                sucesores.add(intercambiarCaracteres(5, 8));
                break;
            case 6: // Mueve arriba, derecha
                sucesores.add(intercambiarCaracteres(6, 3));
                sucesores.add(intercambiarCaracteres(6, 7));
                break;
            case 7: // Mueve arriba, izquierda, derecha
                sucesores.add(intercambiarCaracteres(7, 4));
                sucesores.add(intercambiarCaracteres(7, 6));
                sucesores.add(intercambiarCaracteres(7, 8));
                break;
            case 8: // Mueve arriba, izquierda
                sucesores.add(intercambiarCaracteres(8, 5));
                sucesores.add(intercambiarCaracteres(8, 7));
                break;
        }
        return sucesores;
    }
        private String intercambiarCaracteres(int pos1, int pos2) {
        char[] caracteres = this.estado.toCharArray();
        char temp = caracteres[pos1];
        caracteres[pos1] = caracteres[pos2];
        caracteres[pos2] = temp;
        return new String(caracteres);
    }
}
