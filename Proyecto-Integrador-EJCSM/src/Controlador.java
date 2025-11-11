import java.io.*;
import java.sql.SQLException;
import java.util.Map;

public class Controlador {

    private final Vista vista = new Vista();
    private Funcionalidades funcionalidades;

    public static void main(String[] args) throws IOException, SQLException {
        Controlador app = new Controlador();
        app.menu();
    }

    public void menu() throws IOException, SQLException {
        boolean seguir = true;
        while (seguir) {
            vista.menu();
            int opcion = vista.opcion();
            switch (opcion) {
                case 1 -> {
                    String fichero = vista.pedirFichero();
                    if (fichero.endsWith(".txt")) {
                        funcionalidades = new Texto(vista.crearFichero(fichero));
                        subMenu();
                    }
                    if (fichero.endsWith(".bin")) {
                        funcionalidades = new Binario(vista.crearFichero(fichero));
                        subMenu();
                    }
                    if (fichero.endsWith(".xml")) {
                        funcionalidades = new XML(vista.crearFichero(fichero));
                        subMenu();
                    }
                }
                case 2 -> {
                    funcionalidades = new Database(vista.pedirDatabase());
                    subMenu();
                }
                case 3 -> seguir = false;
                default -> seguir = true;
            }
        }
    }
    
    public void subMenu() throws IOException, SQLException {
        boolean seguir = true;
        while (seguir) {
            vista.subMenu();
            int opcion = vista.opcion();
            Map<Integer, Libro> biblioteca = funcionalidades.leerFichero();
            switch (opcion) {
                case 1 -> vista.buscar(biblioteca, vista.pedirLibro(biblioteca));
                case 2 -> vista.mostrar(funcionalidades.leerFichero());
                case 3 -> funcionalidades.insertar(vista.crearLibro());
                case 4 -> funcionalidades.borrar(vista.pedirLibro(biblioteca));
                case 5 -> funcionalidades.modificar(vista.pedirLibro(biblioteca), vista.crearLibro());
                case 6 -> funcionalidades.traspasarDatosFichero(vista.crearFichero(vista.pedirFichero()));
                case 7 -> funcionalidades.traspasarDatosDatabase(vista.pedirDatabase());
                case 8 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

}