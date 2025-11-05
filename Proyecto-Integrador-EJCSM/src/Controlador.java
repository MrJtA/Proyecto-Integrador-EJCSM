
import java.io.IOException;
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
                case 1 -> {
                    funcionalidades = new Texto(vista.pedirFichero());
                    subMenu();
                }
                case 2 -> {
                    funcionalidades = new Binario(vista.pedirFichero());
                    subMenu();
                }
                case 3 -> {
                    funcionalidades = new XML(vista.pedirFichero());
                    subMenu();
                }
                case 4 -> {
                    funcionalidades = new Database();
                }
                case 5 -> seguir = false;
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
                case 1 -> vista.buscar(funcionalidades.leerFichero(), vista.pedirLibro());
                case 2 -> vista.mostrar(funcionalidades.leerFichero());
                case 3 -> funcionalidades.insertar(vista.crearLibro());
                case 4 -> funcionalidades.borrar(vista.pedirLibro());
                case 5 -> funcionalidades.modificar(vista.pedirLibro(), vista.crearLibro());
                case 6 -> funcionalidades.traspasarDatosFichero(vista.pedirFichero());
                case 7 -> funcionalidades.traspasarDatosDatabase(vista.pedirDatabase());
                case 8 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

}