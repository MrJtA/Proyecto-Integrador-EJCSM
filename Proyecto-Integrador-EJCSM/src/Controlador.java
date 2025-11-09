import java.io.*;
import java.sql.SQLException;

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
                case 1 -> menuFicheros();
                case 2 -> {
                    funcionalidades = new Database(vista.pedirDatabase());
                    subMenu();
                }
                case 5 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

    public void menuFicheros() throws IOException, SQLException {
        boolean seguir = true;
        while (seguir) {
            File fichero = vista.pedirFichero();
            String tipoFichero = vista.comprobarFichero(fichero);
            switch (tipoFichero) {
                case ".txt" -> {
                    funcionalidades = new Texto(fichero);
                    subMenu();
                }
                case ".bin" -> {
                    funcionalidades = new Binario(fichero);
                    subMenu();
                }
                case ".xml" -> {
                    funcionalidades = new XML(fichero);
                    subMenu();
                }
                default -> seguir = true;
            }
        }
    }

    public void subMenu() throws IOException, SQLException {
        boolean seguir = true;
        while (seguir) {
            vista.subMenu();
            int opcion = vista.opcion();
            switch (opcion) {
                case 1 -> vista.buscar(funcionalidades.leerFichero(), vista.pedirLibro(funcionalidades.leerFichero()));
                case 2 -> vista.mostrar(funcionalidades.leerFichero());
                case 3 -> funcionalidades.insertar(vista.crearLibro());
                case 4 -> funcionalidades.borrar(vista.pedirLibro(funcionalidades.leerFichero()));
                case 5 -> funcionalidades.modificar(vista.pedirLibro(funcionalidades.leerFichero()), vista.crearLibro());
                case 6 -> funcionalidades.traspasarDatosFichero(vista.pedirFichero());
                case 7 -> funcionalidades.traspasarDatosDatabase(vista.pedirDatabase());
                case 8 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

}